package producthistory;

import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.RequestHook;
import com.commercetools.sunrise.hooks.SingleProductVariantHook;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.Json;
import play.mvc.Http;

import javax.inject.Inject;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static io.sphere.sdk.utils.CompletableFutureUtils.successful;
import static java.util.Collections.emptyList;

public class ProductHistoryComponent implements ControllerComponent {
    public static final String SESSION_KEY = "lastSeenProductSkus";

    private int capacity = 4;//TODO read from config

    private static void writeValuesToSession(final Http.Session session, final List<String> skus) {
        try {
            final String sessionValue = Json.mapper().writeValueAsString(skus);
            session.put(SESSION_KEY, sessionValue);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<String> getSkusFromSession(final Http.Session session) {
        final String skuJsonArrayString = Optional.ofNullable(session.get(SESSION_KEY)).orElse("[]");
        final ArrayNode skusJson = (ArrayNode) Json.parse(skuJsonArrayString);
        final List<String> skus = new LinkedList<>();
        skusJson.elements().forEachRemaining(node -> skus.add(node.asText()));
        return skus;
    }

    public static CompletionStage<List<ProductProjection>> findBySku(final SphereClient sphereClient, final List<String> skus) {
        return  skus.isEmpty()
                ? successful(emptyList())
                : sphereClient.execute(ProductProjectionSearch.ofCurrent()
                .withQueryFilters(product -> product.allVariants().sku().isIn(skus))).thenApply(PagedSearchResult::getResults);
    }

    public static List<ProductProjection> getSortedOnes(final List<ProductProjection> res, final List<String> skus) {
        final Map<String, ProductProjection> skuToProjectionMap = new HashMap<>();
        res.forEach(p -> p.getAllVariants().forEach(v -> {
            if (v.isMatchingVariant()) {
                skuToProjectionMap.put(v.getSku(), p);
            }
        }));
        return skus.stream().map(sku -> skuToProjectionMap.get(sku)).collect(Collectors.toList());
    }
}
