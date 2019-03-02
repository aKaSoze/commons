package fractal.code.data.structures;

import java.util.Objects;

/**
 * Created by sorin.nica in June 2016
 */
public class Tuple<T1, T2> {

    private final T1 first;

    private final T2 second;

    public Tuple(T1 first, T2 second) {
        Objects.requireNonNull(first, "First argument to a tuple cannot be null.");
        Objects.requireNonNull(second, "Second argument to a tuple cannot be null.");
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Tuple<?, ?> otherTuple = (Tuple<?, ?>) other;

        if (!getFirst().equals(otherTuple.getFirst())) return false;
        return getSecond().equals(otherTuple.getSecond());
    }

    @Override
    public int hashCode() {
        return 31 * (getFirst().hashCode() + getSecond().hashCode());
    }

    @Override
    public String toString() {
        return "[" + first + "," + second + "]";
    }
}
