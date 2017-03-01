package java8intro;

import java.util.Random;
import java.util.function.*;

// https://drive.google.com/file/d/0B6I3hlPXzkVhUlJKLXZoTTZKaDQ/view
public class Java8IntroFunctions {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {

    }

    // Function
    static Function<String, Integer> countChars = text -> text.length();

    static int countChars(String text) {
        return text.length();
    }

    // BiFunction
    static BiFunction<Integer, Integer, Integer> sumNumbers = (n1, n2) -> n1 + n2;

    static int sumNumbers(int n1, int n2) {
        return n1 + n2;
    }

    // Consumer
    static Consumer<Integer> print = n -> System.out.println(n);

    static void print(Integer n) {
        System.out.println(n);
    }

    // Supplier
    static Supplier<Integer> generateRandomNumber = () -> RANDOM.nextInt();

    static int generateRandomNumber() {
        return RANDOM.nextInt();
    }

    // Runnable
    static Runnable printRandomNumber = () -> System.out.println(RANDOM.nextInt());

    static void printRandomNumber() {
        System.out.println(RANDOM.nextInt());
    }

    // Predicate
    static Predicate<Integer> isEvenNumber = n -> n % 2 == 0;

    static boolean isEvenNumber(int n) {
        return n % 2 == 0;
    }
}
