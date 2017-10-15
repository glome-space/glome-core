package space.glome.http.schema;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class IntegerDeserializer extends StdDeserializer<Integer> {

	private static final long serialVersionUID = 1L;

	private Map<String, String> params;

	public IntegerDeserializer(Map<String, String> params) {
		super(Integer.class);
		this.params = params;
	}

	@Override
	public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String text = node.asText();
		for (Entry<String, String> entry : params.entrySet()) {
			text = text.replace("${" + entry.getKey() + "}", entry.getValue());
		}
		return Integer.parseInt(text);
	}
}
