package dependencyinjection;

import javax.inject.Inject;

class ClassB {

    private final InjectedClass injectedClass;

    @Inject
    public ClassB(final InjectedClass injectedClass) {
        this.injectedClass = injectedClass;
    }

    int getInstanceId() {
        return injectedClass.getInstanceId();
    }
}
