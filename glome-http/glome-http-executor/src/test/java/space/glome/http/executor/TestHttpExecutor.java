package space.glome.http.executor;

import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

import space.glome.http.schema.JsonMarshallingUtils;
import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpRequest.Method;
import space.glome.http.schema.domain.HttpRequestItem;
import space.glome.http.schema.domain.HttpResponse;
import space.glome.http.schema.domain.RawURL;

public class TestHttpExecutor {

	
	@Test
	public void testGet() throws Exception {
		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-001.json");
		HttpResponse response = new HttpExecutor().exec(httpRequestItem.getRequest());
		System.out.println(response);
	}

	@Test
	public void testPost() throws Exception {
		HttpRequest httpRequest = new HttpRequest(new RawURL("http://httpbin.org/post"), Method.POST);
		new HttpExecutor().exec(httpRequest);
	}
	
	private HttpRequestItem unmarshal(String jsonPath) throws Exception {
		return JsonMarshallingUtils.unmarshal(readFile(jsonPath), HttpRequestItem.class);
	}
	
	protected String readFile(String filePath) throws IOException {
		try (Scanner scanner = new Scanner(TestHttpExecutor.class.getResourceAsStream(filePath), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
