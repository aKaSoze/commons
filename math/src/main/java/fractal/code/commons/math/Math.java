package fractal.code.commons.math;

/**
 * Created by sorin.nica in September 2016
 */
public class Math {

    public static Double getThe4thProportional(Number firstQuantity, Number secondQuantity, Number desiredQuantity) {
        if (firstQuantity.doubleValue() == 0.0)
            throw new IllegalStateException("First quantity cannot be 0.");
        else return (secondQuantity.doubleValue() / firstQuantity.doubleValue()) * desiredQuantity.doubleValue();
    }

}
