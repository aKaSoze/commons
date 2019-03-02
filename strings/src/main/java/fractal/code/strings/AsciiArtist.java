package fractal.code.strings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sorin.nica in October 2016
 */
public class AsciiArtist {


    private Long width;

    private Long currentHeight;

    private String content;

    public void addHeader(String header) {
        Long startPosition = (width - header.length()) / 2;

        multiplyString(Constants.SPACE, startPosition);
        content += header;
        feedLine();
    }

    public void addColumns(List<List<String>> columns) {
        List<Integer> lengths = columns.stream()
                .map(this::maxLengthOfStrings)
                .collect(Collectors.toList());

    }

    public void feedLine() {
        content += System.lineSeparator();
    }

    public void multiplyString(String value, Long multiplier) {
        for (int counter = 0; counter < multiplier; counter++)
            content += value;
    }

    private Integer maxLengthOfStrings(List<String> strings) {
        return strings.stream().mapToInt(String::length).max().orElse(0);
    }
}
