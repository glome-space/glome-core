package space.glome.http.schema;

import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonValidationUtils {

	public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
	public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

	public static void validateRequest(String json) throws IOException, ProcessingException {
		validate(json, "/schema/request-schema.json");
	}
	
	public static void validateRequestItem(String json) throws IOException, ProcessingException {
		validate(json, "/schema/request-item-schema.json");
	}
	
	public static void validateResponse(String json) throws IOException, ProcessingException {
		validate(json, "/schema/response-schema.json");
	}
	
	public static void validateMappingItem(String json) throws IOException, ProcessingException {
		validate(json, "/schema/mapping-item-schema.json");
	}
	
	private static void validate(String json, String schemaPath) throws IOException, ProcessingException {
		try (Scanner scanner = new Scanner(JsonMarshallingUtils.class.getResourceAsStream(schemaPath), "UTF-8")) {
			String schema = scanner.useDelimiter("\\A").next();
			JsonValidationUtils.validateJson(schema, json);
		}
	}
	
	private static JsonNode getJsonNode(String jsonText) throws IOException {
		return JsonLoader.fromString(jsonText);
	}

	private static JsonSchema getSchemaNode(String schemaText) throws IOException, ProcessingException {
		final JsonNode schemaNode = getJsonNode(schemaText);
		return getSchemaNode(schemaNode);
	}

	private static void validateJson(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException {
		ProcessingReport report = jsonSchemaNode.validate(jsonNode);
		if (!report.isSuccess()) {
			for (ProcessingMessage processingMessage : report) {
				throw new ProcessingException(processingMessage);
			}
		}
	}

	private static boolean isJsonValid(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException {
		ProcessingReport report = jsonSchemaNode.validate(jsonNode);
		return report.isSuccess();
	}

	private static void validateJson(String schemaText, String jsonText) throws IOException, ProcessingException {
		final JsonSchema schemaNode = getSchemaNode(schemaText);
		final JsonNode jsonNode = getJsonNode(jsonText);
		validateJson(schemaNode, jsonNode);
	}

	private static JsonSchema getSchemaNode(JsonNode jsonNode) throws ProcessingException {
		final JsonNode schemaIdentifier = jsonNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
		if (null == schemaIdentifier) {
			((ObjectNode) jsonNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
		}

		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		return factory.getJsonSchema(jsonNode);
	}
}