The learning goal is to use hooks which are not dependent on data stored to the commercetools platform which typically is used to include content from a CMS.
In this exercise a GitHubStreamComponent should be used to show issues from a GitHub repository on the home page.

The result should look like this:

![result](GitHubStreamComponent.png "so it should look like")

The [template](../../conf/templates/components/github/issues.hbs) is already implemented.

The component is already registered for the home controller:

```java
@RequestScoped
public class HomeController extends SunriseHomeController {
    @Inject
    public void setGitHubComponent(final GitHubStreamComponent component) {
        registerControllerComponent(component);
    }
}
```

And is already configured in application.conf

```
GitHubStreamComponent.url = "https://api.github.com/repos/commercetools/commercetools-jvm-sdk/issues"
GitHubStreamComponent.templateName = "components/GitHub/issues"
```

Still missing is the implementation of the hooks in [GitHubStreamComponent](GitHubStreamComponent.java).