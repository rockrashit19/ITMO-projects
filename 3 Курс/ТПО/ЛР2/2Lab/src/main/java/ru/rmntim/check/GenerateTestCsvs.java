package ru.rmntim.check;

import ru.rmntim.system.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.rmntim.check.CheckFirstFunc.generateForFirstEquation;
import static ru.rmntim.check.CheckSecondFunc.generateForSecondEquation;


public class GenerateTestCsvs {
    public static double epsilon = 1e-6;
    public static double xMinTrig = -6.30461;
    public static double xMaxTrig = 0;
    public static double deltaTrig = 0.015;
    public static double deltaLog = 0.1;
    public static double xMinLog = 0.1;
    public static double xMaxLog = 16;
    public static List<Double> xValuesForTrig = new ArrayList<>();
    public static List<Double> xValuesForLog = new ArrayList<>();
    private static final String PDIR = "src/test/resources/";

    public static void main(String[] args) throws IOException {
        Solver.setEpsilon(epsilon);
        filler();
        System.out.println("Filled");
        generateForFirstEquation(PDIR, xValuesForTrig, epsilon);
        generateForSecondEquation(PDIR, xValuesForLog);
    }

    public static void filler() {
        for (double i = xMinTrig; i < xMaxTrig; i += deltaTrig) {
            xValuesForTrig.add(i);
        }
        for (double i = xMinLog; i < xMaxLog; i += deltaLog) {
            xValuesForLog.add(i);
        }
    }
}

