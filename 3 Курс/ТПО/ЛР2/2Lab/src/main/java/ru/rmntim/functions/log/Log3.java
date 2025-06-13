package ru.rmntim.functions.log;

import static ru.rmntim.functions.log.Ln.ln;

public class Log3 {
    public static double log3(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x has to be greater than 0 to get log3(x).");
        }
        return ln(x) / ln(3.0);
    }
}
