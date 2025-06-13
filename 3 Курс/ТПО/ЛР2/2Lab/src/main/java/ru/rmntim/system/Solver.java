package ru.rmntim.system;


import ru.rmntim.utils.CsvWriter;

import java.io.IOException;

public class Solver {
    public static double epsilon;

    public static double solvingSystem(double x){
        if (x <= 0) {
//            if (isTrigSingularity(x)) {
//                x += epsilon;
//            }
            return TrigonometricSystemPart.calculate1(x);
        } else {
            return LogarithmicSystemPart.calculate2(x);
        }
    }

    public static void run(String fileName, double start, double end, double step, double epsilon, String separator) throws IOException {
        setEpsilon(epsilon);
        CsvWriter csvWriter = new CsvWriter(fileName, separator);
        try {
            for (double x = start; x <= end; x += step) {
                double result = solvingSystem(x);
                csvWriter.write(x, result, separator, 3);
            }
        } finally {
            csvWriter.close();
        }
    }


    public static double getEpsilon() {
        return epsilon;
    }

    public static void setEpsilon(double epsilon) {
        Solver.epsilon = epsilon;
    }
}


