package series;

import static ru.rmntim.system.Solver.setEpsilon;
import static ru.rmntim.functions.series.Series.calculateLnSeries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CalculateLnSeriesTest {
    private static final double DEFAULT_EPS = 1e-10;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values")
    class KnownValues {
        @Test
        @DisplayName("ln(1) = 0")
        void testLn1() {
            assertEquals(0.0, calculateLnSeries(1.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("ln(e) = 1")
        void testLnE() {
            assertEquals(1.0, calculateLnSeries(Math.E), DEFAULT_EPS);
        }

        @Test
        @DisplayName("ln(1/e) = -1")
        void testLnInvE() {
            assertEquals(-1, calculateLnSeries(1/Math.E), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.log")
    class CompareMathLog {
        @Test
        @DisplayName("Various x > 0")
        void testRandomPositive() {
            double[] xs = {0.1, 0.5, 1.5, 2.0, 3.7, 10.0};
            for (double x : xs) {
                double expected = Math.log(x);
                double actual = calculateLnSeries(x);
                assertEquals(expected, actual, DEFAULT_EPS,
                        () -> String.format("ln(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.2, 2.0, 5.0};
            for (double x : xs) {
                double expected = Math.log(x);
                double actual = calculateLnSeries(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("ln(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }

    @Nested
    @DisplayName("Invalid and boundary cases")
    class InvalidAndBoundary {
        @Test
        @DisplayName("x <= 0 throws IllegalArgumentException")
        void testNonPositive() {
            assertThrows(IllegalArgumentException.class, () -> calculateLnSeries(0.0));
            assertThrows(IllegalArgumentException.class, () -> calculateLnSeries(-1.0));
        }

        @Test
        @DisplayName("Very small x > 0")
        void testVerySmallX() {
            double eps = 1e-9;
            double x = 1e-6;
            double expected = Math.log(x);
            double actual = calculateLnSeries(x);
            assertEquals(expected, actual, eps);
        }

        @Test
        @DisplayName("Monotonicity: ln(x) increases with x")
        void testMonotonicity() {
            double x1 = 0.5, x2 = 2.5;
            double ln1 = calculateLnSeries(x1);
            double ln2 = calculateLnSeries(x2);
            assertTrue(ln1 < ln2,
                    () -> String.format("Expected ln(%.3f)=%.5f < ln(%.3f)=%.5f", x1, ln1, x2, ln2));
        }
    }
}
