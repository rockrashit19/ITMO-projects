package ru.rmntim.functions.log;

import static ru.rmntim.functions.log.Ln.ln;

public class Log5 {
    public static double log5(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x has to be greater than 0 to get log5(x).");
        }
        return ln(x) / ln(5.0);
    }
}
