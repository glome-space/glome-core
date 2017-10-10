package io.glome.http.schema.domain;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class RawURL extends URL {

	private String raw;

	public RawURL(String raw) {
		this.raw = raw;
	}

	@JsonInclude(Include.NON_NULL)
	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	@Override
	public String toString() {
		try {
			return new URI(raw).toString();
		} catch (URISyntaxException e) {
			throw new Error("Failed to assemble URL");
		}
	}
}
