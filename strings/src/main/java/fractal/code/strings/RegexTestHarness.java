package fractal.code.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class RegexTestHarness {

    public static void main(String[] args) throws IOException {

        PatternMatcher patternMatcher = new PatternMatcher();

        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Pattern: ");
            String pattern = br.readLine();
            System.out.print("Input string: ");
            String input = br.readLine();

            List<Match> matches = patternMatcher.match(pattern, input);

            matches.forEach(match ->
                    System.out.println(String.format("I found the text" +
                                    " \"%s\" starting at " +
                                    "index %d and ending at index %d.%n",
                            match.getValue(),
                            match.getStartIndex(),
                            match.getEndIndex())));

            if (matches.isEmpty())
                System.out.println(String.format("No match found.%n"));
        }
    }
}