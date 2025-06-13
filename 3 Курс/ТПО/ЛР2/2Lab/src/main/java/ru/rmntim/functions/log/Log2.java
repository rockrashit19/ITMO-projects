package ru.rmntim.functions.log;

import static ru.rmntim.functions.log.Ln.ln;

public class Log2 {
    public static double log2(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x has to be greater than 0 to get log2(x).");
        }
        return ln(x) / ln(2.0);
    }
}
