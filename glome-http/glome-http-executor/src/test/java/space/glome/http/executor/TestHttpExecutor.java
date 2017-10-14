package space.glome.http.executor;

import org.junit.Test;

import space.glome.http.executor.HttpExecutor;
import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.RawURL;
import space.glome.http.schema.domain.HttpRequest.Method;

public class TestHttpExecutor {

	
	@Test
	public void testGet() throws Exception {
		HttpRequest httpRequest = new HttpRequest(new RawURL("http://httpbin.org/get"), Method.GET);
		new HttpExecutor().exec(httpRequest);
	}

	@Test
	public void testPost() throws Exception {
		HttpRequest httpRequest = new HttpRequest(new RawURL("http://httpbin.org/post"), Method.POST);
		new HttpExecutor().exec(httpRequest);
	}
	
	
}
