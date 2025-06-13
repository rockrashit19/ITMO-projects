package ru.rmntim.utils;

import java.io.IOException;

import ru.rmntim.system.Solver;

import static ru.rmntim.functions.trigon.Sin.sin;

public class Main{
    private static final double xmin = -12;
    private static final double xmax = 12;
    private static final String separator = ";";

    private static final String filepath = "/Users/admin/ITMO/3 Курс/ТПО/ЛР2/2Lab/src/main/resources/system.csv";

    public static void main(String[] args){
        try{
            System.out.println(sin(xmin));
            Solver.run(filepath, xmin, xmax, 0.0001, 1e-9, separator);
            CsvPlotter.plot(
                    filepath, separator, "График решения системы", "x", "y", xmin, xmax);
        } catch (IOException | RuntimeException | Error e){
            e.printStackTrace();
        }
    }
}