package exercises.extendingjvmsdk;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientDecorator;
import io.sphere.sdk.client.SphereRequest;

import java.util.concurrent.CompletionStage;

public class MySphereClient extends SphereClientDecorator {

    public MySphereClient(final SphereClient delegate) {
        super(delegate);
    }

    @Override
    public <T> CompletionStage<T> execute(final SphereRequest<T> sphereRequest) {
        // do things before the request is executed
        final CompletionStage<T> result = super.execute(sphereRequest);
        // do things after the request is executed
        return result;
    }

    // add additional methods
}
