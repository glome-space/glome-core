package space.glome.schema.domain;

public class ExecutionPlan {

	private String name;
	
	private int numThreads;

	private int rampUp;

	public ExecutionPlan(String name, int numThreads, int rampUp) {
		this.name = name;
		this.numThreads = numThreads;
		this.rampUp = rampUp;
	}

	public int getNumThreads() {
		return numThreads;
	}

	public int getRampUp() {
		return rampUp;
	}

	public String getName() {
		return name;
	}

}
