package space.glome.schema.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Item is a basic Glome Processing Unit. Examples of such units are HTTP Request Item or HTTP Mapping Item
 */
public abstract class Item {

	private Set<Argument> arguments;

	private Description description;

	@JsonInclude(Include.NON_NULL)
	public Set<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(Set<Argument> arguments) {
		this.arguments = arguments;
	}

	@JsonInclude(Include.NON_NULL)
	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

}
