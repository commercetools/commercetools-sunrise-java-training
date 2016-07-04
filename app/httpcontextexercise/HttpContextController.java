package httpcontextexercise;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.Locale.ENGLISH;

public class HttpContextController extends Controller {
    @Inject
    private SphereClient sphereClient;

    /*
    given is this normal Play controller
    open http://localhost:9000/exercise/httpcontext
    it will render: [CompletionException: java.lang.RuntimeException: There is no HTTP Context available from here.]
    fix the error
     */
    public CompletionStage<Result> show(final String englishSlug) {
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withLimit(1)
                .withQueryFilters(product -> product.slug().locale(ENGLISH).is(englishSlug));
        return sphereClient.execute(request)
                .thenApply(result -> result.head())//one possible solution here (A)
                .thenApply(productProjectionOptional -> {//one possible solution here (B)
                    productProjectionOptional.ifPresent(prod -> {
                        session("lastSeenProduct", prod.getId());
                    });
                    return productProjectionOptional
                            .map(product -> ok(product.getName().get(ENGLISH)))
                            .orElse(notFound("product not found"));
                });
    }
}
