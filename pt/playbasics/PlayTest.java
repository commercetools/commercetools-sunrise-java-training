package playbasics;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.POST;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

public class PlayTest extends WithApplication {

    @Test
    public void showsHelloWorld() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show1"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello World!");
    }

    @Test
    public void showsHelloNameByPath() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show2/John"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John!");
    }

    @Test
    public void showsHelloNameByQueryString() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show2?name=John"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John!");
    }

    @Test
    public void showsByeNameFromSession() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show3?name=John")
                .session("name", "Snow"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John! Bye Snow");
        assertThat(result.session().get("name")).isEqualTo("John");
    }

    @Test
    public void showsByeStrangerFromEmptySession() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show3?name=John"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John! Bye stranger");
        assertThat(result.session().get("name")).isEqualTo("John");
    }

    @Test
    public void showsByeNameFromCookie() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show4?name=John")
                .cookie(Http.Cookie.builder("name", "Snow").build()));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John! Bye Snow");
        assertThat(result.cookie("name").value()).isEqualTo("John");
    }

    @Test
    public void showsByeStrangerFromEmptyCookie() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show4?name=John"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John! Bye stranger");
        assertThat(result.cookie("name").value()).isEqualTo("John");
    }

    @Test
    public void showsHelloNameFromFormByGet() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show5?name=John"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John!");
    }

    @Test
    public void showsHelloNameFromFormByPost() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show5")
                .method(POST)
                .bodyForm(singletonMap("name", "John")));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("Hello John!");
    }

    @Test
    public void showsFormErrorOnMissingRequiredField() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show5"));
        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result))
                .startsWith("Form had errors")
                .containsOnlyOnce("name")
                .containsOnlyOnce("field is required");
    }
}
