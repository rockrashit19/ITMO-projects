package ru.rmntim.functions.log;

import ru.rmntim.functions.series.Series;

public class Ln {
    public static double ln(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x has to be greater than 0 to get ln(x).");
        }
        return Series.calculateLnSeries(x);
    }
}
