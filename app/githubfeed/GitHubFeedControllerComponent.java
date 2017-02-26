package githubfeed;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.components.ViewModelComponent;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.RequestStartedHook;
import io.sphere.sdk.models.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.ws.WSAPI;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static githubfeed.GitHubFeedUtils.extractGitHubFeed;

/**
 * In this exercise we are going to see how to implement a {@link ControllerComponent} that is not dependent on CTP data.
 * We are going to fetch a GitHub feed and display it on the page, on those controllers where it is registered.
 *
 * Hook 1: When the request starts, fetch the GitHub feed and save it by calling {@link #fetchAndSaveGitHubFeed()}
 * Hook 2: Once the {@link PageData} is built and ready, add the GitHub feed to it by calling {@link #addGitHubFeedToPageData(PageData)}
 */
public class GitHubFeedControllerComponent extends Base implements ControllerComponent, RequestStartedHook, PageDataReadyHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubFeedControllerComponent.class);
    private static final String GITHUB_URL = "https://api.github.com/repos/lauraluiz/issue-provider/issues?sort=updated";

    private final WSAPI wsApi;
    private List<Map> gitHubFeed;

    @Inject
    public GitHubFeedControllerComponent(final WSAPI wsApi) {
        this.wsApi = wsApi;
    }

    @Override
    public CompletionStage<?> onRequestStarted(final Http.Context httpContext) {
        return fetchAndSaveGitHubFeed();
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        addGitHubFeedToPageData(pageData);
    }

    /**
     * Fetches the GitHub issues and saves them in {@code gitHubFeed} class field
     */
    private CompletionStage<Void> fetchAndSaveGitHubFeed() {
        return wsApi.url(GITHUB_URL).get()
                .thenApply(result -> {
                    if (result.getStatus() == Http.Status.OK) {
                        this.gitHubFeed = extractGitHubFeed(result.asJson());
                    } else {
                        LOGGER.error("GitHub unexpectedly answered with status {} and body {}", result.getStatus(), result.getBody());
                    }
                    return (Void) null;
                }).exceptionally(throwable -> {
                    LOGGER.error("Could not fetch GitHub feed", throwable);
                    return null;
                });
    }

    /**
     * Includes the required information to display the GitHub feed to the page data.
     * @param pageData the data that is going to be used to render the page
     */
    private void addGitHubFeedToPageData(final PageData pageData) {
        if (gitHubFeed != null) {
            final ViewModelComponent componentViewModel = new ViewModelComponent();
            componentViewModel.setTemplateName("githubfeed/issues");
            componentViewModel.put("feed", gitHubFeed);
            pageData.getContent().addComponent(componentViewModel);
        }
    }
}