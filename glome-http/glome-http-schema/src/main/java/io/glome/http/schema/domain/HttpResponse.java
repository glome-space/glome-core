package io.glome.http.schema.domain;

import java.util.List;

import io.glome.schema.domain.Response;

public class HttpResponse extends Response {

	private List<Header> headers;
	
	private Body body;
	
	private String status;
	
	private Integer code;
}
