package space.glome.http.schema.domain;

import java.util.List;

import space.glome.schema.domain.Response;

public class HttpResponse extends Response {

	private List<Header> headers;

	private String responseBody;

	private String status;

	private Integer code;

	public HttpResponse() {
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
