package ru.rmntim.functions.trigon;

import static ru.rmntim.functions.trigon.Sin.sin;

public class Cos {
    public static double cos(double x) {
        return sin(x+Math.PI/2);
    }
}
