package ru.rmntim.functions.series;

import ru.rmntim.system.Solver;

public class Series {

    public static double calculateSinSeries(double x) {
        double epsilon = Solver.getEpsilon();
        x %= (Math.PI * 2D);
        double term = x;
        double sum = term;
        int n = 1;

        while (Math.abs(term) >= epsilon) {
            term *= -x * x / ((2 * n) * (2 * n + 1));
            sum += term;
            n++;
        }

        return sum;
    }

    public static double calculateLnSeries(double x) {
        double epsilon = Solver.getEpsilon() / 2;
        if (x <= 0) {
            throw new IllegalArgumentException("X must be greater than zero");
        }

        if (x == 1){
            return 0.0;
        }

        double y = (x - 1) / (x + 1);
        double yPower = y * y;
        double f = 1 - yPower;
        double sum = y;
        int n = 1;

        while (true) {
            y *= yPower * (2 * n - 1) / (2 * n + 1);

            if (Math.abs(y) < epsilon * f) {
                break;
            }
            sum += y;
            n++;
        }

        return 2 * sum;
    }
}
