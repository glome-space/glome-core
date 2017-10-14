package space.glome.http.executor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpResponse;

public class HttpExecutor {

	public HttpExecutor() {
	}

	public HttpResponse exec(HttpRequest request) throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpUriRequest httpUriRequest = null;
			switch (request.getMethod()) {
			case GET:
				httpUriRequest = new HttpGet(request.getUrl().toString());
				break;
			case POST:
				HttpPost httpPost = new HttpPost(request.getUrl().toString());
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("username1", "vip"));
				nvps.add(new BasicNameValuePair("password2", "secret"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				httpUriRequest = httpPost;
				break;
			default:
				throw new Error("Method " + request.getMethod() + " not supported");
			}
			try (CloseableHttpResponse response = httpclient.execute(httpUriRequest)) {
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
			}
		}
		return null;
	}

}
