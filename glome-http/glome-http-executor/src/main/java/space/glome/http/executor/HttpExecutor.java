package space.glome.http.executor;

import static space.glome.http.executor.ApacheHttpConverters.convert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpResponse;

public class HttpExecutor {

	public HttpExecutor() {
	}

	public HttpRecord exec(HttpRequest request) throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
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
			default:
				throw new Error("Method " + request.getMethod() + " not supported");
			}
			httpUriRequest.setHeaders(convert(request.getHeaders()));

			try (CloseableHttpResponse httpResponse = httpclient.execute(httpUriRequest)) {
				HttpEntity entity = httpResponse.getEntity();
				HttpResponse response = new HttpResponse();
				response.setResponseBody(convertStreamToString(entity.getContent()));
				response.setStatus(httpResponse.getStatusLine().toString());
				response.setCode(httpResponse.getStatusLine().getStatusCode());
				response.setHeaders(convert(httpResponse.getAllHeaders()));
				EntityUtils.consume(entity);
				return new HttpRecord(request, response);
			}
		}

	}

	private static String convertStreamToString(java.io.InputStream is) {
		return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
	}


}
