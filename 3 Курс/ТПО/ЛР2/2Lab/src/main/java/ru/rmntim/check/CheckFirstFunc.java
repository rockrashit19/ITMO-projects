package ru.rmntim.check;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static ru.rmntim.functions.trigon.Cos.cos;
import static ru.rmntim.functions.trigon.Cot.cot;
import static ru.rmntim.functions.trigon.Csc.csc;
import static ru.rmntim.functions.trigon.Sec.sec;
import static ru.rmntim.functions.trigon.Sin.sin;
import static ru.rmntim.functions.trigon.Tan.tan;

public class CheckFirstFunc {
    private static double sin;
    private static double cos;
    private static double tan;
    private static double cot;
    private static double sec;
    private static double csc;
    public static double exp;
    public static double epsilon;
    public static void generateForFirstEquation(String DIR, List<Double> xValuesForTrig, double eps){
        epsilon = eps;
        try (BufferedWriter wStubs = new BufferedWriter(new FileWriter(DIR + "testTrigWithAllStubs.csv"));
             BufferedWriter wAll   = new BufferedWriter(new FileWriter(DIR + "testTrigWithAllFunc.csv"));
             BufferedWriter wA     = new BufferedWriter(new FileWriter(DIR + "testTrigWithCosTanCotSecCscStubs.csv"));
             BufferedWriter wB     = new BufferedWriter(new FileWriter(DIR + "testTrigWithTanCotSecCscStubs.csv"));
             BufferedWriter wC     = new BufferedWriter(new FileWriter(DIR + "testTrigWithCotSecCscStubs.csv"));
             BufferedWriter wD     = new BufferedWriter(new FileWriter(DIR + "testTrigWithSecCscStubs.csv"));
             BufferedWriter wE     = new BufferedWriter(new FileWriter(DIR + "testTrigWithCscStubs.csv"))
        ) {
            wStubs.write("x;sin;cos;tan;cot;sec;csc;expected\n");
            wAll .write("x;answer\n");
            wA   .write("x;sin;answer\n");
            wB   .write("x;sin;cos;answer\n");
            wC   .write("x;sin;cos;tan;answer\n");
            wD   .write("x;sin;cos;tan;cot;answer\n");
            wE   .write("x;sin;cos;tan;cot;sec;answer\n");

            StringBuilder sb = new StringBuilder(128);
            for (double x : xValuesForTrig) {
                sin = sin(x);
                cos = cos(x);
                tan = tan(x);
                cot = cot(x);
                sec = sec(x);
                csc = csc(x);
                exp  = calculation(x);

                sb.setLength(0);
                sb.append(String.format(Locale.US, "%.8f", x)).append(';')
                        .append(sin).append(';')
                        .append(cos).append(';')
                        .append(tan).append(';')
                        .append(cot).append(';')
                        .append(sec).append(';')
                        .append(csc).append(';')
                        .append(exp).append('\n');
                wStubs.write(sb.toString());
                
                sb.setLength(0);
                sb.append(String.format(Locale.US, "%.8f", x))
                        .append(';').append(exp).append('\n');
                wAll.write(sb.toString());
                
                sb.setLength(0);
                sb.append(String.format(Locale.US, "%.8f", x))
                        .append(';').append(sin).append(';').append(exp).append('\n');
                wA.write(sb.toString());

                sb.setLength(0);
                sb.append(String.format(Locale.US, "%.8f", x))
                        .append(';').append(sin).append(';').append(cos).append(';').append(exp).append('\n');
                wB.write(sb.toString());

                sb.setLength(0);
                sb.append(String.format(Locale.US, "%.8f", x))
                        .append(';').append(sin).append(';').append(cos).append(';')
                        .append(tan).append(';').append(exp).append('\n');
                wC.write(sb.toString());

                sb.setLength(0);
                sb.append(String.format(Locale.US, "%.8f", x))
                        .append(';').append(sin).append(';').append(cos).append(';')
                        .append(tan).append(';').append(cot).append(';').append(exp).append('\n');
                wD.write(sb.toString());

                sb.setLength(0);
                sb.append(String.format(Locale.US, "%.8f", x))
                        .append(';').append(sin).append(';').append(cos).append(';')
                        .append(tan).append(';').append(cot).append(';')
                        .append(sec).append(';').append(exp).append('\n');
                wE.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double calculation(double x){
        double a = sin * sec - sin;
        double b = a * tan - sin;
        double pow3 = Math.pow(b, 3);

        double c = (tan + csc / cos) / cot;

        double pow2 = Math.pow(pow3 + c, 2);

        double d = sec - (cot - csc);
        double e = sin + d / csc;

        double numerator = pow2 * e;

        double top  = cos + cos;
        double left = sin + sec * ((sin - csc) * sec);
        double right= (cot * cos) * sin * ((tan - sin) / sec);
        if (Math.abs(left - right) - epsilon < 0){
            throw new ArithmeticException("Function cannot be competed for this x");
        }
        double frac = top / (left - right);

        return ( (numerator - frac) * csc ) / cos;
    }
}
