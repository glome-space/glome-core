package space.glome.schema.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public abstract class Request {

	private Set<Argument> arguments;

	@JsonInclude(Include.NON_NULL)
	public Set<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(Set<Argument> arguments) {
		this.arguments = arguments;
	}
}
