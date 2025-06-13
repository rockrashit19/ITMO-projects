package functions.trigon;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.*;
import static ru.rmntim.functions.trigon.Sin.sin;
import static ru.rmntim.system.Solver.setEpsilon;

public class FuncSinTest {
    private static final double DEFAULT_EPS = 1e-6;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for sin(x)")
    class KnownValues {
        @Test
        @DisplayName("sin(0) = 0")
        void testSin_0() {
            assertEquals(0.0, sin(0.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(PI/6) = 0.5")
        void testSin_PI_6() {
            assertEquals(0.5, sin(Math.PI / 6.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(PI/2) = 1")
        void testSin_PI_2() {
            assertEquals(1.0, sin(Math.PI / 2.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(PI) = 0")
        void testSin_PI() {
            assertEquals(0.0, sin(Math.PI), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(3*PI/2) = -1")
        void testSin_3PI_2() {
            assertEquals(-1.0, sin(3.0 * Math.PI / 2.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(2*PI) = 0")
        void testSin_2PI() {
            assertEquals(0.0, sin(2.0 * Math.PI), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sin(-PI/2) = -1")
        void testSin_Negative_PI_2() {
            assertEquals(-1.0, sin(-Math.PI / 2.0), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.sin(x)")
    class CompareMathSin {
        @ParameterizedTest
        @ValueSource(doubles = {0.1, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0})
        @DisplayName("Various positive x")
        void testRandomPositive(double x) {
            double expected = Math.sin(x);
            double actual = sin(x);
            assertEquals(expected, actual, DEFAULT_EPS,
                    () -> String.format("sin(%.3f): expected %.10f but got %.10f", x, expected, actual));
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.1, -0.5, -1.0, -1.5, -2.0, -2.5, -3.0, -3.5, -4.0, -4.5, -5.0, -5.5, -6.0})
        @DisplayName("Various negative x")
        void testRandomNegative(double x) {
            double expected = Math.sin(x);
            double actual = sin(x);
            assertEquals(expected, actual, DEFAULT_EPS,
                    () -> String.format("sin(%.3f): expected %.10f but got %.10f", x, expected, actual));
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.3, Math.PI / 4, 2.7, 5.9};
            for (double x : xs) {
                double expected = Math.sin(x);
                double actual = sin(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("sin(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }

    @Nested
    @DisplayName("Invalid input cases for sin(x)")
    class InvalidInput {
        @Test
        @DisplayName("NaN throws IllegalArgumentException")
        void testSin_NaN() {
            assertThrows(IllegalArgumentException.class, () -> sin(Double.NaN));
        }

        @Test
        @DisplayName("Positive Infinity throws IllegalArgumentException")
        void testSin_PositiveInfinity() {
            assertThrows(IllegalArgumentException.class, () -> sin(Double.POSITIVE_INFINITY));
        }

        @Test
        @DisplayName("Negative Infinity throws IllegalArgumentException")
        void testSin_NegativeInfinity() {
            assertThrows(IllegalArgumentException.class, () -> sin(Double.NEGATIVE_INFINITY));
        }
    }

    @Nested
    @DisplayName("Symmetry and periodicity of sin(x)")
    class SymmetryAndPeriodicity {
        @ParameterizedTest
        @ValueSource(doubles = {0.2, 1.1, Math.PI / 3, 4.5})
        @DisplayName("sin(-x) = -sin(x) (Odd function)")
        void testSin_OddFunction(double x) {
            assertEquals(-sin(x), sin(-x), DEFAULT_EPS,
                    () -> String.format("sin(-%.3f) should be equal to -sin(%.3f)", x, x));
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.4, 1.8, Math.PI / 4, 3.9})
        @DisplayName("sin(x + 2*PI) = sin(x) (Periodicity)")
        void testSin_PeriodicityPositive(double x) {
            assertEquals(sin(x), sin(x + 2 * Math.PI), DEFAULT_EPS,
                    () -> String.format("sin(%.3f + 2*PI) should be equal to sin(%.3f)", x, x));
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.7, 2.3, Math.PI / 6, 5.1})
        @DisplayName("sin(x - 2*PI) = sin(x) (Periodicity)")
        void testSin_PeriodicityNegative(double x) {
            assertEquals(sin(x), sin(x - 2 * Math.PI), DEFAULT_EPS,
                    () -> String.format("sin(%.3f - 2*PI) should be equal to sin(%.3f)", x, x));
        }
    }


}
