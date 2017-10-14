package space.glome.schema.domain;

public class MappingItem<MATCHER extends Matcher, RESPONSE extends Response> extends Item {

	private MATCHER matcher;
	
	private RESPONSE response;
}
