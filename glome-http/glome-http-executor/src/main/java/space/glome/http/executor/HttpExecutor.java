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
import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.assertions.gui.AssertionGui;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;

import com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup;

import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpResponse;
import space.glome.http.schema.domain.JksCertificate;
import space.glome.http.schema.domain.PemCertificate;
import space.glome.schema.domain.ExecutionGroup;
import space.glome.schema.domain.ExecutionPlan;

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

		Integer timeout = request.getTimeout();
		timeout = timeout == null ? 2000 : timeout;

		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).build();

		try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLContext(sslContext).build()) {
			HttpUriRequest httpUriRequest = null;
			switch (request.getMethod()) {
			case GET:
				httpUriRequest = new HttpGet(request.getUrl().getRaw());
				break;
			case POST:
				HttpPost httpPost = new HttpPost(request.getUrl().getRaw());
				httpPost.setEntity(new ByteArrayEntity(request.getPayload()));
				httpUriRequest = httpPost;
				break;
			case PUT:
				HttpPut httpPut = new HttpPut(request.getUrl().getRaw());
				httpPut.setEntity(new ByteArrayEntity(request.getPayload()));
				httpUriRequest = httpPut;
				break;
			case DELETE:
				httpUriRequest = new HttpDelete(request.getUrl().getRaw());;
				break;
			default:
				throw new Error("Method " + request.getMethod() + " not supported");
			}
			httpUriRequest.setHeaders(convert(request.getHeaders()));

			HttpResponse response = new HttpResponse();
			long start = System.nanoTime();
			try (CloseableHttpResponse apacheResponse = httpclient.execute(httpUriRequest)) {
				long elapsedTime = System.nanoTime() - start;
				response.setElapsedTime(elapsedTime);
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

	public JMeterSummariser exec(ExecutionPlan<HttpRequest> execPlan) throws Exception {

		String jmeterHome = System.getProperty("glome.jmeter.home");
		if (jmeterHome == null) {
			throw new RuntimeException("glome.jmeter.home property is not set");
		}

		StandardJMeterEngine jmeter = new StandardJMeterEngine();
		JMeterUtils.setJMeterHome(jmeterHome);
		JMeterUtils.loadJMeterProperties(jmeterHome + "/jmeter.properties");
		JMeterUtils.initLogging();
		SaveService.loadProperties();

		ListedHashTree rootTree = new ListedHashTree();

		TestPlan testPlan = new TestPlan(execPlan.getName());
		HashTree testPlanTree = rootTree.add(testPlan);

		for (ExecutionGroup<HttpRequest> group : execPlan.getExecutionGroup()) {

			ConcurrencyThreadGroup threadGroup = new ConcurrencyThreadGroup();
			threadGroup.setName(group.getName());
			threadGroup.setTargetLevel(String.valueOf(group.getTargetConcurrency()));
			threadGroup.setRampUp(String.valueOf(group.getRampUpTime()));
			threadGroup.setSteps(String.valueOf(group.getRampUpStepCount()));
			threadGroup.setHold(String.valueOf(group.getHoldTargetRateTime()));
			threadGroup.setUnit("S");
			HashTree threadGroupTree = testPlanTree.add(threadGroup);

			TransactionController transactionController = new TransactionController();
			transactionController.setIncludeTimers(false);
			transactionController.setParent(true);
			HashTree transactionControllerTree = threadGroupTree.add(transactionController);

			HttpRequest httpRequest = group.getRequest();

			HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
			httpSampler.setDomain(httpRequest.getUrl().getHost());
			httpSampler.setPort(httpRequest.getUrl().getPort());
			httpSampler.setPath(httpRequest.getUrl().getPath() + "?" + httpRequest.getUrl().getQuery());
			httpSampler.setProtocol(httpRequest.getUrl().getScheme());
			httpSampler.setMethod(httpRequest.getMethod().name());
			if (httpSampler.getMethod().equals("POST")) {
				httpSampler.setPostBodyRaw(true);
				httpSampler.addNonEncodedArgument("", new String(httpRequest.getPayload()), "=");
			}
			HashTree httpSamplerTree = transactionControllerTree.add(httpSampler);

			ResponseAssertion responseAssertion = new ResponseAssertion();
			responseAssertion.setProperty(TestElement.TEST_CLASS, ResponseAssertion.class.getName());
			responseAssertion.setProperty(TestElement.GUI_CLASS, AssertionGui.class.getName());
			responseAssertion.setAssumeSuccess(false);
			responseAssertion.setTestFieldResponseCode();
			responseAssertion.setScopeAll();
			responseAssertion.setToEqualsType();
			responseAssertion.addTestString(group.getAssertionResponseCode());
			httpSamplerTree.add(responseAssertion);

			HeaderManager headerManager = new HeaderManager();
			headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
			headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
			for (space.glome.http.schema.domain.Header header : httpRequest.getHeaders()) {
				headerManager.add(new Header(header.getKey(), header.getValue()));
			}
			threadGroupTree.add(headerManager);
		}

		JMeterSummariser summariser = new JMeterSummariser();
		ResultCollector logger = new ResultCollector(summariser);
		testPlanTree.add(logger);

		jmeter.configure(rootTree);
		jmeter.run();

		return summariser;
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
