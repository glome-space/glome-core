package space.glome.schema.domain;

public class ExecutionGroup<E extends Request> {

	private String name;

	private E request;

	private String targetConcurrency;

	// ramUpTime in seconds
	private String rampUpTime;

	private String rampUpStepCount;

	// holdTargetRateTime in seconds
	private String holdTargetRateTime;

	private String assertionResponseCode;

	public ExecutionGroup() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public E getRequest() {
		return request;
	}

	public void setRequest(E request) {
		this.request = request;
	}

	public String getAssertionResponseCode() {
		return assertionResponseCode;
	}

	public void setAssertionResponseCode(String assertionResponseCode) {
		this.assertionResponseCode = assertionResponseCode;
	}

	public String getTargetConcurrency() {
		return targetConcurrency;
	}

	public void setTargetConcurrency(String targetConcurrency) {
		this.targetConcurrency = targetConcurrency;
	}

	public String getRampUpTime() {
		return rampUpTime;
	}

	public void setRampUpTime(String rampUpTime) {
		this.rampUpTime = rampUpTime;
	}

	public String getRampUpStepCount() {
		return rampUpStepCount;
	}

	public void setRampUpStepCount(String rampUpStepCount) {
		this.rampUpStepCount = rampUpStepCount;
	}

	public String getHoldTargetRateTime() {
		return holdTargetRateTime;
	}

	public void setHoldTargetRateTime(String holdTargetRateTime) {
		this.holdTargetRateTime = holdTargetRateTime;
	}

}
