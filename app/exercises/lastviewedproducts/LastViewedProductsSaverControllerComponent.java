package exercises.lastviewedproducts;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Http;

import java.util.concurrent.CompletionStage;

import static exercises.lastviewedproducts.LastViewedProductUtils.saveSkuInSession;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * In this exercise we are going to see how to implement a {@link ControllerComponent} that depends on a loaded CTP resource.
 * Whenever we are visiting a product we are going to save this product to a list in session for future requests.
 *
 * Step 1: Register this component to {@link controllers.productcatalog.ProductDetailController},
 *         as it's the only available controller that loads a single product
 * Step 2: Implement the missing hook
 *   Hook: Whenever a product with variant (we need the SKU!) is loaded, save it in session by calling {@link #saveProductToLastViewedList(ProductVariant)}
 *
 * How to check:
 * - See {@link LastViewedProductsViewerControllerComponent}
 */
public class LastViewedProductsSaverControllerComponent implements ControllerComponent {

    /**
     * Saves the product into the last viewed products list
     * @param variant product variant to be saved as last viewed
     */
    private CompletionStage<?> saveProductToLastViewedList(final ProductVariant variant) {
        if (variant.getSku() != null) {
            saveSkuInSession(Http.Context.current().session(), variant.getSku());
        }
        return completedFuture(null);
    }
}
