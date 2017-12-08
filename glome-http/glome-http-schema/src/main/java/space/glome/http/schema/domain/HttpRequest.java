package space.glome.http.schema.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import space.glome.schema.domain.Argument;
import space.glome.schema.domain.Request;

public class HttpRequest extends Request {

	public enum Method {
		GET, PUT, POST, PATCH, DELETE, COPY, HEAD, OPTIONS, LINK, UNLINK, PURGE, LOCK, UNLOCK, PROPFIND, VIEW
	}

	private URL url;

	private Method method;

	private List<Header> headers;

	private RequestBody requestBody;

	private Proxy proxy;

	private Certificate certificate;

	protected HttpRequest() {
	}

	public HttpRequest(URL url, Method method) {
		this.url = url;
		this.method = method;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@JsonInclude(Include.NON_NULL)
	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	@JsonInclude(Include.NON_NULL)
	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@JsonInclude(Include.NON_NULL)
	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	@JsonInclude(Include.NON_NULL)
	public RequestBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(RequestBody body) {
		this.requestBody = body;
	}

	public void removeHeader(String string) {
		for (ListIterator<Header> iterator = headers.listIterator(); iterator.hasNext();) {
			if (iterator.next().getKey().equals(string)) {
				iterator.remove();
			}
		}
	}

	@JsonIgnore
	public byte[] getPayload() {
		if (requestBody == null) {
			return new byte[0];
		}
		if (requestBody instanceof RawRequestBody) {
			return ((RawRequestBody) requestBody).getRaw().getBytes();
		} else if (requestBody instanceof FileRequestBody) {
			try (Scanner scanner = new Scanner(new File(((FileRequestBody) requestBody).getFilePath()))) {
				String fileContent = scanner.useDelimiter("\\Z").next();
				for (Argument argument : getArguments()) {
					fileContent = fileContent.replace("${" + argument.getKey() + "}", argument.getValue());
				}
				return fileContent.getBytes();
			} catch (FileNotFoundException e) {
				throw new Error("Can't load body file", e);
			}
		} else if (requestBody instanceof FormDataRequestBody) {
			// TODO
			throw new Error("Converter for FormDataRequestBody is not implemented yet");
		} else if (requestBody instanceof UrlEncodedRequestBody) {
			// TODO
			throw new Error("Converter for UrlEncodedRequestBody is not implemented yet");
		} else {
			throw new Error("Converter for " + this.getClass().getSimpleName() + " is not implemented");
		}
	}
}
