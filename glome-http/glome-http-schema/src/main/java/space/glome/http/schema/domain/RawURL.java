package space.glome.http.schema.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class RawURL extends URL {

	private String raw;

	public RawURL() {
	}
	
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
		return "RawURL [raw=" + raw + "]";
	}


}
