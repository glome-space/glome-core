package space.glome.schema.domain;

/**
 * 
 * Glome Processing Unit for matching and triggering test doubles.
 * 
 */
public class MappingItem<MATCHER extends Matcher, RESPONSE extends Response> extends Item {

	private MATCHER matcher;
	
	private RESPONSE response;
}
