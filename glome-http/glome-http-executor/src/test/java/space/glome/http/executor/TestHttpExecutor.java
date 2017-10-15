package space.glome.http.executor;

import java.io.IOException;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import space.glome.http.schema.JsonMarshallingUtils;
import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequestItem;

public class TestHttpExecutor {

	
	@Test
	public void testGet() throws Exception {
		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-001.json");
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		Assert.assertEquals(200, record.getResponse().getCode().intValue()); 
	}

	@Test
	public void testPost() throws Exception {
		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-002.json");
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		Assert.assertEquals(200, record.getResponse().getCode().intValue()); 
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
