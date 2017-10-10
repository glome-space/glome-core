package io.glome.http.schema;

import java.util.Scanner;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.glome.http.schema.domain.HttpRequest;
import io.glome.http.schema.domain.HttpRequest.Method;
import io.glome.http.schema.domain.HttpRequestItem;
import io.glome.http.schema.domain.RawURL;

public class TestMarshallingRequest {

	@Test
	public void testGeneratingHttpRequestJson() throws Exception {
		HttpRequest httpRequest = new HttpRequest(new RawURL("http://localhost:8080/health"), Method.GET);

		String out = writeValueAsString(httpRequest);
		System.out.println(out);

		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/schema/request-schema.json"), "UTF-8")) {
			String schema = scanner.useDelimiter("\\A").next();
			JsonValidationUtils.validateJson(schema, out);
		}

	}

	@Test
	public void testGeneratingHttpRequestItemJson() throws Exception {
		HttpRequestItem httpRequestItem = new HttpRequestItem(
				new HttpRequest(new RawURL("http://localhost:8080/health"), Method.GET));

		String out = writeValueAsString(httpRequestItem);
		System.out.println(out);

		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/schema/request-item-schema.json"),
				"UTF-8")) {
			String schema = scanner.useDelimiter("\\A").next();
			JsonValidationUtils.validateJson(schema, out);
		}

	}

	private static String writeValueAsString(Object value) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);

	}
}
