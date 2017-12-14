package space.glome.http.executor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicHeader;

import space.glome.http.schema.domain.Header;

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

}
