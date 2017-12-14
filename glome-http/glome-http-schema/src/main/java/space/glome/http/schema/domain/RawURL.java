package space.glome.http.schema.domain;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class RawURL implements URL {

	private String raw;

	private URI uri;

	public RawURL() {
	}

	public RawURL(String raw) throws URISyntaxException {
		setRaw(raw);
	}

	@JsonInclude(Include.NON_NULL)
	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) throws URISyntaxException {
		this.raw = raw;
		uri = new URI(raw);
	}

	@Override
	@JsonIgnore
	public String getScheme() {
		return uri.getScheme();
	}

	@Override
	@JsonIgnore
	public String getUserInfo() {
		return uri.getUserInfo();
	}

	@Override
	@JsonIgnore
	public String getHost() {
		return uri.getHost();
	}

	@Override
	@JsonIgnore
	public String getPath() {
		return uri.getPath();
	}

	@Override
	@JsonIgnore
	public Integer getPort() {
		return uri.getPort();
	}

	@Override
	@JsonIgnore
	public String getQuery() {
		return uri.getQuery();
	}

	@Override
	@JsonIgnore
	public String getFragment() {
		return uri.getFragment();
	}

}
