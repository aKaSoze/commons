package fractal.code.translate;

/**
 * Created by sorin.nica in October 2016
 */
public class TextStats {

    private final Long lines;

    private final Long words;

    private final Long nonWhiteSpaceChars;

    public TextStats(Long lines, Long words, Long nonWhiteSpaceChars) {
        this.lines = lines;
        this.words = words;
        this.nonWhiteSpaceChars = nonWhiteSpaceChars;
    }

    public Long getLines() {
        return lines;
    }

    public Long getWords() {
        return words;
    }

    public Long getNonWhiteSpaceChars() {
        return nonWhiteSpaceChars;
    }
}
