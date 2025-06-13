package ru.rmntim.functions.trigon;

import static ru.rmntim.functions.trigon.Cos.cos;

public class Sec {
    public static double sec(double x) {
        double cos = cos(x);
        if (Math.abs(cos) < 1e-7) {
            throw new ArithmeticException("Sec is undefined for x = " + x);
        }
        return 1.0 / cos;
    }
}
