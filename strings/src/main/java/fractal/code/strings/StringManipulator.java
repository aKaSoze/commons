package fractal.code.strings;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by sorin.nica in October 2016
 */
public class StringManipulator {

    private static final String SPACE = " ";

    private final String target;

    public StringManipulator(String target) {
        Objects.requireNonNull(target);
        this.target = target;
    }

    public Stream<String> lines() {
        return Arrays.stream(target.split(System.lineSeparator()));
    }

    public Stream<String> words() {
        return lines().flatMap(lines -> Arrays.stream(lines.split(SPACE)));
    }
}
