package ru.rmntim.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class CsvWriter {
    private final FileWriter writer;

    public CsvWriter(String filePath, String separator) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым.");
        }
        if (separator == null || separator.isEmpty()) {
            throw new IllegalArgumentException("Разделитель не может быть пустым.");
        }
        this.writer = new FileWriter(filePath, false);
    }

    public void write(double x, double result, String customSeparator, int afterDecimal) throws IOException {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumIntegerDigits(1);
        df.setMinimumFractionDigits(afterDecimal);
        df.setGroupingUsed(false);

        String xVal = df.format(x);
        String resultVal = df.format(result);
        writer.append(xVal)
                .append(customSeparator)
                .append(resultVal)
                .append(System.lineSeparator());

//        DecimalFormat df = new DecimalFormat("#." + "0".repeat(Math.max(0, afterDecimal)));
    }

    public void close() throws IOException{
        writer.close();
    }
}