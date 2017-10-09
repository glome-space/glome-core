package io.glome.schema.domain;

import java.util.List;

public abstract class Item {

	private List<Variable> variables;

	private Description description;

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

}
