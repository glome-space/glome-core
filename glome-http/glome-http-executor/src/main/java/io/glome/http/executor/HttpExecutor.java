package io.glome.http.executor;

import java.net.URI;

import ratpack.http.client.HttpClient;
import ratpack.server.PublicAddress;
import ratpack.test.embed.EmbeddedApp;

public class HttpExecutor {

	public static void main(String... args) throws Exception {
		EmbeddedApp.fromHandlers(chain -> {
			chain.get("simpleGet", ctx -> {
				PublicAddress address = ctx.get(PublicAddress.class); // find local ip address
				HttpClient httpClient = ctx.get(HttpClient.class); // get httpClient
				URI uri = address.get("httpClientGet");

				httpClient.get(uri).then(response -> ctx.render(response.getBody().getText()) // Render the response
																								// from the httpClient
																								// GET request
				);
			}).get("simplePost", ctx -> {
				PublicAddress address = ctx.get(PublicAddress.class); // find local ip address
				HttpClient httpClient = ctx.get(HttpClient.class); // get httpClient
				URI uri = address.get("httpClientPost");

				httpClient.post(uri, s -> s.getBody().text("foo"))
						.then(response -> ctx.render(response.getBody().getText()) // Render the response from the
																					// httpClient POST request
				);
			}).get("httpClientGet", ctx -> ctx.render("httpClientGet")).post("httpClientPost",
					ctx -> ctx.render(ctx.getRequest().getBody().map(b -> b.getText().toUpperCase())));
		}).test(testHttpClient -> {
			System.out.println("++++++++++++++++++simpleGet " + testHttpClient.getText("/simpleGet"));
			System.out.println("++++++++++++++++++simplePost " + testHttpClient.getText("/simplePost"));
		});
	}
}
