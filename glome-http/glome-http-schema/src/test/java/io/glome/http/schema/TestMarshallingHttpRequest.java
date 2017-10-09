package io.glome.http.schema;

import java.util.Scanner;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.glome.http.schema.domain.HttpRequest;
import io.glome.http.schema.domain.HttpRequest.Method;
import io.glome.http.schema.domain.HttpRequestItem;
import io.glome.http.schema.domain.URL;

public class TestMarshallingHttpRequest {

	@Test
	public void testGeneratingHttpRequestJson() throws Exception {
		HttpRequest httpRequest = new HttpRequest(new URL("http://localhost:8080/health"), Method.GET);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<HttpRequest>> violations = validator.validate(httpRequest);

		Assert.assertEquals("Violations:" + violations.toString(), 0, violations.size());

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		

		String out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(httpRequest);
		System.out.println(out);

		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/schema/request-schema.json"), "UTF-8")) {
			String schema = scanner.useDelimiter("\\A").next();
			JsonValidationUtils.validateJson(schema, out);
		}

	}
	
	@Test
	public void testGeneratingHttpRequestItemJson() throws Exception {
		HttpRequestItem httpRequestItem = new HttpRequestItem(new HttpRequest(new URL("http://localhost:8080/health"), Method.GET));

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<HttpRequestItem>> violations = validator.validate(httpRequestItem);

		Assert.assertEquals("Violations:" + violations.toString(), 0, violations.size());

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		

		String out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(httpRequestItem);
		System.out.println(out);

		try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/schema/request-item-schema.json"), "UTF-8")) {
			String schema = scanner.useDelimiter("\\A").next();
			JsonValidationUtils.validateJson(schema, out);
		}

	}

}
