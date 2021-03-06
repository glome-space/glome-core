package space.glome.http.schema.domain;

import java.net.URISyntaxException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = RawURL.class, name = "raw-url"),
		@JsonSubTypes.Type(value = CompositeURL.class, name = "composite-url") })
public interface URL {

	String getScheme();

	String getUserInfo();

	String getHost();

	String getPath();

	Integer getPort() ;

	String getQuery();

	String getFragment();

	String getRaw() throws URISyntaxException;


}
