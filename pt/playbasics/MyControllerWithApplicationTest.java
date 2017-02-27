package playbasics;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

public class MyControllerWithApplicationTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .configure(singletonMap("playbasics.pageTitle", "Another title"))
                .build();
    }

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
    public void showsForm() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show5"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result))
                .contains("Type your name")
                .contains("<form action=\"/playbasics/process5\" method=\"post\">")
                .doesNotContain("Hello")
                .contains("Another title");
    }

    @Test
    public void showsFormWithSuccessMessage() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/show5")
                .flash("message", "Hello John!"));
        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result))
                .contains("Type your name")
                .contains("<form action=\"/playbasics/process5\" method=\"post\">")
                .contains("Hello John!")
                .contains("Another title");
    }

    @Test
    public void showsHelloNameFromFormByGet() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/process5?name=John"));
        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().get("message")).isEqualTo("Hello John!");
    }

    @Test
    public void showsHelloNameFromFormByPost() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/process5")
                .method(POST)
                .bodyForm(singletonMap("name", "John")));
        assertThat(result.status()).isEqualTo(SEE_OTHER);
        assertThat(result.flash().get("message")).isEqualTo("Hello John!");
    }

    @Test
    public void showsFormErrorOnMissingRequiredField() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/playbasics/process5"));
        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result))
                .contains("Form had errors")
                .containsOnlyOnce("This field is required");
    }
}
