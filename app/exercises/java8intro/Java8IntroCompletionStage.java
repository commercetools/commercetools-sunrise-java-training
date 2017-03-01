package exercises.java8intro;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static exercises.java8intro.Java8IntroFunctions.countChars;
import static exercises.java8intro.Java8IntroFunctions.print;
import static java.lang.Thread.sleep;

// https://drive.google.com/file/d/0B6I3hlPXzkVhUlJKLXZoTTZKaDQ/view
public class Java8IntroCompletionStage {

    public static void main(String[] args) {

        // prints "9"
        executeSlowChameleonRequest()
                .thenApply(countChars)
                .thenAccept(print);

        // prints "chameleon bird"
        executeSlowChameleonRequest()
                .thenAcceptBoth(executeFastBirdRequest(),
                        (text1, text2) -> System.out.println(text1 + " " + text2));

        // prints "bird"
        executeSlowChameleonRequest()
                .acceptEither(executeFastBirdRequest(), text -> System.out.println(text));
    }

    private static CompletionStage<String> executeSlowChameleonRequest() {
        return createCompletionStage(1000L, "chameleon");
    }

    private static CompletionStage<String> executeFastBirdRequest() {
        return createCompletionStage(null, "bird");
    }

    private static CompletionStage<String> createCompletionStage(@Nullable Long sleepMs, final String answer) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            try {
                if (sleepMs != null) {
                    sleep(sleepMs);
                }
                completableFuture.complete(answer);
            } catch (InterruptedException e) {
                completableFuture.completeExceptionally(e);
            }
        }).start();
        return completableFuture;
    }
}
