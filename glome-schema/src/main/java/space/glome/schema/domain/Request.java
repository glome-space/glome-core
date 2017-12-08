package space.glome.schema.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Request {

	private Set<Argument> arguments;

	@JsonIgnore
	public Set<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(Set<Argument> arguments) {
		this.arguments = arguments;
	}
}
