package space.glome.http.executor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicHeader;

import space.glome.http.schema.domain.CompositeURL;
import space.glome.http.schema.domain.Header;
import space.glome.http.schema.domain.RawURL;
import space.glome.http.schema.domain.URL;

public class ApacheHttpConverters {

	static org.apache.http.Header[] convert(List<Header> from) {
		if (from == null) {
			return null;
		}
		org.apache.http.Header[] retVal = new org.apache.http.Header[from.size()];
		for (int i = 0; i < from.size(); i++) {
			retVal[i] = convert(from.get(i));
		}
		return retVal;
	}

	static org.apache.http.Header convert(Header from) {
		if (from == null) {
			return null;
		}
		return new BasicHeader(from.getKey(), from.getValue());
	}

	static List<Header> convert(org.apache.http.Header[] from) {
		if (from == null) {
			return null;
		}
		List<Header> retVal = new ArrayList<>();
		for (int i = 0; i < from.length; i++) {
			retVal.add(convert(from[i]));
		}
		return retVal;
	}

	static Header convert(org.apache.http.Header from) {
		if (from == null) {
			return null;
		}
		return new Header(from.getName(), from.getValue());
	}

	static String convert(URL from) {
		if (from == null) {
			return null;
		}
		try {
			if (from instanceof RawURL) {
				RawURL rawURL = (RawURL) from;
				return new URI(rawURL.getRaw()).toString();
			} else if (from instanceof CompositeURL) {
				CompositeURL compositeURL = (CompositeURL) from;
				return new URI(compositeURL.getScheme(), compositeURL.getUserInfo(), compositeURL.getHost(),
						compositeURL.getPort() == null ? 0 : compositeURL.getPort(), compositeURL.getPath(),
						compositeURL.getQuery(), compositeURL.getFragment()).toString();
			} else {
				throw new Error("Can't convert URL");
			}
		} catch (URISyntaxException e) {
			throw new Error("Can't convert URL", e);
		}
	}

}
