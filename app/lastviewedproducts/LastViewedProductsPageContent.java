package lastviewedproducts;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.productcatalog.common.ProductListBean;

public class LastViewedProductsPageContent extends PageContent {
    private ProductListBean products;

    public ProductListBean getProducts() {
        return products;
    }

    public void setProducts(final ProductListBean products) {
        this.products = products;
    }
}
