package didemo;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

/**
 * InjectionSubject contains kind of a ticket system: each instance that is created from this class (or any subclass)
 * will increment the counter and keep the given number to identify itself.
 *
 * Trying different combinations of Singleton, RequestScoped or no scope (instantiate each time) one can observe
 * how the ticket system reflects the number of instances created within the controller and a component.
 */
@RequestScoped
public final class DiDemoController extends SunriseFrameworkController {

    @Inject
    private InjectionSubject injectionSubject;
    @Inject
    private SubclassInjectionSubject subclassInjectionSubject;

    //also demo with reload
    public CompletionStage<Result> show() {
        return doRequest(() -> {
            final DiDemoPage pageContent = new DiDemoPage();
            final List<String> subjects = new LinkedList<>();
            subjects.add("CONTROLLER");
            subjects.add("Class instance ID: " + injectionSubject.getId());
            subjects.add("Subclass instance ID: " + subclassInjectionSubject.getId());
            pageContent.setSubjects(subjects);
            return asyncOk(renderPageWithTemplate(pageContent, "didemo/show"));
        });
    }

    private static final class DemoComponent implements ControllerComponent, PageDataHook {
        @Inject
        private InjectionSubject injectionSubject;
        @Inject
        private SubclassInjectionSubject subclassInjectionSubject;

        @Override
        public void acceptPageData(final PageData sunrisePageData) {
            if (sunrisePageData.getContent() instanceof DiDemoPage) {
                final DiDemoPage diDemoPage = (DiDemoPage) sunrisePageData.getContent();
                final List<String> subjects = diDemoPage.getSubjects();
                subjects.add("COMPONENT");
                subjects.add("Class instance ID: " + injectionSubject.getId());
                subjects.add("Subclass instance ID: " + subclassInjectionSubject.getId());
            }
        }
    }

    @Inject
    public void setDemoComponent(final DemoComponent component) {
        registerControllerComponent(component);
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("whatever"));
    }
}
