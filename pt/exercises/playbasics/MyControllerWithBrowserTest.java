package exercises.playbasics;

import org.junit.Test;
import play.test.WithBrowser;

import static org.assertj.core.api.Assertions.assertThat;

public class MyControllerWithBrowserTest extends WithBrowser {

    @Test
    public void showsAndProcessesForm() throws Exception {
        browser.goTo("/playbasics/show5");
        browser.fill("form input[name=name]").with("John");
        browser.submit("button[type=submit]");

        assertThat(browser.title()).isEqualTo("Hello you!");
        assertThat(browser.url()).isEqualTo("/playbasics/show5");
        assertThat(browser.text("p.message")).containsOnly("Hello John!");
    }
}
