package lastviewedproducts;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.components.ViewModelComponent;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.ProductVariantLoadedHook;
import com.commercetools.sunrise.framework.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListViewModelFactory;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static lastviewedproducts.LastViewedProductUtils.*;

/**
 * In this exercise we are going to see how to implement a {@link ControllerComponent} that depends on a loaded CTP resource.
 * Whenever we are visiting a product we are going to save this product to a list in session for future requests.
 * For the current request, we are going to display the list of last viewed products as it was before this visit.
 *
 * Hook 1: Whenever a product with variant (we need the SKU!) is loaded, save it in session by calling {@link #saveProductToLastViewedList(ProductVariant)}
 * Hook 2: To display the last viewed products in the page, first fetch and save them by calling {@link #fetchAndSaveLastViewedProducts()}
 * Hook 3: And once the {@link PageData} is built and ready, add the last viewed products to it by calling {@link #addLastViewedProductsToPageData(PageData)}
 */
public class LastViewedProductsControllerComponent implements ControllerComponent, RequestStartedHook, ProductVariantLoadedHook, PageDataReadyHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(LastViewedProductsControllerComponent.class);

    private final SphereClient sphereClient;
    private final Http.Session session;
    private final PriceSelection priceSelection;
    private final ProductListViewModelFactory productListViewModelFactory;

    private List<ProductProjection> lastViewedProducts;

    @Inject
    public LastViewedProductsControllerComponent(final SphereClient sphereClient, final Http.Session session,
                                                 final PriceSelection priceSelection,
                                                 final ProductListViewModelFactory productListViewModelFactory) {
        this.sphereClient = sphereClient;
        this.session = session;
        this.priceSelection = priceSelection;
        this.productListViewModelFactory = productListViewModelFactory;
    }

    @Override
    public CompletionStage<?> onRequestStarted(final Http.Context httpContext) {
        return fetchAndSaveLastViewedProducts();
    }

    @Override
    public CompletionStage<?> onProductVariantLoaded(final ProductProjection productProjection, final ProductVariant productVariant) {
        return saveProductToLastViewedList(productVariant);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        addLastViewedProductsToPageData(pageData);
    }

    /**
     * Fetches the list of viewed products according to what is stored in session
     * and saves it in {@code lastViewedProducts} class field.
     */
    private CompletionStage<?> fetchAndSaveLastViewedProducts() {
        final List<String> skuList = findLastViewedProductsSkuInSession(session);
        if (!skuList.isEmpty()) {
            final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                    .withQueryFilters(product -> product.allVariants().sku().isIn(skuList))
                    .withPriceSelection(priceSelection);
            return sphereClient.execute(request)
                    .thenApply(result -> {
                        this.lastViewedProducts = sortProductsBySkuList(result.getResults(), skuList);
                        return null;
                    }).exceptionally(throwable -> {
                        LOGGER.error("Could not fetch last viewed products", throwable);
                        return null;
                    });
        }
        return completedFuture(null);
    }

    /**
     * Saves the product into the last viewed products list
     * @param variant product variant to be saved as last viewed
     */
    private CompletionStage<?> saveProductToLastViewedList(final ProductVariant variant) {
        if (variant.getSku() != null) {
            saveSkuInSession(session, variant.getSku());
        }
        return completedFuture(null);
    }

    /**
     * Includes the required information to display the last viewed products to the page data.
     * @param pageData the data that is going to be used to render the page
     */
    private void addLastViewedProductsToPageData(final PageData pageData) {
        if (lastViewedProducts != null) {
            final ViewModelComponent viewModelComponent = new ViewModelComponent();
            viewModelComponent.setTemplateName("last-viewed-products");
            viewModelComponent.put("productList", productListViewModelFactory.create(lastViewedProducts).getList());
            pageData.getContent().addComponent(viewModelComponent);
        }
    }
}
