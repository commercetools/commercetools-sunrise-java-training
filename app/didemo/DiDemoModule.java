package didemo;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.google.inject.AbstractModule;

public class DiDemoModule extends AbstractModule {
    @Override
    protected void configure() {
//        bind(InjectionSubject.class).in(RequestScoped.class);
    }
}
