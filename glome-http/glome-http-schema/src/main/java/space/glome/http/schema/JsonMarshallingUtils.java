package space.glome.http.schema;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import space.glome.http.schema.domain.HttpRequestItem;
import space.glome.schema.domain.Argument;

public class JsonMarshallingUtils {

	public static String marshal(Object value) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
	}

	public static HttpRequestItem unmarshalHttpRequestItem(String requestFilePath) throws Exception {
		return unmarshalHttpRequestItem(requestFilePath, null);
	}
	
	public static HttpRequestItem unmarshalHttpRequestItem(String requestFilePath, Set<Argument> arguments) throws Exception {
		HttpRequestItem requestItem = JsonMarshallingUtils.unmarshal(readFile(requestFilePath), HttpRequestItem.class,
				arguments);
		Set<Argument> finalArguments = new HashSet<>();
		if(arguments !=null) {
			finalArguments.addAll(arguments);
		}

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


	private static String readFile(String filePath) throws IOException {
		try (Scanner scanner = new Scanner(JsonMarshallingUtils.class.getResourceAsStream(filePath), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
	
	private static <E> E unmarshal(String content, Class<E> clazz, Set<Argument> arguments) throws IOException {
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
