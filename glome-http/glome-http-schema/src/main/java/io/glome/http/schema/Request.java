package io.glome.http.schema;

import java.util.List;

import io.glome.schema.Variable;

public class Request {

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

	private List<Variable> variables;

}
