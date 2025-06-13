package ru.rmntim.check;

import ru.rmntim.system.LogarithmicSystemPart;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static ru.rmntim.functions.log.Ln.ln;
import static ru.rmntim.functions.log.Log10.log10;
import static ru.rmntim.functions.log.Log2.log2;
import static ru.rmntim.functions.log.Log3.log3;
import static ru.rmntim.functions.log.Log5.log5;

public class CheckSecondFunc {
    public static void generateForSecondEquation(String PDIR, List<Double> xValuesForLog) throws IOException {
        try (FileWriter w = new FileWriter(PDIR +"testLogWithAllStubs.csv")) {
            w.write("x;ln;log2;log3;log5;log10;expected\n");
            for (double x : xValuesForLog) {
                double ln = ln(x);
                double log2 = log2(x);
                double log3 = log3(x);
                double log5 = log5(x);
                double log10 = log10(x);
                double exp = LogarithmicSystemPart.calculate2(x);
                if (Double.isNaN(exp) | !Double.isFinite(exp)) {
                    continue;
                }
                w.write(String.format(Locale.US,
                        "%.8f;%.8f;%.8f;%.8f;%.8f;%.8f;%.8f%n",
                        x, ln, log2, log3, log5, log10, exp));
            }
        }

        try (FileWriter w = new FileWriter(PDIR +"testLogWithAllFunc.csv")) {
            w.write("x;expected\n");
            for (double x : xValuesForLog) {
                double exp = LogarithmicSystemPart.calculate2(x);
                if (Double.isNaN(exp) | !Double.isFinite(exp)) {
                    continue;
                }
                w.write(String.format(Locale.US,
                        "%.8f;%.8f%n",
                        x, exp));
            }
        }

        try (FileWriter w = new FileWriter(PDIR +"testLogWithLog2Log3Log5Log10Stubs.csv")) {
            w.write("x;ln;expected\n");
            for (double x : xValuesForLog) {
                double ln = ln(x);
                double exp = LogarithmicSystemPart.calculate2(x);
                if (Double.isNaN(exp) | !Double.isFinite(exp)) {
                    continue;
                }
                w.write(String.format(Locale.US,
                        "%.8f;%.8f;%.8f%n",
                        x, ln, exp));
            }
        }

        try (FileWriter w = new FileWriter(PDIR +"testLogWithLog3Log5Log10Stubs.csv")) {
            w.write("x;ln;log2;expected\n");
            for (double x : xValuesForLog) {
                double ln = ln(x);
                double log2 = log2(x);
                double exp = LogarithmicSystemPart.calculate2(x);
                if (Double.isNaN(exp) | !Double.isFinite(exp)) {
                    continue;
                }
                w.write(String.format(Locale.US,
                        "%.8f;%.8f;%.8f;%.8f%n",
                        x, ln, log2, exp));
            }
        }

        try (FileWriter w = new FileWriter(PDIR +"testLogWithLog5Log10Stubs.csv")) {
            w.write("x;ln;log2;log3;expected\n");
            for (double x : xValuesForLog) {
                double ln = ln(x);
                double log2 = log2(x);
                double log3 = log3(x);
                double exp = LogarithmicSystemPart.calculate2(x);
                if (Double.isNaN(exp) | !Double.isFinite(exp)) {
                    continue;
                }
                w.write(String.format(Locale.US,
                        "%.8f;%.8f;%.8f;%.8f;%.8f%n",
                        x, ln, log2, log3, exp));
            }
        }

        try (FileWriter w = new FileWriter(PDIR +"testLogWithLog10Stubs.csv")) {
            w.write("x;ln;log2;log3;log5;expected\n");
            for (double x : xValuesForLog) {
                double ln = ln(x);
                double log2 = log2(x);
                double log3 = log3(x);
                double log5 = log5(x);
                double exp = LogarithmicSystemPart.calculate2(x);
                if (Double.isNaN(exp) | !Double.isFinite(exp)) {
                    continue;
                }
                w.write(String.format(Locale.US,
                        "%.8f;%.8f;%.8f;%.8f;%.8f;%.8f%n",
                        x, ln, log2, log3, log5, exp));
            }
        }
    }
}
