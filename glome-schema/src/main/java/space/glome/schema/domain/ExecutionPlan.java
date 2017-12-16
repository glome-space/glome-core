package space.glome.schema.domain;

import java.util.HashSet;
import java.util.Set;

public class ExecutionPlan<E extends Request> {

	private String name;

	private Set<ExecutionGroup<E>> groups;

	public ExecutionPlan(String name) {
		this.name = name;
		groups = new HashSet<>();
	}

	public void addExecutionGroup(ExecutionGroup<E> group) {
		groups.add(group);
	}
	
	public Set<ExecutionGroup<E>> getExecutionGroup() {
		return groups;
	}

	public String getName() {
		return name;
	}

}
