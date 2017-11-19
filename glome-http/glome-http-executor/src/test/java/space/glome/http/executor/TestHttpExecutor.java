package space.glome.http.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;

import space.glome.http.schema.JsonMarshallingUtils;
import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequestItem;

public class TestHttpExecutor {

	@Test
	public void testGet_take1() throws Exception {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("URL", "http://httpbin.org/get");
		
		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-001.json", parameters);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue("Response doesn't contain \"Host\":\"httpbin.org\", responseBody=" + record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"Host\": \"httpbin.org\""));
	}
	
	@Test
	public void testGet_take2() throws Exception {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("URL", "https://httpbin.org/anything");
		
		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-001.json", parameters);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue("Response doesn't contain \"method\":\"GET\", responseBody=" + record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"method\": \"GET\""));
	}

	@Test
	public void testPost_take1() throws Exception {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("SCHEMA", "http");
		parameters.put("PORT_NUMBER", "80");
		parameters.put("BODY_CONTENT", "Request Body");

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-002.json", parameters);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue("Response doesn't contain \"data\":\"Request Body\", responseBody=" + record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"data\": \"Request Body\""));
	}

	@Test
	public void testPost_take2() throws Exception {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("SCHEMA", "https");
		parameters.put("PORT_NUMBER", "443");
		parameters.put("BODY_CONTENT", "Request Body Number Two");

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-002.json", parameters);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue("Response doesn't contain \"data\":\"Request Body Number Two\", responseBody=" + record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"data\": \"Request Body Number Two\""));
	}
	
	private HttpRequestItem unmarshal(String requestFilePath) throws Exception {
		return JsonMarshallingUtils.unmarshal(readFile(requestFilePath), HttpRequestItem.class);
	}

	private HttpRequestItem unmarshal(String requestFilePath, Map<String, String> parameters) throws Exception {
		return JsonMarshallingUtils.unmarshal(readFile(requestFilePath), HttpRequestItem.class, parameters);
	}
	
	private String readFile(String filePath) throws IOException {
		try (Scanner scanner = new Scanner(TestHttpExecutor.class.getResourceAsStream(filePath), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
