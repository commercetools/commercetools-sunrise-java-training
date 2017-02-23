package controllers.shoppingcart;

import com.commercetools.sunrise.framework.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.framework.checkout.thankyou.OrderCreatedFinder;
import com.commercetools.sunrise.framework.checkout.thankyou.SunriseCheckoutThankYouController;
import com.commercetools.sunrise.framework.checkout.thankyou.viewmodels.CheckoutThankYouPageContentFactory;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        CheckoutStepControllerComponent.class
})
public final class CheckoutThankYouController extends SunriseCheckoutThankYouController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public CheckoutThankYouController(final TemplateRenderer templateRenderer,
                                      final OrderCreatedFinder orderCreatedFinder,
                                      final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory,
                                      final HomeReverseRouter homeReverseRouter) {
        super(templateRenderer, orderCreatedFinder, checkoutThankYouPageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "checkout-thankyou";
    }

    @Override
    public CompletionStage<Result> handleNotFoundOrderCreated() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
