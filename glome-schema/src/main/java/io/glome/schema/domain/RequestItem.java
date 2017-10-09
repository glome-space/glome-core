package io.glome.schema.domain;

public class RequestItem<REQUEST extends Request> extends Item {

	private REQUEST request;
	
	protected RequestItem() {
	}

	public RequestItem(REQUEST request) {
		this.request = request;
	}
	
	public REQUEST getRequest() {
		return request;
	}

	public void setRequest(REQUEST request) {
		this.request = request;
	}
	
}
