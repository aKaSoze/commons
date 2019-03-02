package fractal.code.data.structures;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Created by sorin.nica in September 2016
 */
@Slf4j
public class IdentityUtils {

    public static <T> Boolean equals(Object first, Object second, BiFunction<T, T, Boolean> yourEquals) {
        log.debug(String.format("Checking equality for objects: %s =? %s", first, second));
        if (first == second) return true;
        if (first == null || second == null || first.getClass() != second.getClass()) return false;

        Objects.requireNonNull(yourEquals);
        return yourEquals.apply((T) first, (T) second);
    }


}
