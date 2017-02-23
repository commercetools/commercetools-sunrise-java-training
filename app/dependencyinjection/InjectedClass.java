package dependencyinjection;

import java.util.concurrent.atomic.AtomicInteger;

class InjectedClass {

    /**
     * This ID generator is shared among all instances of this class
     */
    private static final AtomicInteger instanceIdGenerator = new AtomicInteger(0);

    /**
     * ID, which is incremented the moment a new instance is created
     */
    private final int id = instanceIdGenerator.incrementAndGet();

    int getInstanceId() {
        return id;
    }
}
