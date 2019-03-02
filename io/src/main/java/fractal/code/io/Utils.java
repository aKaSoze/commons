package fractal.code.io;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class Utils {

    public static void uncheckedIO(IOAction ioAction) {
        try {
            ioAction.doIOAction();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T uncheckedIO(IOFunction<? extends T> ioFunction) {
        try {
            return ioFunction.calculate();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
