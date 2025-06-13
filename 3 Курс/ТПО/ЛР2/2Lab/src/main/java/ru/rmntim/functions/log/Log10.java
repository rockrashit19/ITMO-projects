package ru.rmntim.functions.log;

import static ru.rmntim.functions.log.Ln.ln;

public class Log10 {
    public static double log10(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x has to be greater than 0 to get log10(x).");
        }
        return ln(x) / ln(10.0);
    }
}
