package exercises.lastviewedproducts;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.components.viewmodels.ViewModelComponent;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListViewModelFactory;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static exercises.lastviewedproducts.LastViewedProductUtils.findLastViewedProductsSkuInSession;
import static exercises.lastviewedproducts.LastViewedProductUtils.sortProductsBySkuList;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * In this exercise we are going to see how to implement a {@link ControllerComponent} that depends on a loaded CTP resource.
 * Whenever we are visiting a product we are going to save this product to a list in session for future requests.
 * For the current request, we are going to display the last viewed products as it was before this visit.
 *
 * Step 1: Register this component to any controller where you want to display the last viewed products
 *
 * Step 2: Implement the missing hooks
 *   Hook 1: To display the last viewed products in the page, first fetch and save them by calling {@link #fetchAndSaveLastViewedProducts()},
 *           notice this can be done right after the HTTP request starts
 *   Hook 2: Once the {@link PageData} is built and ready, add the last viewed products to it by calling {@link #addLastViewedProductsToPageData(PageData)}
 *
 * How to check:
 * - Visit a couple of different product detail pages
 * - Go to the page of the chosen controller
 * - Scroll to the bottom, you should be able to see a "Last Viewed Products" section with those products you visited
 */
public class LastViewedProductsViewerControllerComponent implements ControllerComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(LastViewedProductsViewerControllerComponent.class);

    private final SphereClient sphereClient;
    private final PriceSelection priceSelection;
    private final ProductListViewModelFactory productListViewModelFactory;

    private List<ProductProjection> lastViewedProducts;

    @Inject
    public LastViewedProductsViewerControllerComponent(final SphereClient sphereClient,
                                                       final PriceSelection priceSelection,
                                                       final ProductListViewModelFactory productListViewModelFactory) {
        this.sphereClient = sphereClient;
        this.priceSelection = priceSelection;
        this.productListViewModelFactory = productListViewModelFactory;
    }

    /**
     * Fetches the list of viewed products according to what is stored in session
     * and saves it in {@code lastViewedProducts} class field.
     */
    private CompletionStage<?> fetchAndSaveLastViewedProducts() {
        final List<String> skuList = findLastViewedProductsSkuInSession(Http.Context.current().session());
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
     * Includes the required information to display the last viewed products to the page data.
     * @param pageData the data that is going to be used to render the page
     */
    private void addLastViewedProductsToPageData(final PageData pageData) {
        if (lastViewedProducts != null) {
            final ViewModelComponent viewModelComponent = new ViewModelComponent();
            viewModelComponent.setTemplateName("exercises/last-viewed-products");
            viewModelComponent.put("productList", productListViewModelFactory.create(lastViewedProducts).getList());
            pageData.getContent().addComponent(viewModelComponent);
        }
    }
}
