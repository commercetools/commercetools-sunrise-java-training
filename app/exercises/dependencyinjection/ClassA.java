package exercises.dependencyinjection;

import javax.inject.Inject;

class ClassA {

    private final InjectedClass injectedClass;

    @Inject
    public ClassA(final InjectedClass injectedClass) {
        this.injectedClass = injectedClass;
    }

    int getInstanceId() {
        return injectedClass.getInstanceId();
    }
}
