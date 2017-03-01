import play.Configuration;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(final Configuration configuration, final Environment environment,
                        final OptionalSourceMapper sourceMapper, final Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }
}
