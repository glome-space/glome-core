package space.glome.http.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import space.glome.http.schema.domain.HttpRequestItem;
import space.glome.schema.domain.Argument;

public class TestMarshallingRequestItem extends TestMarshallingBase {

	@Test
	public void testHttpRequestItemMarshalling_001() throws Exception {
		testHttpRequestItemMarshalling("/samples/request-item-001.json");
	}

	@Test
	public void testHttpRequestItemMarshalling_002() throws Exception {
		testHttpRequestItemMarshalling("/samples/request-item-002.json");
	}

	@Test
	public void testHttpRequestItemMarshalling_003() throws Exception {
		Set<Argument> arguments = new HashSet<>();
		arguments.add(new Argument("PORT_NUMBER", "8080"));
		arguments.add(new Argument("BODY_CONTENT", "<p>Content</p>"));
		testHttpRequestItemMarshallingWithSubstitution("/samples/request-item-003.json", arguments,
				"/samples/request-item-ref-003.json");
	}

	private void testHttpRequestItemMarshalling(String jsonPath) throws Exception {
		String input = readFile(jsonPath);
		HttpRequestItem httpRequestItem = JsonMarshallingUtils.unmarshal(input, HttpRequestItem.class);
		String output = JsonMarshallingUtils.marshal(httpRequestItem);
		assertEquals(input, output);
		try {
			JsonValidationUtils.validateRequestItem(output);
		} catch (ProcessingException e) {
			assertTrue("ProcessingException thrown on validation", true);
		}
	}

	private void testHttpRequestItemMarshallingWithSubstitution(String templateJsonPath, Set<Argument> arguments,
			String refJsonPath) throws Exception {
		String input = readFile(templateJsonPath);
		String ref = readFile(refJsonPath);
		HttpRequestItem httpRequestItem = JsonMarshallingUtils.unmarshal(input, HttpRequestItem.class, arguments);
		String output = JsonMarshallingUtils.marshal(httpRequestItem);
		assertEquals(ref, output);
		try {
			JsonValidationUtils.validateRequestItem(output);
		} catch (ProcessingException e) {
			assertTrue("ProcessingException thrown on validation", true);
		}
	}
}
