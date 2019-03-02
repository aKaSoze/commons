package fractal.code.io;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by sorin.nica in October 2016
 */
@Slf4j
public class FileStream {

    private final FileReader fileReader;

    private final FileWriter fileWriter;

    private Stream<String> lines;

    public FileStream(String sourceFile, String destinationFile) {
        fileReader = new FileReader(sourceFile);
        fileWriter = new FileWriter(destinationFile);
        lines = fileReader.lines();
    }

    public FileStream mapLine(Function<String, String> lineTransformation) {
        lines = lines.map(lineTransformation);
        return this;
    }

    public void write() {
        log.info("Writing file: " + fileWriter.getFile().getAbsolutePath());
        lines.forEach(fileWriter::writeLine);
        fileWriter.close();
        fileReader.close();
    }

}
