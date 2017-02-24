package githubfeed;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.components.ComponentViewModel;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.RequestStartedHook;
import io.sphere.sdk.models.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.ws.WSAPI;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static githubfeed.GitHubFeedUtils.extractGitHubIssues;

/**
 * 1. do the webservice call and save the feed data to be used later
 * 2. add the data to the page as a component
 */
public class GitHubFeedControllerComponent extends Base implements ControllerComponent, RequestStartedHook, PageDataReadyHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubFeedControllerComponent.class);
    private static final String GITHUB_URL = "https://api.github.com/repos/lauraluiz/issue-provider/issues";
    private static final String TEMPLATE_NAME = "githubfeed/issues";

    private final WSAPI wsApi;
    private List<Map> gitHubIssues;

    @Inject
    public GitHubFeedControllerComponent(final WSAPI wsApi) {
        this.wsApi = wsApi;
    }

    @Override
    public CompletionStage<?> onRequestStarted(final Http.Context httpContext) {
        return fetchGitHubIssues();
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (gitHubIssues != null) {
            pageData.getContent().addComponent(createComponentViewModel());
        }
    }

    /**
     * Fetches the GitHub issues and saves the ....
     */
    private CompletionStage<Void> fetchGitHubIssues() {
         return wsApi.url(GITHUB_URL).get()
                 .thenApply(result -> {
                    if (result.getStatus() == 200) {
                        this.gitHubIssues = extractGitHubIssues(result.asJson());
                    } else {
                        LOGGER.error("GitHub unexpectedly answered with status {} and body {}", result.getStatus(), result.getBody());
                    }
                    return null;
                 });
    }

    private ComponentViewModel createComponentViewModel() {
        final ComponentViewModel componentBean = new ComponentViewModel();
        componentBean.setTemplateName(TEMPLATE_NAME);
        final HashMap<String, Object> data = new HashMap<>();
        data.put("list", gitHubIssues);
        componentBean.setComponentData(data);
        return componentBean;
    }
}