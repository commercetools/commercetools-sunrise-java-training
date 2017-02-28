package controllers.productcatalog;

import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AuthenticationReverseRouter;
import com.commercetools.sunrise.productcatalog.home.viewmodels.HomePageContent;
import com.commercetools.sunrise.productcatalog.home.viewmodels.HomePageContentFactory;

import javax.inject.Inject;

public class MyHomePageContentFactory extends HomePageContentFactory {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyHomePageContentFactory(final PageTitleResolver pageTitleResolver, final AuthenticationReverseRouter
            authenticationReverseRouter) {
        super(pageTitleResolver);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    protected HomePageContent newViewModelInstance(final Void input) {
        final HomePageContent homePageContent = super.newViewModelInstance(input);
        homePageContent.put("loginUrl", authenticationReverseRouter.logInPageCall());
        return homePageContent;
    }

    @Override
    protected void fillTitle(final HomePageContent viewModel, final Void input) {
        viewModel.setTitle("Home Page");
    }
}
