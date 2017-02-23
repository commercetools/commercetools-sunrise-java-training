package dependencyinjection;

import javax.inject.Inject;

public class ClassB {

    private final InjectedClass injectedClass;

    @Inject
    public ClassB(final InjectedClass injectedClass) {
        this.injectedClass = injectedClass;
    }

    int getInstanceId() {
        return injectedClass.getInstanceId();
    }
}
