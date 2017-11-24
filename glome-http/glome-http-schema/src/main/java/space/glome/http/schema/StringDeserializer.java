package space.glome.http.schema;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class StringDeserializer extends StdDeserializer<String> {

	private static final long serialVersionUID = 1L;

	private Map<String, String> params;

	public StringDeserializer(Map<String, String> params) {
		super(String.class);
		this.params = params;
	}

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String text = node.asText();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				text = text.replace("${" + entry.getKey() + "}", entry.getValue());
			}
		}
		return text;
	}
}
