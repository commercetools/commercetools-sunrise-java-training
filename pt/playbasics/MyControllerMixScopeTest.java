package playbasics;

import org.junit.Test;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.mvc.Result;
import play.test.TestServer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class MyControllerMixScopeTest {

    @Test
    public void showsHelloWorld() throws Exception {
        running(fakeApplication(), () -> {
            final Result result = route(new Http.RequestBuilder()
                    .uri("/playbasics/show1"));
            assertThat(result.status()).isEqualTo(OK);
            assertThat(contentAsString(result)).isEqualTo("Hello World!");
        });
    }

    @Test
    public void processesFormByGet() throws Exception {
        final TestServer testServer = testServer(fakeApplication());
        running(testServer, () -> {
            try (final WSClient wsClient = WS.newClient(testServer.port())) {
                final WSResponse wsResponse = wsClient
                        .url("/playbasics/show2")
                        .setQueryString("name=John")
                        .get().toCompletableFuture().get();
                assertThat(wsResponse.getStatus()).isEqualTo(OK);
                assertThat(wsResponse.getBody()).isEqualTo("Hello John!");
            } catch (IOException | InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void showsAndProcessesForm() throws Exception {
        final TestServer testServer = testServer(fakeApplication());
        running(testServer, HTMLUNIT, browser -> {
            browser.goTo("/playbasics/show5");
            browser.fill("form input[name=name]").with("John");
            browser.submit("button[type=submit]");

            assertThat(browser.title()).isEqualTo("Hello you!");
            assertThat(browser.url()).isEqualTo("/playbasics/show5");
            assertThat(browser.text("p.message")).containsOnly("Hello John!");
        });
    }
}
