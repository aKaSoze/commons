package fractal.code.io;

import java.io.IOException;

@FunctionalInterface
public interface IOAction {

    void doIOAction() throws IOException;

}
