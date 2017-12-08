package space.glome.http.schema;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import space.glome.schema.domain.Argument;

public class IntegerDeserializer extends StdDeserializer<Integer> {

	private static final long serialVersionUID = 1L;

	private List<Argument> arguments;

	public IntegerDeserializer(List<Argument> arguments) {
		super(Integer.class);
		this.arguments = arguments;
	}

	@Override
	public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String text = node.asText();
		for (Argument argument : arguments) {
			text = text.replace("${" + argument.getKey() + "}", argument.getValue());
		}
		return Integer.parseInt(text);
	}
}
