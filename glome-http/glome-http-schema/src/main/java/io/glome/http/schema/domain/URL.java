package io.glome.http.schema.domain;

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

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
}
