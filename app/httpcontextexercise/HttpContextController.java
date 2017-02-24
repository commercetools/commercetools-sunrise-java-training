package httpcontextexercise;

import com.commercetools.sunrise.framework.controllers.SunriseController;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.Locale.ENGLISH;

public class HttpContextController extends SunriseController {
    @Inject
    private SphereClient sphereClient;
    @Inject
    private HttpExecutionContext httpExecutionContext;

    /*
    given is this normal Play controller
    open http://localhost:9000/exercise/httpcontext
    it will render: [CompletionException: java.lang.RuntimeException: There is no HTTP Context available from here.]
    fix the error

    More info: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
     */
    public CompletionStage<Result> show(final String englishSlug) {
        return searchProduct(englishSlug)
                .thenApply(PagedResult::head) // one possible solution here (A)
                .thenApplyAsync(productOptional -> productOptional //one possible solution here (B)
                        .map(this::handleFoundProduct)
                        .orElseGet(this::handleNotFoundResult), HttpExecution.defaultContext());
    }

    private CompletionStage<PagedSearchResult<ProductProjection>> searchProduct(final String englishSlug) {
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withLimit(1)
                .withQueryFilters(product -> product.slug().locale(ENGLISH).is(englishSlug));
        return sphereClient.execute(request);
    }

    private Result handleFoundProduct(final ProductProjection product) {
        session("lastSeenProduct", product.getId());
        return ok(product.getName().find(ENGLISH).orElse(""));
    }

    private Result handleNotFoundResult() {
        return notFound("product not found");
    }
}
