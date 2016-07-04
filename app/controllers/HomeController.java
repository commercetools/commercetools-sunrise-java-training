package controllers;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import githubstream.GitHubStreamComponent;

import javax.inject.Inject;

@RequestScoped
public class HomeController extends SunriseHomeController {
    @Inject
    public void setGitHubComponent(final GitHubStreamComponent component) {
        registerControllerComponent(component);
    }
}
