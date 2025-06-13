package ru.rmntim.functions.trigon;

import static ru.rmntim.functions.trigon.Sin.sin;

public class Csc {
    public static double csc(double x) {
        double sin = sin(x);
        if (sin == 0) {
            throw new ArithmeticException("Csc is undefined for x = " + x);
        }
        return 1.0 / sin;
    }
}
