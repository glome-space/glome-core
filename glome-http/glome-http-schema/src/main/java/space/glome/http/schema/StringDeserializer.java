package space.glome.http.schema;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import space.glome.schema.domain.Argument;

public class StringDeserializer extends StdDeserializer<String> {

	private static final long serialVersionUID = 1L;

	private Set<Argument> arguments;

	public StringDeserializer(Set<Argument> arguments) {
		super(String.class);
		this.arguments = arguments;
	}

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String text = node.asText();
		if (arguments != null) {
			for (Argument argument : arguments) {
				text = text.replace("${" + argument.getKey() + "}", argument.getValue());
			}
		}
		return text;
	}
}
