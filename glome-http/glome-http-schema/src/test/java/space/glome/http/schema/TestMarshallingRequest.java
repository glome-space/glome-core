package space.glome.http.schema;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import space.glome.http.schema.domain.HttpRequest;
import space.glome.http.schema.domain.HttpRequest.Method;
import space.glome.http.schema.domain.RawURL;

public class TestMarshallingRequest extends TestMarshallingBase {

	@Test
	public void testHttpRequestJsonValidation() throws Exception {
		HttpRequest httpRequest = new HttpRequest(new RawURL("http://localhost:8080/health"), Method.GET);
		String json = JsonMarshallingUtils.marshal(httpRequest);
		try {
			JsonValidationUtils.validateRequestItem(json);
		} catch (ProcessingException e) {
			assertTrue("ProcessingException thrown on validation", true);
		}
	}


}
