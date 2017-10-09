package io.glome.http.schema.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class URL {

	private String raw;
	
	private String protocol;
	
	private String host;
	
	private String path;
	
	private Integer port;
	
	private Query query;
	
	protected URL() {
	}
	
	public URL(String raw) {
		this.raw = raw;
	}

	@JsonInclude(Include.NON_NULL)
	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	@JsonInclude(Include.NON_NULL)
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@JsonInclude(Include.NON_NULL)
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@JsonInclude(Include.NON_NULL)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@JsonInclude(Include.NON_NULL)
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@JsonInclude(Include.NON_NULL)
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
}
