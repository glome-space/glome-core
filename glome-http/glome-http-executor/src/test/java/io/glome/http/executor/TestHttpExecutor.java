package io.glome.http.executor;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;

import org.junit.BeforeClass;
import org.junit.Test;

import ratpack.http.HttpUrlBuilder;
import ratpack.http.client.HttpClient;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import ratpack.test.embed.EmbeddedApp;

public class TestHttpExecutor {

	@BeforeClass
	public static void runTestServer() throws Exception {
		RatpackServer.start(serverSpec -> serverSpec //
				.serverConfig(ServerConfig.builder().address(InetAddress.getLoopbackAddress()).port(8888).build())
				.handlers( //
						chain -> chain.get(ctx -> ctx.render("Hello Glome")) //
								.get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!")) //
		));

	}

	@Test
	public void testCallServer() throws Exception {
		EmbeddedApp.fromHandler(ctx -> ctx.render(ctx.get(HttpClient.class).get( //
				HttpUrlBuilder.http().host(InetAddress.getLoopbackAddress().getHostAddress()).port(8888).build())
				.map(response -> response.getBody().getText())))
				.test(httpClient -> assertEquals("Hello Glome", httpClient.getText()));

	}
}
