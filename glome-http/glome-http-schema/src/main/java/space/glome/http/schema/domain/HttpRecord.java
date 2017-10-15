package space.glome.http.schema.domain;

import space.glome.schema.domain.Record;

public class HttpRecord extends Record<HttpRequest, HttpResponse>{

	public HttpRecord() {
	}
	
	public HttpRecord(HttpRequest request, HttpResponse response) {
		super(request, response);
	}
}
