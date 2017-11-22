import cms.contentful.ContentfulCmsServiceProvider;
import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.ctp.categories.CachedCategoryTreeProvider;
import com.commercetools.sunrise.ctp.categories.CategoriesSettings;
import com.commercetools.sunrise.ctp.categories.NavigationCategoryTree;
import com.commercetools.sunrise.ctp.categories.NewCategoryTree;
import com.commercetools.sunrise.email.EmailSender;
import com.commercetools.sunrise.framework.controllers.metrics.SimpleMetricsSphereClientProvider;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.localization.CountryFromSessionProvider;
import com.commercetools.sunrise.framework.localization.CurrencyFromCountryProvider;
import com.commercetools.sunrise.framework.localization.LocaleFromUrlProvider;
import com.commercetools.sunrise.framework.template.cms.FileBasedCmsServiceProvider;
import com.commercetools.sunrise.framework.template.engine.HandlebarsTemplateEngineProvider;
import com.commercetools.sunrise.framework.template.engine.TemplateEngine;
import com.commercetools.sunrise.framework.template.i18n.ConfigurableI18nResolverProvider;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.carts.MiniCartViewModelFactory;
import com.commercetools.sunrise.httpauth.HttpAuthentication;
import com.commercetools.sunrise.httpauth.basic.BasicAuthenticationProvider;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpControllerAction;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinderByCategoryWithMatchingVariants;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.AlphabeticallySortedTermFacetViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.CustomSortedTermFacetViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.TermFacetViewModelFactory;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.sessions.cart.TruncatedMiniCartViewModelFactory;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.neovisionaries.i18n.CountryCode;
import email.smtp.EmailSenderProvider;
import exercises.b2bcustomer.B2BCustomerSignUpControllerAction;
import exercises.b2bcustomer.B2BCustomerSignUpFormData;
import exercises.creditcardfee.CartWithCreditCardFeeViewModelFactory;
import exercises.creditcardfee.CreditCardFeeCartOperationsControllerComponentSupplier;
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
import static java.util.Collections.emptyList;

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
        // Binding for the client to connect commercetools
        bind(SphereClient.class)
                .toProvider(SimpleMetricsSphereClientProvider.class)
                .in(Singleton.class);
        //bind(SphereClient.class).to(MySphereClient.class).in(Singleton.class);

        // Binding for the HTTP Authentication
        bind(HttpAuthentication.class)
                .toProvider(BasicAuthenticationProvider.class)
                .in(Singleton.class);

        // Binding for category tree
        bind(CategoryTree.class).toProvider(CachedCategoryTreeProvider.class);

        // Binding for all template related, such as the engine, CMS and i18n
        bind(TemplateEngine.class)
                .toProvider(HandlebarsTemplateEngineProvider.class)
                .in(Singleton.class);
        bind(I18nResolver.class)
                .toProvider(ConfigurableI18nResolverProvider.class)
                .in(Singleton.class);

        // Bindings fo email sender
        bind(EmailSender.class)
                .toProvider(EmailSenderProvider.class)
                .in(Singleton.class);

        // Bindings for all user context related
        bind(Locale.class)
                .toProvider(LocaleFromUrlProvider.class)
                .in(RequestScoped.class);
        bind(CountryCode.class)
                .toProvider(CountryFromSessionProvider.class)
                .in(RequestScoped.class);
        bind(CurrencyUnit.class)
                .toProvider(CurrencyFromCountryProvider.class)
                .in(RequestScoped.class);

        // Bindings for the configured faceted search mappers
        bind(TermFacetViewModelFactory.class)
                .annotatedWith(Names.named("alphabeticallySorted"))
                .to(AlphabeticallySortedTermFacetViewModelFactory.class)
                .in(RequestScoped.class);
        bind(TermFacetViewModelFactory.class)
                .annotatedWith(Names.named("customSorted"))
                .to(CustomSortedTermFacetViewModelFactory.class)
                .in(RequestScoped.class);

        // Binding to truncate mini cart to fit it into limited session space
        bind(MiniCartViewModelFactory.class).to(TruncatedMiniCartViewModelFactory.class);

        // Binding to enable matching variants on listing products
        // IMPORTANT: comment the following line if your project does not require this functionality, leaving it on can severely affect performance
        bind(ProductListFinder.class).to(ProductListFinderByCategoryWithMatchingVariants.class);

        // Bindings for B2B Customer example
        bind(SignUpFormData.class).to(B2BCustomerSignUpFormData.class);
        bind(SignUpControllerAction.class).to(B2BCustomerSignUpControllerAction.class);

        // Bindings for Credit Card Fee example
        bind(CartViewModelFactory.class).to(CartWithCreditCardFeeViewModelFactory.class);
        bind(CartOperationsControllerComponentSupplier.class).to(CreditCardFeeCartOperationsControllerComponentSupplier.class);

        // Provide here your own bindings
    }

    @Provides
    @RequestScoped
    @NavigationCategoryTree
    private CategoryTree provideNavigationCategoryTree(final CategoriesSettings categoriesSettings, final CategoryTree categoryTree) {
        return categoriesSettings.navigationExternalId()
                .flatMap(categoryTree::findByExternalId)
                .map(categoryTree::findChildren)
                .map(categoryTree::getSubtree)
                .orElse(categoryTree);
    }

    @Provides
    @RequestScoped
    @NewCategoryTree
    private CategoryTree provideNewCategoryTree(final CategoriesSettings categoriesSettings, final CategoryTree categoryTree) {
        return categoriesSettings.newExtId()
                .flatMap(categoryTree::findByExternalId)
                .map(categoryTree::findChildren)
                .map(categoryTree::getSubtree)
                .orElseGet(() -> CategoryTree.of(emptyList()));
    }

    @Provides
    @Singleton
    private ProductTypeLocalRepository fetchProductTypeLocalRepository(final SphereClient sphereClient) {
        final ProductTypeQuery query = ProductTypeQuery.of();
        final List<ProductType> productTypes = blockingWait(queryAll(sphereClient, query), 1, TimeUnit.MINUTES);
        return ProductTypeLocalRepository.of(productTypes);
    }

    @Provides
    @RequestScoped
    public DateTimeFormatter dateTimeFormatter(final Locale locale) {
        return DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
    }

    @Provides
    @RequestScoped
    public PriceSelection providePriceSelection(final CurrencyUnit currency, final CountryCode country,
                                                final CustomerInSession customerInSession) {
        return PriceSelection.of(currency)
                .withPriceCountry(country)
                .withPriceCustomerGroupId(customerInSession.findCustomerGroupId().orElse(null));
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

    /**
     * Provides the CMS Service for Contentful if the configuration is provided, otherwise provides a simple File-Based CMS.
     * If you do not plan on using Contentful, please remove the dependency from {@code build.sbt}, the file
     * {@link ContentfulCmsServiceProvider} and directly call the {@link FileBasedCmsServiceProvider} in the code below.
     * @return the CMS Service used, either Contentful if configured or a simple file based CMS
     */
    @Provides
    @Singleton
    public CmsService provideCmsService(final ContentfulCmsServiceProvider contentfulCmsServiceProvider,
                                        final FileBasedCmsServiceProvider fileBasedCmsServiceProvider) {
        try {
            return contentfulCmsServiceProvider.get();
        } catch (SunriseConfigurationException e) {
            return fileBasedCmsServiceProvider.get();
        }
    }
}
