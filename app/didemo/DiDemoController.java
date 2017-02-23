package didemo;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateController;
import com.commercetools.sunrise.framework.controllers.WithTemplate;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * InjectionSubject contains kind of a ticket system: each instance that is created from this class (or any subclass)
 * will increment the counter and keep the given number to identify itself.
 *
 * Trying different combinations of Singleton, RequestScoped or no scope (instantiate each time) one can observe
 * how the ticket system reflects the number of instances created within the controller and a component.
 */
@RegisteredComponents(DiDemoController.DemoComponent.class)
public final class DiDemoController extends SunriseTemplateController implements WithTemplate {

    private final InjectionSubject injectionSubject;
    private final SubclassInjectionSubject subclassInjectionSubject;

    @Inject
    public DiDemoController(final TemplateRenderer templateRenderer,
                            final InjectionSubject injectionSubject,
                            final SubclassInjectionSubject subclassInjectionSubject) {
        super(templateRenderer);
        this.injectionSubject = injectionSubject;
        this.subclassInjectionSubject = subclassInjectionSubject;
    }

    //also demo with reload
    @RunRequestStartedHook
    public CompletionStage<Result> show() {
        final DiDemoPage pageContent = new DiDemoPage();
        final List<String> subjects = new LinkedList<>();
        subjects.add("CONTROLLER");
        subjects.add("Class instance ID: " + injectionSubject.getId());
        subjects.add("Subclass instance ID: " + subclassInjectionSubject.getId());
        pageContent.setSubjects(subjects);
        return okResultWithPageContent(pageContent);
    }

    @Override
    public String getTemplateName() {
        return "didemo/show";
    }


    public static final class DemoComponent implements ControllerComponent, PageDataReadyHook {

        private final InjectionSubject injectionSubject;
        private final SubclassInjectionSubject subclassInjectionSubject;

        @Inject
        public DemoComponent(final InjectionSubject injectionSubject,
                             final SubclassInjectionSubject subclassInjectionSubject) {
            this.injectionSubject = injectionSubject;
            this.subclassInjectionSubject = subclassInjectionSubject;
        }

        @Override
        public void onPageDataReady(final PageData pageData) {
            if (pageData.getContent() instanceof DiDemoPage) {
                final DiDemoPage diDemoPage = (DiDemoPage) pageData.getContent();
                final List<String> subjects = diDemoPage.getSubjects();
                subjects.add("COMPONENT");
                subjects.add("Class instance ID: " + injectionSubject.getId());
                subjects.add("Subclass instance ID: " + subclassInjectionSubject.getId());
            }
        }
    }
}
