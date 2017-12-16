package space.glome.schema.domain;

import java.util.HashSet;
import java.util.Set;

public class ExecutionPlan {

	private String name;

	private Set<ExecutionGroup> groups;

	public ExecutionPlan(String name) {
		this.name = name;
		groups = new HashSet<>();
	}

	public void addExecutionGroup(ExecutionGroup group) {
		groups.add(group);
	}
	
	public Set<ExecutionGroup> getExecutionGroup() {
		return groups;
	}

	public String getName() {
		return name;
	}

}
