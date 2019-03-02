package fractal.code.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

/**
 * Created by sorin.nica in August 2016
 */
public class FileWriter {

    private final Path absolutePath;

    private final File file;

    private final PrintWriter printWriter;

    public FileWriter(String absolutePath, Boolean useCompression) {
        try {
            this.absolutePath = Paths.get(absolutePath);
            file = new File(absolutePath);
            if (!file.getParentFile().exists()) Files.createDirectories(this.absolutePath.getParent());

            OutputStream outputStream = new FileOutputStream(absolutePath);
            if (useCompression) outputStream = new GZIPOutputStream(outputStream);
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);
            printWriter = new PrintWriter(outputStreamWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileWriter(String absolutePath) {
        this(absolutePath, false);
    }

    public void write(String text) {
        printWriter.print(text);
    }

    public void writeLine(String line) {
        printWriter.println(line);
    }

    public File getFile() {
        return file;
    }

    public void delete() {
        file.delete();
    }

    public void close() {
        printWriter.flush();
        printWriter.close();
    }
}
