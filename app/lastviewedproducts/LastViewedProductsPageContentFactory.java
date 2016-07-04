package lastviewedproducts;

import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;

public class LastViewedProductsPageContentFactory extends Base {
    @Inject
    private ProductListBeanFactory productListBeanFactory;

    public LastViewedProductsPageContent create(final Iterable<ProductProjection> productProjections) {
        final LastViewedProductsPageContent bean = new LastViewedProductsPageContent();
        bean.setProducts(productListBeanFactory.create(productProjections));
        return bean;
    }
}
