package lastviewedproducts;

import com.commercetools.sunrise.components.ViewModelComponent;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListViewModelFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static io.sphere.sdk.utils.CompletableFutureUtils.successful;
import static java.util.Collections.emptyList;

/**
 * 1. save loaded variant SKU in session
 * 2. fetch products in session and save them to be used later
 * 3. add the data to the page as a component
 */
public class LastViewedProductsControllerComponent implements ControllerComponent {

    private static final String SESSION_KEY = "lastSeenProductSkus";
    private static final int MAX_CAPACITY = 4;
    private List<ProductProjection> productProjections;

    private CompletionStage<List<ProductProjection>> fetchProductsFromSkuInSession(final Http.Context context, final SphereClient sphereClient) {
        final List<String> skus = getSkusFromSession(context.session());
        return findProductsBySku(sphereClient, skus)
                .thenApply(products -> getSortedOnes(products, skus));
    }


    private static void writeSkuToSession(@Nullable final String sku, final Http.Session session, final int capacity) {
        if (sku != null) {
            final List<String> skusFromSession = getSkusFromSession(session);
            final LinkedList<String> skus = new LinkedList<>(skusFromSession);
            skus.remove(sku);
            skus.add(sku);
            while (skus.size() > capacity) {
                skus.remove();
            }
            try {
                final String sessionValue = Json.mapper().writeValueAsString(skus);
                session.put(SESSION_KEY, sessionValue);
                Logger.debug("Saving product {} to session {}", sku, session.get(SESSION_KEY));
            } catch (JsonProcessingException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static ViewModelComponent createComponentBean(final ProductListViewModelFactory productListViewModelFactory,
                                                          final List<ProductProjection> productProjections) {
        final ViewModelComponent componentBean = new ViewModelComponent();
        componentBean.setTemplateName("components/LastViewedProducts/productsView");
        componentBean.put("list", productListViewModelFactory.create(productProjections));
        return componentBean;
    }

    private static List<String> getSkusFromSession(final Http.Session session) {
        final String skuJsonArrayString = Optional.ofNullable(session.get(SESSION_KEY)).orElse("[]");
        final ArrayNode skusJson = (ArrayNode) Json.parse(skuJsonArrayString);
        final List<String> skus = new LinkedList<>();
        skusJson.elements().forEachRemaining(node -> skus.add(node.asText()));
        return skus;
    }

    private static CompletionStage<List<ProductProjection>> findProductsBySku(final SphereClient sphereClient, final List<String> skus) {
        return  skus.isEmpty()
                ? successful(emptyList())
                : sphereClient.execute(ProductProjectionSearch.ofCurrent()
                .withQueryFilters(product -> product.allVariants().sku().isIn(skus))).thenApply(PagedSearchResult::getResults);
    }

    private static List<ProductProjection> getSortedOnes(final List<ProductProjection> res, final List<String> skus) {
        final Map<String, ProductProjection> skuToProjectionMap = new HashMap<>();
        res.forEach(p -> p.getAllVariants().forEach(v -> {
            if (v.isMatchingVariant()) {
                skuToProjectionMap.put(v.getSku(), p);
            }
        }));
        return skus.stream().map(sku -> skuToProjectionMap.get(sku)).collect(Collectors.toList());
    }
}
