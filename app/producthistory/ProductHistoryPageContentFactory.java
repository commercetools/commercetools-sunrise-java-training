package producthistory;

import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;

public class ProductHistoryPageContentFactory extends Base {
    @Inject
    private ProductListBeanFactory productListBeanFactory;

    public ProductHistoryPageContent create(final Iterable<ProductProjection> productProjections) {
        final ProductHistoryPageContent bean = new ProductHistoryPageContent();
        bean.setProducts(productListBeanFactory.create(productProjections));
        return bean;
    }
}
