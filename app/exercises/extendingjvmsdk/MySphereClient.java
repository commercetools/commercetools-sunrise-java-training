package exercises.extendingjvmsdk;

import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientDecorator;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.client.SphereRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class MySphereClient extends SphereClientDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySphereClient.class);

    @Inject
    public MySphereClient(final SphereClientConfig sphereClientConfig) {
        super(SphereClientFactory.of().createClient(sphereClientConfig));
    }

    @Override
    public <T> CompletionStage<T> execute(final SphereRequest<T> sphereRequest) {
        // do things before the request is executed, e.g. logging
        LOGGER.debug("Executing request: {}", sphereRequest.toString());

        // do the request
        final CompletionStage<T> result = super.execute(sphereRequest);

        // do things after the request is executed, e.g. logging
        result.thenAccept(response -> LOGGER.debug("Received response: {}", response.toString()));

        return result;
    }

    // add additional methods
}
