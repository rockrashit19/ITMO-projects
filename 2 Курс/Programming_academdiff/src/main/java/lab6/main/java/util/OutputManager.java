package lab6.main.java.util;
import java.io.PrintStream;

public class OutputManager {
    private final PrintStream outputStream;

    public OutputManager(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public void print(String message) {
        outputStream.print(message);
    }

    public void println(String message) {
        outputStream.println(message);
    }

    public void printf(String format, Object... args) {
        outputStream.printf(format, args);
    }
}