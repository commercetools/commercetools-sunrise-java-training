package githubstream;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.components.ComponentData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import com.commercetools.sunrise.hooks.RequestHook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.sphere.sdk.models.Base;
import play.libs.ws.WSAPI;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class GitHubStreamComponent extends Base implements ControllerComponent, RequestHook, PageDataHook {
    public static final String URL = "https://api.github.com/repos/commercetools/commercetools-jvm-sdk/issues";
    @Inject
    private WSAPI ws;
    private List<GitHubIssueData> dataList = new LinkedList<>();

    @Override
    public CompletionStage<?> onRequest(final Http.Context context) {
        return ws.url(URL).get().thenAccept(r -> {
            if (r.getStatus() == 200) {
                storeData(r);
            }
        });
    }

    @Override
    public void acceptPageData(final PageData pageData) {
        final ComponentData componentData = new ComponentData();
        componentData.setTemplateName("components/GitHub/issues");
        final HashMap<String, Object> data = new HashMap<>();
        data.put("list", dataList);
        componentData.setComponentData(data);
        pageData.getContent().addComponent(componentData);
    }

    private void storeData(final WSResponse r) {
        final JsonNode jsonNode = r.asJson();
        if (jsonNode instanceof ArrayNode) {
            final ArrayNode arrayNode = (ArrayNode) jsonNode;
            arrayNode.elements().forEachRemaining(element -> {
                final GitHubIssueData bean = new GitHubIssueData();
                bean.setName(element.get("title").asText());
                bean.setUrl(element.get("html_url").asText());
                dataList.add(bean);
            });
        }
    }

    private static final class GitHubIssueData extends Base {
        private String name;
        private String url;

        public GitHubIssueData() {
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(final String url) {
            this.url = url;
        }
    }
}