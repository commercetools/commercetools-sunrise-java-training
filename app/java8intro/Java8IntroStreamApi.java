package java8intro;

import java.util.List;

import static java.util.Arrays.asList;
import static java8intro.Java8IntroFunctions.countChars;
import static java8intro.Java8IntroFunctions.print;

// https://drive.google.com/file/d/0B6I3hlPXzkVhUlJKLXZoTTZKaDQ/view
public class Java8IntroStreamApi {

    public static void main(String[] args) {

        final List<String> words = asList("dog", "bird", "chameleon");

        for (String word : words) {
            System.out.println(word.length());
        }

        for (String word : words) {
            print(countChars(word));
        }

        words.stream()
                .map(text -> text.length())
                .forEach(length -> System.out.println(length));

        words.stream()
                .map(String::length)
                .forEach(System.out::println);

        words.stream()
                .map(countChars)
                .forEach(print);

    }
}
