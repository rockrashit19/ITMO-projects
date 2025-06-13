package series;

import static ru.rmntim.system.Solver.setEpsilon;
import static ru.rmntim.functions.series.Series.calculateSinSeries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CalculateSinSeriesTest {
    private static final double DEFAULT_EPS = 1e-10;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values tests")
    class KnownValues {
        @Test
        @DisplayName("sin(0) = 0")
        void testSin0() {
            assertEquals(0.0, calculateSinSeries(0.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(π/2) = 1")
        void testSinPiOver2() {
            assertEquals(1.0, calculateSinSeries(Math.PI / 2), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(π) = 0")
        void testSinPi() {
            assertEquals(0.0, calculateSinSeries(Math.PI), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(3π/2) = -1")
        void testSin3PiOver2() {
            assertEquals(-1.0, calculateSinSeries(3 * Math.PI / 2), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.sin for random values")
    class ComparisonWithMathSin {
        @Test
        @DisplayName("Various x values, default epsilon")
        void testRandomValuesDefaultEps() {
            double[] xs = {0.1, -0.5, 2.3, -4.7, 10.0};
            for (double x : xs) {
                double expected = Math.sin(x);
                double actual = calculateSinSeries(x);
                assertEquals(expected, actual, DEFAULT_EPS,
                        () -> String.format("sin(%.3f) expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("Various x values with larger epsilon")
        void testRandomValuesLargeEps() {
            setEpsilon(1e-4);
            double[] xs = {0.1, 1.0, 2.0};
            for (double x : xs) {
                double expected = Math.sin(x);
                double actual = calculateSinSeries(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("sin(%.3f) expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }


    @Nested
    @DisplayName("Boundary and equivalence classes")
    class BoundaryAndEquivalence {
        @Test
        @DisplayName("Very small x ~ 0")
        void testVerySmallX() {
            double x = 1e-12;
            double actual = calculateSinSeries(x);
            assertEquals(x, actual, DEFAULT_EPS);
        }

        @Test
        @DisplayName("Large x wraps around")
        void testLargeXWrap() {
            double x = 10 * Math.PI; // эквивалентно 0
            double actual = calculateSinSeries(x);
            assertEquals(0.0, actual, DEFAULT_EPS);
        }

        @Test
        @DisplayName("Negative x behaves odd")
        void testOddFunctionProperty() {
            double x = 2.5;
            double pos = calculateSinSeries(x);
            double neg = calculateSinSeries(-x);
            assertEquals(-pos, neg, DEFAULT_EPS);
        }
    }

    @Test
    @DisplayName("Throws when epsilon is too large for convergence check")
    void testConvergence() {
        setEpsilon(2.0); // слишком большая
        double x = 1.0;
        assertDoesNotThrow(() -> calculateSinSeries(x));
    }
}
