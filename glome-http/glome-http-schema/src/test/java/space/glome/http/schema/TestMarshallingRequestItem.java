package space.glome.http.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import space.glome.http.schema.domain.HttpRequestItem;

public class TestMarshallingRequestItem extends TestMarshallingBase {

	@Test
	public void testHttpRequestItemMarshalling_001() throws Exception {
		String input = readFile("/samples/request-item-001.json");
		HttpRequestItem httpRequestItem = JsonMarshallingUtils.unmarshal(input, HttpRequestItem.class);
		String output = JsonMarshallingUtils.marshal(httpRequestItem);
		assertEquals(input, output);
		try {
			JsonValidationUtils.validateRequestItem(output);
		} catch (ProcessingException e) {
			assertTrue("ProcessingException thrown on validation", true);
		}
	}

}
