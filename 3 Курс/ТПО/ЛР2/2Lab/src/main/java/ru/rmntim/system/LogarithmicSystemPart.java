package ru.rmntim.system;

import static ru.rmntim.functions.log.Ln.ln;
import static ru.rmntim.functions.log.Log10.log10;
import static ru.rmntim.functions.log.Log2.log2;
import static ru.rmntim.functions.log.Log3.log3;
import static ru.rmntim.functions.log.Log5.log5;

public class LogarithmicSystemPart {
    public static double epsilon;

    public static double calculate2(double x) {
        try {
            if (Math.abs(x - 1.0) <= 1e-6) {
                return Double.NaN;
            }
            double part1 = log2(x) / log10(x) - log10(x);
            double part2 = log5(x) * log3(x);
            double sum   = (part1 - part2) + ln(x);

            double num = log2(x) + Math.pow(log3(x), 2);
            double den = ln(x) + log3(x) + log2(x);
            if (Math.abs(den) <= epsilon) {
                throw new ArithmeticException("Denominator too small");
            }
            double frac = num / den;
            return sum - frac;
        } catch (ArithmeticException e) {
            return Double.NaN;
        }
    }

}
