package fractal.code.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by sorin.nica in August 2016
 */
public class FileReader {

    private final File file;

    private final BufferedReader bufferedReader;

    public FileReader(String absolutePath) {
        try {
            file = new File(absolutePath);
            if (!file.isFile()) throw new IllegalArgumentException("File " + absolutePath + " does not exist.");
            bufferedReader = new BufferedReader(new java.io.FileReader(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readAsString() {
        return bufferedReader.lines().reduce((line1, line2) -> line1 + line2).orElse("");
    }

    public Stream<String> lines() {
        return bufferedReader.lines();
    }

    public void close() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
