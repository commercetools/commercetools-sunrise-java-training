package exercises.httpcontext;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Access http://localhost:9000/httpcontext
 *
 * Running the code we will see an exception that says "There is no HTTP Context available from here".
 * Find out how to fix it!
 */
public class HttpContextController extends Controller {

    private final SphereClient sphereClient;

    @Inject
    public HttpContextController(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    public CompletionStage<Result> show() {
        return sphereClient.execute(ProjectGet.of())
                .thenApply(Project::getKey)
                .thenApply(this::responseUsingHttpContext);
    }

    private Result responseUsingHttpContext(final String projectKey) {
        return ok(Http.Context.current().request().remoteAddress() + " is accessing project " + projectKey);
    }
}
