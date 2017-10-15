package space.glome.http.schema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = RawRequestBody.class, name = "raw-body"),
		@JsonSubTypes.Type(value = FileRequestBody.class, name = "file-body"),
		@JsonSubTypes.Type(value = FormDataRequestBody.class, name = "form-data-body"),
		@JsonSubTypes.Type(value = UrlEncodedRequestBody.class, name = "url-encoded-body") })
public abstract class RequestBody {

}
