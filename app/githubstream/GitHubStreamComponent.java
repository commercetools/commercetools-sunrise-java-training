package githubstream;

import com.commercetools.sunrise.components.ComponentViewModel;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.libs.ws.WSAPI;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * 1. do the webservice call and save the feed data to be used later
 * 2. add the data to the page as a component
 */
public class GitHubStreamComponent extends Base implements ControllerComponent {
    private String url;
    private String templateName;
    @Inject
    private WSAPI ws;
    private List<GitHubIssueData> dataList = new LinkedList<>();

    @Inject
    private void setConfig(final Configuration configuration) {
        url = configuration.getString("GitHubStreamComponent.url");
        templateName = configuration.getString("GitHubStreamComponent.templateName");
    }

    private CompletionStage<Optional<WSResponse>> performWebserviceCall() {
        return ws.url(url).get()//Play stuff
                .thenApply(res -> Optional.ofNullable(res).filter(r -> r.getStatus() == 200))//ignore responses other than 200
                .exceptionally(e -> Optional.empty());//recover by providing an empty Optional
    }

    private static List<GitHubIssueData> extractData(final WSResponse r) {
        final List<GitHubIssueData> result = new LinkedList<>();
        final JsonNode jsonNode = r.asJson();
        if (jsonNode instanceof ArrayNode) {
            final ArrayNode arrayNode = (ArrayNode) jsonNode;
            arrayNode.elements().forEachRemaining(element -> {
                final GitHubIssueData bean = new GitHubIssueData();
                bean.setName(element.get("title").asText());
                bean.setUrl(element.get("html_url").asText());
                result.add(bean);
            });
        }
        return result;
    }

    private ComponentViewModel createComponentViewModel() {
        final ComponentViewModel componentBean = new ComponentViewModel();
        componentBean.setTemplateName(templateName);
        final HashMap<String, Object> data = new HashMap<>();
        data.put("list", dataList);
        componentBean.setComponentData(data);
        return componentBean;
    }
}