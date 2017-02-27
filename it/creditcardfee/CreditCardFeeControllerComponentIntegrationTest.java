package creditcardfee;

import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.google.inject.AbstractModule;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.PriceSelection;
import lastviewedproducts.LastViewedProductsControllerComponent;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.WithSphereClient;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static java.util.Collections.emptyMap;

public class CreditCardFeeControllerComponentIntegrationTest extends WithSphereClient {

    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .overrides(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(SphereClient.class).toInstance(sphereClient);
                        bind(Http.Session.class).toInstance(new Http.Session(emptyMap()));
                        bind(PriceSelection.class).toInstance(PriceSelection.of(CURRENCY));
                    }
                }).build();
    }

    private LastViewedProductsControllerComponent component() {
        return app.injector().instanceOf(LastViewedProductsControllerComponent.class);
    }


    @RegisteredComponents(LastViewedProductsControllerComponent.class)
    private static class TestController extends Controller {

        public Result updateCart() {

        }
    }
}