package io.glome.http.schema.domain;

import io.glome.schema.domain.RequestItem;

public class HttpRequestItem extends RequestItem<HttpRequest> {

	protected HttpRequestItem() {
		super();
	}

	public HttpRequestItem(HttpRequest httpRequest) {
		super(httpRequest);
	}

}
