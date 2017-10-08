package io.glome.http.schema.domain;

import java.util.List;

import io.glome.schema.domain.Request;

public class HttpRequest extends Request {

	public enum Method {
		GET, PUT, POST, PATCH, DELETE, COPY, HEAD, OPTIONS, LINK, UNLINK, PURGE, LOCK, UNLOCK, PROPFIND, VIEW
	}

	private URL url;

	private Auth auth;

	private Proxy proxy;

	private Certificate certificate;

	private Method method;

	private Description description;

	private List<Header> headers;

	private Body body;

}
