package space.glome.schema.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public abstract class Item {

	private List<Variable> variables;

	private Description description;

	@JsonInclude(Include.NON_NULL)
	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	@JsonInclude(Include.NON_NULL)
	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

}
