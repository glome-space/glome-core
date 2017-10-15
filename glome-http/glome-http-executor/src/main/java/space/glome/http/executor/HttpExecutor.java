package space.glome.http.executor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import space.glome.http.schema.domain.CompositeURL;
import space.glome.http.schema.domain.Header;
import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpResponse;
import space.glome.http.schema.domain.RawRequestBody;
import space.glome.http.schema.domain.RawURL;
import space.glome.http.schema.domain.RequestBody;
import space.glome.http.schema.domain.URL;

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
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("username1", "vip"));
				nvps.add(new BasicNameValuePair("password2", "secret"));
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

	private static org.apache.http.Header[] convert(List<Header> from) {
		if (from == null) {
			return null;
		}
		org.apache.http.Header[] retVal = new org.apache.http.Header[from.size()];
		for (int i = 0; i < from.size(); i++) {
			retVal[i] = convert(from.get(i));
		}
		return retVal;
	}

	private static org.apache.http.Header convert(Header from) {
		if (from == null) {
			return null;
		}
		return new BasicHeader(from.getKey(), from.getValue());
	}

	private static List<Header> convert(org.apache.http.Header[] from) {
		if (from == null) {
			return null;
		}
		List<Header> retVal = new ArrayList<>();
		for (int i = 0; i < from.length; i++) {
			retVal.add(convert(from[i]));
		}
		return retVal;
	}

	private static Header convert(org.apache.http.Header from) {
		if (from == null) {
			return null;
		}
		return new Header(from.getName(), from.getValue());
	}

	private static String convert(URL from) {
		if (from == null) {
			return null;
		}
		try {
			if (from instanceof RawURL) {
				RawURL rawURL = (RawURL) from;
				return new URI(rawURL.getRaw()).toString();
			} else if (from instanceof CompositeURL) {
				CompositeURL compositeURL = (CompositeURL) from;
				return new URI(compositeURL.getScheme(), compositeURL.getUserInfo(), compositeURL.getHost(),
						compositeURL.getPort() == null ? 0 : compositeURL.getPort(), compositeURL.getPath(),
						compositeURL.getQuery(), compositeURL.getFragment()).toString();
			} else {
				throw new Error("Can't convert URL");
			}
		} catch (URISyntaxException e) {
			throw new Error("Can't convert URL", e);
		}
	}

	private static byte[] convert(RequestBody from) {
		if (from == null) {
			return new byte[0];
		}
		if (from instanceof RawRequestBody) {
			return ((RawRequestBody)from).getRaw().getBytes();
		} else {
			throw new Error("Can't convert RequestBody");
		}
	}
}
