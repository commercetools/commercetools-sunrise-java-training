package githubfeed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import githubfeed.GitHubFeedControllerComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

final class GitHubFeedUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(GitHubFeedControllerComponent.class);

    private GitHubFeedUtils() {
    }

    static List<Map> extractGitHubFeed(final JsonNode jsonNode) {
        final ObjectMapper mapper = Json.mapper();
        final List<Map> result = new LinkedList<>();
        if (jsonNode instanceof ArrayNode) {
            jsonNode.forEach(node -> {
                try {
                    result.add(mapper.readValue(node.toString(), Map.class));
                } catch (IOException e) {
                    LOGGER.error("Could not parse GitHub issue with content {}", node, e);
                }
            });
        }
        return result;
    }
}
