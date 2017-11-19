package space.glome.http.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

import space.glome.http.schema.JsonMarshallingUtils;
import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequestItem;

public class TestHttpExecutor {

	@Test
	public void testGet() throws Exception {
		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-001.json");
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
	}

	@Test
	public void testPost() throws Exception {
		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-002.json");
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue("Response doesn't contain \"data\":\"Request Body\", responseBody=" + record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"data\": \"Request Body\""));
	}

	private HttpRequestItem unmarshal(String requestFilePath) throws Exception {
		return JsonMarshallingUtils.unmarshal(readFile(requestFilePath), HttpRequestItem.class);
	}

	private String readFile(String filePath) throws IOException {
		try (Scanner scanner = new Scanner(TestHttpExecutor.class.getResourceAsStream(filePath), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
