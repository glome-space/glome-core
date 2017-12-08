package space.glome.http.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.junit.Test;

import space.glome.http.schema.JsonMarshallingUtils;
import space.glome.http.schema.domain.HttpRecord;
import space.glome.http.schema.domain.HttpRequestItem;
import space.glome.schema.domain.Argument;

public class TestHttpExecutor {

	@Test
	public void testGet_take1() throws Exception {
		Set<Argument> arguments = new HashSet<>();
		arguments.add(new Argument("URL", "http://httpbin.org/get"));

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-001.json", arguments);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue(
				"Response doesn't contain \"Host\":\"httpbin.org\", responseBody="
						+ record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"Host\": \"httpbin.org\""));
	}

	@Test
	public void testGet_take2() throws Exception {
		Set<Argument> arguments = new HashSet<>();
		arguments.add(new Argument("URL", "https://httpbin.org/anything"));

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-001.json", arguments);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue(
				"Response doesn't contain \"method\":\"GET\", responseBody=" + record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"method\": \"GET\""));
	}

	@Test
	public void testPost_take1() throws Exception {
		Set<Argument> arguments = new HashSet<>();
		arguments.add(new Argument("SCHEMA", "http"));
		arguments.add(new Argument("PORT_NUMBER", "80"));
		arguments.add(new Argument("BODY_CONTENT", "Request Body"));

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-002.json", arguments);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue(
				"Response doesn't contain \"data\":\"Request Body\", responseBody="
						+ record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"data\": \"Request Body\""));
	}

	@Test
	public void testPost_take2() throws Exception {
		Set<Argument> arguments = new HashSet<>();
		arguments.add(new Argument("SCHEMA", "https"));
		arguments.add(new Argument("PORT_NUMBER", "443"));
		arguments.add(new Argument("BODY_CONTENT", "Request Body Number Two"));

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-002.json", arguments);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue(
				"Response doesn't contain \"data\":\"Request Body Number Two\", responseBody="
						+ record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"data\": \"Request Body Number Two\""));
	}

	@Test
	public void testPost_external_body_file() throws Exception {
		Set<Argument> arguments = new HashSet<>();
		arguments.add(new Argument("URL", "https://httpbin.org/anything"));
		arguments.add(new Argument("BODY_FILE_NAME", "body-001.xml"));

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-003.json", arguments);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue(
				"Response doesn't contain \"data\":\"<body>BodyFromFile1</body>\", responseBody="
						+ record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"data\": \"<body>BodyFromFile1</body>\""));
	}

	@Test
	public void testPost_external_body_file_with_template() throws Exception {
		Set<Argument> arguments = new HashSet<>();
		arguments.add(new Argument("URL", "https://httpbin.org/anything"));
		arguments.add(new Argument("BODY_FILE_NAME", "body-002.xml"));
		arguments.add(new Argument("BODY_TEXT", "BodyFromFile2"));

		HttpRequestItem httpRequestItem = unmarshal("/samples/request-item-003.json", arguments);
		HttpRecord record = new HttpExecutor().exec(httpRequestItem.getRequest());
		assertEquals(200, record.getResponse().getCode().intValue());
		assertTrue(
				"Response doesn't contain \"data\":\"<body>BodyFromFile2</body>\", responseBody="
						+ record.getResponse().getResponseBody(),
				record.getResponse().getResponseBody().contains("\"data\": \"<body>BodyFromFile2</body>\""));
	}

	private HttpRequestItem unmarshal(String requestFilePath) throws Exception {
		return unmarshal(requestFilePath, null);
	}

	private HttpRequestItem unmarshal(String requestFilePath, Set<Argument> arguments) throws Exception {
		HttpRequestItem requestItem = JsonMarshallingUtils.unmarshal(readFile(requestFilePath), HttpRequestItem.class,
				arguments);
		Set<Argument> finalArguments = new HashSet<>(arguments);

		Set<Argument> defaultArguments = requestItem.getArguments();
		if (defaultArguments != null) {
			for (Argument argument : defaultArguments) {
				if (argument.getValue() != null) {
					finalArguments.add(argument);
				}
			}
		}
		requestItem.getRequest().setArguments(arguments);
		return requestItem;
	}

	private String readFile(String filePath) throws IOException {
		try (Scanner scanner = new Scanner(TestHttpExecutor.class.getResourceAsStream(filePath), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
