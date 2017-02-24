package dependencyinjection;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class contains a ticketing system: each instance that is created will receive a unique incremental identifier.
 *
 * Trying different combinations of {@link @Singleton}, {@link @RequestScoped} or defining no scope
 * one can observe when a new instance of this class is created.
 */
class InjectedClass {

    /**
     * This ID generator is shared among all instances of this class
     */
    private static final AtomicInteger instanceIdGenerator = new AtomicInteger(0);

    /**
     * ID assigned to each instance, which is incremented the moment a new instance is created
     */
    private final int id = instanceIdGenerator.incrementAndGet();

    int getInstanceId() {
        return id;
    }
}
