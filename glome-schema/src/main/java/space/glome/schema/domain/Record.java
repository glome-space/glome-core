package space.glome.schema.domain;

public class Record<REQUEST extends Request, RESPONSE extends Response> {

	private REQUEST request;

	private RESPONSE response;

	public Record() {
	}

	public Record(REQUEST request, RESPONSE response) {
		this.request = request;
		this.response = response;
	}

	public REQUEST getRequest() {
		return request;
	}

	public void setRequest(REQUEST request) {
		this.request = request;
	}

	public RESPONSE getResponse() {
		return response;
	}

	public void setResponse(RESPONSE response) {
		this.response = response;
	}

}
