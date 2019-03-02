package fractal.code.translate;

import fractal.code.data.structures.Tuple;
import fractal.code.strings.StringManipulator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sorin.nica in October 2016
 */
public class TranslationComparator {

    private static final String ORIGINAL = "Doina 11/11/1987 se plimba prin casa 1987 de 32 de ani. Azi este 2016 11 si o zi anume. In buletin Doinita are seria RX/234/98-09Z.";

    private static final String TRANSALATION = "Doina 11/17/1987 wander through the 1977 house for 32 years. Today is a particular day in 2016 11 . The RX series has Doinita bulletin RX/234/98-09Z.";


    private static final Set<String> NUMBERS = new HashSet<>();

    static {
        NUMBERS.add("0");
        NUMBERS.add("1");
        NUMBERS.add("2");
        NUMBERS.add("3");
        NUMBERS.add("4");
        NUMBERS.add("5");
        NUMBERS.add("6");
        NUMBERS.add("7");
        NUMBERS.add("8");
        NUMBERS.add("9");
    }


    public ComparisonResult compare(String text, String translatedText) {
        StringManipulator textManipulator = new StringManipulator(text);
        StringManipulator translationManipulator = new StringManipulator(translatedText);

        List<String> originalWords = textManipulator.words().filter(this::wordContainsNumber)
                .collect(Collectors.toList());
        List<String> translatedWords = translationManipulator.words().filter(this::wordContainsNumber)
                .collect(Collectors.toList());

        List<Tuple<String, String>> nonMatchingWords = new ArrayList<>();
        for (String word : originalWords) {
            String translatedWord = translatedWords.get(originalWords.indexOf(word));
            if (!word.equals(translatedWord)) nonMatchingWords.add(new Tuple<>(word, translatedWord));
        }

        return new ComparisonResult(nonMatchingWords);
    }


    private Boolean wordContainsNumber(String word) {
        for (String number : NUMBERS) if (word.contains(number)) return true;
        return false;
    }

    public static void main(String[] args) {
        TranslationComparator translationComparator = new TranslationComparator();
        System.out.println(translationComparator.compare(ORIGINAL, TRANSALATION));
    }


}
