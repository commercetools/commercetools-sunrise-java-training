import b2bcustomer.B2BCustomerSignUpControllerAction;
import b2bcustomer.B2BCustomerSignUpFormData;
import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.categorytree.CategoryTreeInNewProvider;
import com.commercetools.sunrise.common.categorytree.RefreshableCategoryTree;
import com.commercetools.sunrise.common.models.carts.CartViewModelFactory;
import com.commercetools.sunrise.common.models.carts.MiniCartViewModelFactory;
import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchConfigList;
import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchConfigListProvider;
import com.commercetools.sunrise.contexts.CountryFromSessionProvider;
import com.commercetools.sunrise.contexts.CurrencyFromCountryProvider;
import com.commercetools.sunrise.contexts.LocaleFromUrlProvider;
import com.commercetools.sunrise.framework.controllers.metrics.SimpleMetricsSphereClientProvider;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.cms.FileBasedCmsServiceProvider;
import com.commercetools.sunrise.framework.template.engine.HandlebarsTemplateEngineProvider;
import com.commercetools.sunrise.framework.template.engine.TemplateEngine;
import com.commercetools.sunrise.framework.template.i18n.ConfigurableI18nResolverProvider;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpControllerAction;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import com.commercetools.sunrise.productcatalog.home.viewmodels.HomePageContentFactory;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.sessions.cart.TruncatedMiniCartViewModelFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.neovisionaries.i18n.CountryCode;
import controllers.productcatalog.MyHomePageContentFactory;
import creditcardfee.CartWithCreditCardFeeViewModelFactory;
import creditcardfee.CreditCardFeeCartOperationsControllerComponentSupplier;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientUtils;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.customergroups.queries.CustomerGroupQuery;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import io.sphere.sdk.taxcategories.TaxCategory;
import io.sphere.sdk.taxcategories.queries.TaxCategoryQuery;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        defaultBindings();

        // Bindings for B2B Customer example
        bind(SignUpFormData.class).to(B2BCustomerSignUpFormData.class);
        bind(SignUpControllerAction.class).to(B2BCustomerSignUpControllerAction.class);

        // Bindings for Credit Card Fee example
        bind(CartViewModelFactory.class).to(CartWithCreditCardFeeViewModelFactory.class);
        bind(CartOperationsControllerComponentSupplier.class).to(CreditCardFeeCartOperationsControllerComponentSupplier.class);

        // Put your additional bindings here!
        bind(HomePageContentFactory.class).to(MyHomePageContentFactory.class);
    }

    @Provides
    @RequestScoped
    public DateTimeFormatter dateTimeFormatter(final Locale locale) {
        return DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
    }

    @Provides
    @RequestScoped
    public PriceSelection providePriceSelection(final CurrencyUnit currency, final CountryCode country) {
        return PriceSelection.of(currency)
                .withPriceCountry(country);
    }

    @Provides
    @Singleton
    public CategoryTree provideRefreshableCategoryTree(final SphereClient sphereClient) {
        return RefreshableCategoryTree.of(sphereClient);
    }

    @Provides
    @Singleton
    private ProductTypeLocalRepository provideProductTypeLocalRepository(final SphereClient sphereClient) {
        final ProductTypeQuery query = ProductTypeQuery.of();
        final List<ProductType> productTypes = blockingWait(queryAll(sphereClient, query), 30, TimeUnit.SECONDS);
        return ProductTypeLocalRepository.of(productTypes);
    }

    @Provides
    @Singleton
    @Named("standard")
    public TaxCategory provideTaxCategory(final SphereClient sphereClient) {
        final TaxCategoryQuery query = TaxCategoryQuery.of().byName("standard");
        return SphereClientUtils.blockingWait(sphereClient.execute(query), Duration.ofMinutes(1))
                .head()
                .orElseThrow(() -> new RuntimeException("Tax category \"standard\" missing"));
    }

    @Provides
    @Singleton
    @Named("b2b")
    public CustomerGroup provideCustomerGroup(final SphereClient sphereClient) {
        final CustomerGroupQuery query = CustomerGroupQuery.of().byName("B2B");
        return SphereClientUtils.blockingWait(sphereClient.execute(query), Duration.ofMinutes(1))
                .head()
                .orElseThrow(() -> new RuntimeException("Customer group \"B2B\" missing"));
    }

    private void defaultBindings() {
        bind(SphereClient.class).toProvider(SimpleMetricsSphereClientProvider.class).in(Singleton.class);
        bind(Locale.class).toProvider(LocaleFromUrlProvider.class).in(RequestScoped.class);
        bind(CountryCode.class).toProvider(CountryFromSessionProvider.class).in(RequestScoped.class);
        bind(CurrencyUnit.class).toProvider(CurrencyFromCountryProvider.class).in(RequestScoped.class);
        bind(CmsService.class).toProvider(FileBasedCmsServiceProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(HandlebarsTemplateEngineProvider.class).in(Singleton.class);
        bind(I18nResolver.class).toProvider(ConfigurableI18nResolverProvider.class).in(Singleton.class);
        bind(CategoryTree.class).annotatedWith(Names.named("new")).toProvider(CategoryTreeInNewProvider.class).in(Singleton.class);
        bind(FacetedSearchConfigList.class).toProvider(FacetedSearchConfigListProvider.class).in(Singleton.class);
        bind(MiniCartViewModelFactory.class).to(TruncatedMiniCartViewModelFactory.class);
    }
}
