package ru.rmntim.functions.trigon;

import static ru.rmntim.functions.trigon.Sin.sin;
import static ru.rmntim.functions.trigon.Cos.cos;

public class Tan {
    public static double tan(double x) {
        double cos = cos(x);
        double sin = sin(x);
        if (Math.abs(cos) - 1e-7 < 0){
            throw new ArithmeticException("Tg is undefined for x = " + x);
        }
        return sin / cos;
    }
}
