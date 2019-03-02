package fractal.code.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sorin.nica in October 2016
 */
public class PatternMatcher {

//    <([{\^-=$!|]})?*+.>

//    There are two ways to force a metacharacter to be treated as an ordinary character:
//
//    precede the metacharacter with a backslash, or
//    enclose it within \Q (which starts the quote) and \E (which ends it).
//    When using this technique, the \Q and \E can be placed at any location within the expression, provided that the \Q comes first.

    public List<Match> match(String regExp, CharSequence input) {
        Objects.requireNonNull(regExp);
        Objects.requireNonNull(input);

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(input);

        List<Match> matches = new ArrayList<>();
        while (matcher.find()) matches.add(new Match(matcher.group(), matcher.start(), matcher.end()));
        return matches;
    }

}
