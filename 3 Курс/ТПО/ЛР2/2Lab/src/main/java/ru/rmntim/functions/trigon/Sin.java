package ru.rmntim.functions.trigon;

import ru.rmntim.functions.series.Series;

public class Sin {
    public static double sin(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Error: x cannot be NaN or Infinity.");
        }
        return Series.calculateSinSeries(x);
    }
}
