package functions.trigon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import ru.rmntim.functions.trigon.Sin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static ru.rmntim.system.Solver.setEpsilon;
import static ru.rmntim.functions.trigon.Cos.cos;
import static ru.rmntim.functions.trigon.Sin.sin;

public class FuncCosTest {
    private static final double DEFAULT_EPS = 1e-6;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for cos(x)")
    class KnownValues {
        @Test
        @DisplayName("cos(0) = 1")
        void testCos_0() {
            assertEquals(1.0, cos(0.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cos(PI/6) = sqrt(3)/2")
        void testCos_PI_6() {
            assertEquals(Math.sqrt(3) / 2.0, cos(Math.PI / 6.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cos(PI/2) = 0")
        void testCos_PI_2() {
            assertEquals(0.0, cos(Math.PI / 2.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cos(PI) = -1")
        void testCos_PI() {
            assertEquals(-1.0, cos(Math.PI), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cos(3*PI/2) = 0")
        void testCos_3PI_2() {
            assertEquals(0.0, cos(3.0 * Math.PI / 2.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cos(2*PI) = 1")
        void testCos_2PI() {
            assertEquals(1.0, cos(2.0 * Math.PI), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cos(-PI/2) = 0")
        void testCos_Negative_PI_2() {
            assertEquals(0.0, cos(-Math.PI / 2.0), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.cos(x)")
    class CompareMathCos {
        @ParameterizedTest
        @ValueSource(doubles = {0.1, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0})
        @DisplayName("Various positive x")
        void testRandomPositive(double x) {
            double expected = Math.cos(x);
            double actual = cos(x);
            assertEquals(expected, actual, DEFAULT_EPS,
                    () -> String.format("cos(%.3f): expected %.10f but got %.10f", x, expected, actual));
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.1, -0.5, -1.0, -1.5, -2.0, -2.5, -3.0, -3.5, -4.0, -4.5, -5.0, -5.5, -6.0})
        @DisplayName("Various negative x")
        void testRandomNegative(double x) {
            double expected = Math.cos(x);
            double actual = cos(x);
            assertEquals(expected, actual, DEFAULT_EPS,
                    () -> String.format("cos(%.3f): expected %.10f but got %.10f", x, expected, actual));
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.3, Math.PI / 4, 2.7, 5.9};
            for (double x : xs) {
                double expected = Math.cos(x);
                double actual = cos(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("cos(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }

    @Nested
    @DisplayName("Symmetry and periodicity of cos(x)")
    class SymmetryAndPeriodicity {
        @ParameterizedTest
        @ValueSource(doubles = {0.2, 1.1, Math.PI / 3, 4.5})
        @DisplayName("cos(-x) = cos(x) (Even function)")
        void testCos_EvenFunction(double x) {
            assertEquals(cos(x), cos(-x), DEFAULT_EPS,
                    () -> String.format("cos(-%.3f) should be equal to cos(%.3f)", x, x));
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.4, 1.8, Math.PI / 4, 3.9})
        @DisplayName("cos(x + 2*PI) = cos(x) (Periodicity)")
        void testCos_PeriodicityPositive(double x) {
            assertEquals(cos(x), cos(x + 2 * Math.PI), DEFAULT_EPS,
                    () -> String.format("cos(%.3f + 2*PI) should be equal to cos(%.3f)", x, x));
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.7, 2.3, Math.PI / 6, 5.1})
        @DisplayName("cos(x - 2*PI) = cos(x) (Periodicity)")
        void testCos_PeriodicityNegative(double x) {
            assertEquals(cos(x), cos(x - 2 * Math.PI), DEFAULT_EPS,
                    () -> String.format("cos(%.3f - 2*PI) should be equal to cos(%.3f)", x, x));
        }
    }

    @Test
    @DisplayName("Mock Test: cos should call sin with correct argument")
    void cosShouldCallSinWithCorrectArgument() {
        try (MockedStatic<Sin> mockedSinStatic = mockStatic(Sin.class)) {
            double x = Math.PI / 4.0;
            double expectedSinArg = x + Math.PI / 2.0;
            double expectedCosValue = Math.sin(expectedSinArg);

            mockedSinStatic.when(() -> sin(expectedSinArg)).thenReturn(expectedCosValue);

            double actualCosValue = cos(x);

            assertEquals(expectedCosValue, actualCosValue, DEFAULT_EPS,
                    () -> String.format("cos(%.3f) should call sin(%.3f)", x, expectedSinArg));
            mockedSinStatic.verify(() -> sin(expectedSinArg));
        }
    }
}