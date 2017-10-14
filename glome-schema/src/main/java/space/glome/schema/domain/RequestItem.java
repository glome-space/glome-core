package space.glome.schema.domain;

/**
 * Glome Processing Unit for triggering the request.
 */
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
