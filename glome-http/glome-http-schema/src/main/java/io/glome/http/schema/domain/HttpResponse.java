package io.glome.http.schema.domain;

import java.util.List;

import io.glome.schema.domain.Response;

public class HttpResponse extends Response {

	private List<Header> headers;
	
	private ResponseBody responseBody;
	
	private String status;
	
	private Integer code;
}
