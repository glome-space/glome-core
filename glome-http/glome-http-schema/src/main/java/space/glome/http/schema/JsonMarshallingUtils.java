package space.glome.http.schema;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import space.glome.schema.domain.Argument;

public class JsonMarshallingUtils {

	public static String marshal(Object value) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
	}

	public static <E> E unmarshal(String content, Class<E> clazz) throws IOException {
		return new ObjectMapper().readValue(content, clazz);
	}

	public static <E> E unmarshal(String content, Class<E> clazz, List<Argument> arguments) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		if (arguments != null) {
			module.addDeserializer(String.class, new StringDeserializer(arguments));
			module.addDeserializer(Integer.class, new IntegerDeserializer(arguments));
		}
		mapper.registerModule(module);

		return mapper.readValue(content, clazz);
	}
}
