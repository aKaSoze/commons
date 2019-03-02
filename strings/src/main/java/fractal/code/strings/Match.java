package fractal.code.strings;

/**
 * Created by sorin.nica in October 2016
 */
public class Match {

    private final String value;

    private final Integer startIndex;

    private final Integer endIndex;

    public Match(String value, Integer startIndex, Integer endIndex) {
        this.value = value;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getValue() {
        return value;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }
}
