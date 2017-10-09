package io.glome.http.schema.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.glome.schema.domain.Request;

public class HttpRequest extends Request {

	public enum Method {
		GET, PUT, POST, PATCH, DELETE, COPY, HEAD, OPTIONS, LINK, UNLINK, PURGE, LOCK, UNLOCK, PROPFIND, VIEW
	}

	protected HttpRequest() {
	}

	public HttpRequest(URL url) {
		this.url = url;
	}

	private URL url;

	private Method method;

	private List<Header> headers;

	private Body body;

	private Proxy proxy;

	private Certificate certificate;
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@JsonInclude(Include.NON_NULL)
	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	@JsonInclude(Include.NON_NULL)
	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

}
