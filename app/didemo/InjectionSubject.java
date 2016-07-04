package didemo;

import io.sphere.sdk.models.Base;

import java.util.concurrent.atomic.AtomicInteger;

public class InjectionSubject extends Base {
    private static final AtomicInteger instanceIdGenerator = new AtomicInteger(0);
    private final int id = instanceIdGenerator.incrementAndGet();

    public InjectionSubject() {
    }

    public int getId() {
        return id;
    }
}
