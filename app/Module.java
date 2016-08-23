import bulkygoods.BulkyGoodsComponent;
import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import com.commercetools.sunrise.common.httpauth.basic.BasicAuthenticationProvider;
import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.common.pages.DefaultPageNavMenuControllerComponent;
import com.commercetools.sunrise.common.template.cms.FileBasedCmsServiceProvider;
import com.commercetools.sunrise.common.template.engine.HandlebarsTemplateEngineProvider;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.ConfigurableI18nResolverProvider;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolverBuilder;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import com.commercetools.sunrise.shoppingcart.MiniCartControllerComponent;
import com.commercetools.sunrise.shoppingcart.common.CheckoutCommonComponent;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.sphere.sdk.utils.MoneyImpl;
import lastviewedproducts.LastViewedProductsComponent;
import models.ShopCartBeanFactory;

import javax.money.Monetary;
import javax.money.format.MonetaryFormats;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {
    @Override
    protected void configure() {
        applyJavaMoneyHack();
        bind(CartLikeBeanFactory.class).to(ShopCartBeanFactory.class);//used by bulky goods component
        bind(CmsService.class).toProvider(FileBasedCmsServiceProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(HandlebarsTemplateEngineProvider.class).in(Singleton.class);
        bind(I18nResolver.class).toProvider(ConfigurableI18nResolverProvider.class).in(Singleton.class);
        bind(HttpAuthentication.class).toProvider(BasicAuthenticationProvider.class).in(Singleton.class);
    }

    @Provides
    public MultiControllerComponentResolver multiControllerComponentResolver() {
        //here are also instanceof checks possible
        return new MultiControllerComponentResolverBuilder()
                .add(CheckoutCommonComponent.class, controller -> controller.getFrameworkTags().contains("checkout"))
                .add(MiniCartControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(DefaultPageNavMenuControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(LocationSelectorControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(LastViewedProductsComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(BulkyGoodsComponent.class, controller -> true)
                .build();
    }

    private void applyJavaMoneyHack() {
        //fixes https://github.com/commercetools/commercetools-sunrise-java/issues/404
        //exception play.api.http.HttpErrorHandlerExceptions$$anon$1: Execution exception[[CompletionException: java.lang.IllegalArgumentException: java.util.concurrent.CompletionException: io.sphere.sdk.json.JsonException: detailMessage: com.fasterxml.jackson.databind.JsonMappingException: Operator failed: javax.money.DefaultMonetaryRoundingsSingletonSpi$DefaultCurrencyRounding@1655879e (through reference chain: io.sphere.sdk.payments.PaymentDraftImpl["amountPlanned"])
        Monetary.getDefaultRounding();
        Monetary.getDefaultRounding().apply(MoneyImpl.ofCents(123, "EUR"));
        Monetary.getDefaultAmountType();
        MonetaryFormats.getDefaultFormatProviderChain();
        Monetary.getDefaultCurrencyProviderChain();
    }
}
