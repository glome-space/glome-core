package space.glome.http.schema.domain;

public class RawRequestBody extends RequestBody {

	private String raw;
	
	public RawRequestBody() {
	}

	public RawRequestBody(String raw) {
		this.raw = raw;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

}
