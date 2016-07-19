package didemo;

import io.sphere.sdk.models.Base;

import java.util.concurrent.atomic.AtomicInteger;

public class InjectionSubject extends Base {
    private static final AtomicInteger instanceIdGenerator = new AtomicInteger(0); // shared among all instances of InjectionSubject (and subclasses)
    private final int id = instanceIdGenerator.incrementAndGet(); // incremented the moment the instance is created

    public InjectionSubject() {
    }

    public int getId() {
        return id;
    }
}
