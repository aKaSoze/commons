package fractal.code.translate;

import fractal.code.data.structures.Tuple;

import java.util.List;

/**
 * Created by sorin.nica in October 2016
 */
public class ComparisonResult {

    private final List<Tuple<String, String>> nonMatchingNumberWords;

    public ComparisonResult(List<Tuple<String, String>> nonMatchingNumberWords) {
        this.nonMatchingNumberWords = nonMatchingNumberWords;
    }

    @Override
    public String toString() {
        return nonMatchingNumberWords.stream()
                .map(tuple -> tuple.getFirst() + " -> " + tuple.getSecond())
                .reduce((pair1, pair2) -> pair1 + System.lineSeparator() + pair2)
                .orElse("");
    }
}
