package space.glome.http.schema.domain;

import java.util.List;

import space.glome.schema.domain.Matcher;

public class HttpRequestMatcher implements Matcher {

	private UrlMatcher urlMatcher;

	private HttpRequest.Method method;

	private List<QueryMatcher> queryMatchers;

	private List<HeaderMatcher> headerMatchers;

	private List<RequestBodyMatcher> requestBodyMatchers;

	public boolean matches(Object actual) {
		return false;
	}

	public String getMismatchDescription() {
		return null;
	}

}
