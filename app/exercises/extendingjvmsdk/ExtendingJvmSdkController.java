package exercises.extendingjvmsdk;

import com.commercetools.sunrise.framework.controllers.SunriseController;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.projects.queries.ProjectGet;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Access http://localhost:9000/extendingjvmsdk
 */
public class ExtendingJvmSdkController extends SunriseController {

    private final SphereClient sphereClient;

    @Inject
    public ExtendingJvmSdkController(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    public CompletionStage<Result> show() {
        return sphereClient.execute(ProjectGet.of())
                .thenApply(project -> ok(project.getKey()));
    }
}
