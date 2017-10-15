package space.glome.http.schema;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonMarshallingUtils {

	public static String marshal(Object value) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
	}

	public static <E> E unmarshal(String content, Class<E> clazz) throws IOException {
		return  new ObjectMapper().readValue(content, clazz);
	}
	
	public static <E> E unmarshal(String content, Class<E> clazz, Map<String, String> params) throws IOException {
		ObjectMapper mapper=  new ObjectMapper();
		
		SimpleModule module = new SimpleModule();
		module.addDeserializer(String.class, new StringDeserializer(params));
		module.addDeserializer(Integer.class, new IntegerDeserializer(params));
		mapper.registerModule(module);
		
		return mapper.readValue(content, clazz);
	}
}
