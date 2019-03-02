package fractal.code.commons.math;

import java.util.Objects;

/**
 * Created by sorin.nica in October 2016
 */
public class Percentage {

    private static final Long LOWER_BOUND = 0L;

    private static final Long UPPER_BOUND = 100L;

    private final Number value;

    public Percentage(Number value) {
        Objects.requireNonNull(value);

        if (value.doubleValue() < LOWER_BOUND)
            throw new IllegalArgumentException("A percentage cannot be negative.");

        this.value = value;
    }

    public Double applyTo(Number whole) {
        return (value.doubleValue() / UPPER_BOUND) * whole.doubleValue();
    }
}
