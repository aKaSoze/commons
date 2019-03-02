package fractal.code.io;

import java.io.IOException;

public interface IOFunction<T> {

    T calculate() throws IOException;

}
