package ru.rmntim.functions.trigon;

import static ru.rmntim.functions.trigon.Sin.sin;
import static ru.rmntim.functions.trigon.Cos.cos;

public class Cot {
    public static double cot(double x) {
        double cos = cos(x);
        double sin = sin(x);
        if (sin == 0){
            throw new ArithmeticException("Ctg is undefined for x = " + x);
        }
        return cos / sin;
    }
}
