package ru.rmntim.system;

import static ru.rmntim.functions.trigon.Cos.cos;
import static ru.rmntim.functions.trigon.Cot.cot;
import static ru.rmntim.functions.trigon.Csc.csc;
import static ru.rmntim.functions.trigon.Sec.sec;
import static ru.rmntim.functions.trigon.Sin.sin;
import static ru.rmntim.functions.trigon.Tan.tan;

public class TrigonometricSystemPart {
    public static double epsilon;

    public static double calculate1(double x){
        double a = sin(x) * sec(x) - sin(x);
        double b = a * tan(x) - sin(x);
        double pow3 = Math.pow(b, 3);

        double c = (tan(x) + csc(x) / cos(x)) / cot(x);

        double pow2 = Math.pow(pow3 + c, 2);

        double d = sec(x) - (cot(x) - csc(x));
        double e = sin(x) + d / csc(x);

        double numerator = pow2 * e;

        double top  = cos(x) + cos(x);
        double left = sin(x) + sec(x) * ((sin(x) - csc(x)) * sec(x));
        double right= (cot(x) * cos(x)) * sin(x) * ((tan(x) - sin(x)) / sec(x));
        if (Math.abs(left - right) - epsilon < 0){
            throw new ArithmeticException("Function cannot be competed for this x");
        }
        double frac = top / (left - right);

        return ( (numerator - frac) * csc(x) ) / cos(x);
    }

}
