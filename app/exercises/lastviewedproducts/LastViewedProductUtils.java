package exercises.lastviewedproducts;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.sphere.sdk.products.ProductProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Http;

import java.util.*;

import static java.util.stream.Collectors.toList;

final class LastViewedProductUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LastViewedProductsControllerComponent.class);
    private static final String SESSION_KEY = "lastViewedProductsSku";
    private static final int MAX_CAPACITY = 4;


    private LastViewedProductUtils() {
    }

    static void saveSkuInSession(final Http.Session session, final String sku) {
        final List<String> skuList = findLastViewedProductsSkuInSession(session);
        skuList.remove(sku);
        skuList.add(sku);
        while (skuList.size() > MAX_CAPACITY) {
            skuList.remove(0);
        }
        try {
            session.put(SESSION_KEY, Json.mapper().writeValueAsString(skuList));
            LOGGER.debug("Saved product with SKU {} to session: {}", sku, skuList);
        } catch (JsonProcessingException e) {
            LOGGER.error("Could not serialize SKU list {} into JSON", skuList, e);
        }
    }

    static List<String> findLastViewedProductsSkuInSession(final Http.Session session) {
        final List<String> skuList = new LinkedList<>();
        Json.parse(session.getOrDefault(SESSION_KEY, "[]")).elements()
                .forEachRemaining(node -> skuList.add(node.asText()));
        return skuList;
    }

    static List<ProductProjection> sortProductsBySkuList(final List<ProductProjection> products, final List<String> skuList) {
        final Map<String, ProductProjection> skuToProductMap = convertToSkuToProductMap(products);
        return skuList.stream()
                .map(skuToProductMap::get)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private static Map<String, ProductProjection> convertToSkuToProductMap(final List<ProductProjection> products) {
        final Map<String, ProductProjection> skuToProjectionMap = new HashMap<>();
        products.forEach(product -> {
            final String sku = product.findFirstMatchingVariant().orElseGet(product::getMasterVariant).getSku();
            skuToProjectionMap.put(sku, product);
        });
        return skuToProjectionMap;
    }
}
