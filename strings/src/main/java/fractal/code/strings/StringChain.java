package fractal.code.strings;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sorin.nica in June 2016
 */
public class StringChain implements Iterable<String> {

    private static final String EMPTY = "";

    private final LinkedList<String> parts = new LinkedList<>();

    private final String separator;

    public static StringChain of(String source, String separator) {
        return new StringChain(source, separator);
    }

    public StringChain(String source, String separator) {
        parts.addAll(Arrays.asList(source.split(separator)));
        this.separator = separator.replace("\\", "");
    }

    public StringChain(String separator, String... parts) {
        this.separator = separator;
        this.parts.addAll(Arrays.asList(parts));
    }

    public StringChain(Stream<String> parts, String separator) {
        this.separator = separator;
        parts.forEach(this.parts::add);
    }

    public String subtractLast() {
        return parts.pollLast();
    }

    public String subtractFirst() {
        return parts.pollFirst();
    }

    @Override
    public String toString() {
        return parts.stream().reduce((part1, part2) -> part1 + separator + part2).orElse(EMPTY);
    }

    public StringChain subChain(Integer partIndex, String separator) {
        return of(getPart(partIndex), separator);
    }

    public StringChain subChain(Integer... partsIndexes) {
        return new StringChain(Arrays.stream(partsIndexes).map(this::getPart), separator);
    }

    public String getPart(Integer partIndex) {
        if (parts.size() > partIndex) return parts.get(partIndex);
        else return EMPTY;
    }

    public List<String> asList() {
        return new ArrayList<>(parts);
    }

    public Map<String, String> toPairs(String separator) {
        return parts.stream()
                .map(part -> StringChain.of(part, separator))
                .collect(Collectors.toMap(stringChain -> stringChain.getPart(0), stringChain -> stringChain.getPart(1)));
    }

    @Override
    public Iterator<String> iterator() {
        return parts.iterator();
    }

    @Override
    public Spliterator<String> spliterator() {
        return parts.spliterator();
    }
}
