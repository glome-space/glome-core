package space.glome.http.schema.domain;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CompositeURL implements URL {

	private String scheme;

	private String userInfo;

	private String host;

	private Integer port;

	private String path;

	private String query;

	private String fragment;

	public CompositeURL() {
	}

	public CompositeURL(String scheme, String host, Integer port, String path, String query) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
		this.query = query;
	}

	@JsonInclude(Include.NON_NULL)
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@JsonInclude(Include.NON_NULL)
	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
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
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@JsonInclude(Include.NON_NULL)
	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	@Override
	@JsonIgnore
	public String getRaw() throws URISyntaxException {
		return new URI(scheme, userInfo, host, port == null ? 0 : port, path, query, fragment).toString();

	}

}
