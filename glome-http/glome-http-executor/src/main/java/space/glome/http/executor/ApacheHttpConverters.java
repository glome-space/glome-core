package space.glome.http.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.message.BasicHeader;

import space.glome.http.schema.domain.CompositeURL;
import space.glome.http.schema.domain.FileRequestBody;
import space.glome.http.schema.domain.FormDataRequestBody;
import space.glome.http.schema.domain.Header;
import space.glome.http.schema.domain.RawRequestBody;
import space.glome.http.schema.domain.RawURL;
import space.glome.http.schema.domain.RequestBody;
import space.glome.http.schema.domain.URL;
import space.glome.http.schema.domain.UrlEncodedRequestBody;

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

	static byte[] convert(RequestBody from) {
		if (from == null) {
			return new byte[0];
		}
		if (from instanceof RawRequestBody) {
			return ((RawRequestBody) from).getRaw().getBytes();
		} else if (from instanceof FileRequestBody) {
			try (Scanner scanner = new Scanner(new File(((FileRequestBody) from).getFilePath()))) {
				return scanner.useDelimiter("\\Z").next().getBytes();
			} catch (FileNotFoundException e) {
				throw new Error("Can't load body file", e);
			}
		} else if (from instanceof FormDataRequestBody) {
			// TODO
			throw new Error("Converter for FormDataRequestBody is not implemented yet");
		} else if (from instanceof UrlEncodedRequestBody) {
			// TODO
			throw new Error("Converter for UrlEncodedRequestBody is not implemented yet");
		} else {
			throw new Error("Converter for " + from.getClass().getSimpleName() + " is not implemented");
		}
	}
}
