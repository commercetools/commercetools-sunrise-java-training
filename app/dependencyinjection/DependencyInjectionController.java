package dependencyinjection;

import com.commercetools.sunrise.framework.controllers.SunriseTemplateController;
import com.commercetools.sunrise.framework.controllers.WithTemplate;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import common.BlankPageContent;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class DependencyInjectionController extends SunriseTemplateController implements WithTemplate {

    private final ClassA classA;
    private final ClassB classB;

    @Inject
    public DependencyInjectionController(final TemplateRenderer templateRenderer,
                                         final ClassA classA,
                                         final ClassB classB) {
        super(templateRenderer);
        this.classA = classA;
        this.classB = classB;
    }

    public CompletionStage<Result> show() {
        final BlankPageContent pageContent = new BlankPageContent();
        pageContent.put("instanceIdInClassA", classA.getInstanceId());
        pageContent.put("instanceIdInClassB", classB.getInstanceId());
        return okResultWithPageContent(pageContent);
    }

    @Override
    public String getTemplateName() {
        return "dependencyinjection/show";
    }
}
