package space.glome.http.executor;

import static space.glome.http.executor.ApacheHttpConverters.convert;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpResponse;
import space.glome.http.schema.domain.JksCertificate;
import space.glome.http.schema.domain.PemCertificate;

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

		if (request.getCertificate() instanceof PemCertificate) {
			Certificate cert = generateCertificate(((PemCertificate) request.getCertificate()).getCert());
			PrivateKey key = generatePrivateKey(((PemCertificate) request.getCertificate()).getKey());
			String passphrase = request.getCertificate().getPassphrase();

			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(null);
			keystore.setCertificateEntry("cert-alias", cert);
			keystore.setKeyEntry("key-alias", key, passphrase.toCharArray(), new Certificate[] { cert });

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keystore, passphrase.toCharArray());
			keyManagers = keyManagerFactory.getKeyManagers();
		} else if (request.getCertificate() instanceof JksCertificate) {
			String passphrase = request.getCertificate().getPassphrase();
			KeyStore keystore = KeyStore.getInstance("JKS");
			InputStream readStream = new FileInputStream(((JksCertificate) request.getCertificate()).getFilePath());
			keystore.load(readStream, passphrase.toCharArray());
			readStream.close();

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keystore, passphrase.toCharArray());
			keyManagers = keyManagerFactory.getKeyManagers();
		}

		SSLContext sslContext = SSLContexts.custom().build();
		sslContext.init(keyManagers, trustManagers, new SecureRandom());

		RequestConfig config = RequestConfig.custom().setConnectTimeout(2000).setConnectionRequestTimeout(2000)
				.setSocketTimeout(2000).build();

		try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(sslContext).build()) {
			HttpUriRequest httpUriRequest = null;
			switch (request.getMethod()) {
			case GET:
				httpUriRequest = new HttpGet(convert(request.getUrl()));
				break;
			case POST:
				HttpPost httpPost = new HttpPost(convert(request.getUrl()));
				httpPost.setEntity(new ByteArrayEntity(request.getPayload()));
				httpUriRequest = httpPost;
				break;
			case PUT:
				HttpPut httpPut = new HttpPut(convert(request.getUrl()));
				httpPut.setEntity(new ByteArrayEntity(request.getPayload()));
				httpUriRequest = httpPut;
				break;
			default:
				throw new Error("Method " + request.getMethod() + " not supported");
			}
			httpUriRequest.setHeaders(convert(request.getHeaders()));

			HttpResponse response = new HttpResponse();
			try (CloseableHttpResponse apacheResponse = httpclient.execute(httpUriRequest)) {
				response.setStatus(apacheResponse.getStatusLine().toString());
				response.setCode(apacheResponse.getStatusLine().getStatusCode());
				response.setHeaders(convert(apacheResponse.getAllHeaders()));

				HttpEntity entity = apacheResponse.getEntity();
				if (entity != null) {
					String responseBody = new BufferedReader(new InputStreamReader(entity.getContent())).lines()
							.collect(Collectors.joining("\n"));
					response.setResponseBody(responseBody);
					EntityUtils.consume(entity);
				}
				return new HttpRecord(request, response);
			} catch (SocketTimeoutException e) {
				response.setCode(999); // Timeout
				response.setResponseBody("");
				return new HttpRecord(request, response);
			}
		}
	}

	protected static RSAPrivateKey generatePrivateKey(String key)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		return (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key)));
	}

	protected static X509Certificate generateCertificate(String cert) throws CertificateException {
		return (X509Certificate) CertificateFactory.getInstance("X.509")
				.generateCertificate(new ByteArrayInputStream(cert.getBytes()));
	}

}
