package lastviewedproducts;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@RequestScoped
public final class LastViewedProductsController extends SunriseFrameworkController {

    public CompletionStage<Result> show() {
        return doRequest(() -> {
            final LastViewedProductsPageContent lastViewedProductsPageContent = new LastViewedProductsPageContent();
            return asyncOk(renderPageWithTemplate(lastViewedProductsPageContent, "lastviewedproducts/show"));
        });
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("product-history"));
    }
}
