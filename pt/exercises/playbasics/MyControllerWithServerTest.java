package exercises.playbasics;

import org.junit.Test;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.WithServer;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.OK;

public class MyControllerWithServerTest extends WithServer {

    @Test
    public void processesFormByGet() throws Exception {
        try (final WSClient wsClient = WS.newClient(testServer.port())) {
            final WSResponse wsResponse = wsClient
                    .url("/playbasics/show2")
                    .setQueryString("name=John")
                    .get().toCompletableFuture().get();
            assertThat(wsResponse.getStatus()).isEqualTo(OK);
            assertThat(wsResponse.getBody()).isEqualTo("Hello John!");
        }
    }
}
