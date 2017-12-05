package exercises.dependencyinjection;

import com.commercetools.sunrise.framework.controllers.SunriseContentController;
import com.commercetools.sunrise.framework.controllers.WithContent;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import common.BlankPageContent;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Access http://localhost:9000/dependencyinjection
 */
public final class DependencyInjectionController extends SunriseContentController implements WithContent {

    private final ClassA classA;
    private final ClassB classB;

    @Inject
    public DependencyInjectionController(final ContentRenderer contentRenderer,
                                         final ClassA classA,
                                         final ClassB classB) {
        super(contentRenderer);
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
        return "exercises/dependency-injection";
    }
}
