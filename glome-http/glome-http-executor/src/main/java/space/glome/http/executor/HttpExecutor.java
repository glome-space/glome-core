package space.glome.http.executor;

import static space.glome.http.executor.ApacheHttpConverters.convert;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpResponse;

public class HttpExecutor {

	public HttpExecutor() {
	}

	public HttpRecord exec(HttpRequest request) throws Exception {

		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

		TrustManager[] trustManagers = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		KeyManager[] keyManagers = null;

		// TODO this is just a guess - to be tested and covered with JUnit
		if (request.getCertificate() != null) {
			X509Certificate cert = generateCertificate(request.getCertificate().getCert().getBytes());
			RSAPrivateKey key = generatePrivateKey(request.getCertificate().getKey().getBytes());

			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(null);
			keystore.setCertificateEntry("cert-alias", cert);
			keystore.setKeyEntry("key-alias", key, "changeit".toCharArray(), new Certificate[] { cert });

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keystore, "changeit".toCharArray());
			keyManagers = keyManagerFactory.getKeyManagers();
		}

		SSLContext sslContext = SSLContexts.custom().build();
		sslContext.init(keyManagers, trustManagers, new SecureRandom());

		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(sslContext).build()) {
			HttpUriRequest httpUriRequest = null;
			switch (request.getMethod()) {
			case GET:
				httpUriRequest = new HttpGet(convert(request.getUrl()));
				break;
			case POST:
				HttpPost httpPost = new HttpPost(convert(request.getUrl()));
				httpPost.setEntity(new ByteArrayEntity(convert(request.getRequestBody())));
				httpUriRequest = httpPost;
				break;
			case PUT:
				HttpPut httpPut = new HttpPut(convert(request.getUrl()));
				httpPut.setEntity(new ByteArrayEntity(convert(request.getRequestBody())));
				httpUriRequest = httpPut;
				break;
			default:
				throw new Error("Method " + request.getMethod() + " not supported");
			}
			httpUriRequest.setHeaders(convert(request.getHeaders()));

			try (CloseableHttpResponse httpResponse = httpclient.execute(httpUriRequest)) {
				HttpResponse response = new HttpResponse();
				HttpEntity entity = httpResponse.getEntity();
				response.setStatus(httpResponse.getStatusLine().toString());
				response.setCode(httpResponse.getStatusLine().getStatusCode());
				response.setHeaders(convert(httpResponse.getAllHeaders()));
				if (entity != null) {
					response.setResponseBody(convertStreamToString(httpResponse.getEntity().getContent()));
					EntityUtils.consume(entity);
				}
				return new HttpRecord(request, response);
			}
		}
	}

	private static String convertStreamToString(java.io.InputStream is) {
		return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
	}

	protected static RSAPrivateKey generatePrivateKey(byte[] keyBytes)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
	}

	protected static X509Certificate generateCertificate(byte[] certBytes) throws CertificateException {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

}
