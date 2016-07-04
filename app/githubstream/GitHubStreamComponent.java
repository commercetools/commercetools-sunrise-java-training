package githubstream;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.sphere.sdk.models.Base;
import play.libs.ws.WSAPI;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class GitHubStreamComponent extends Base implements ControllerComponent {
    public static final String URL = "https://api.github.com/repos/commercetools/commercetools-jvm-sdk/issues";
    @Inject
    private WSAPI ws;
    private List<GitHubIssueData> dataList = new LinkedList<>();

    //task as a workaround the template is in conf/templates/catalog/home/home-suggestions.hbs

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
