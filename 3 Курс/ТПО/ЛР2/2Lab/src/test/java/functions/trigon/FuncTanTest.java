package functions.trigon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import ru.rmntim.functions.trigon.Sin;
import ru.rmntim.functions.trigon.Cos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static ru.rmntim.system.Solver.setEpsilon;
import static ru.rmntim.functions.trigon.Tan.tan;
import static ru.rmntim.functions.trigon.Sin.sin;
import static ru.rmntim.functions.trigon.Cos.cos;

public class FuncTanTest {
    private static final double DEFAULT_EPS = 1e-6;
    private static final double UNDEFINED_EPS = 1e-7;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for tan(x)")
    class KnownValues {
        @Test
        @DisplayName("tan(0) = 0")
        void testTan_0() {
            assertEquals(0.0, tan(0.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("tan(PI/4) = 1")
        void testTan_PI_4() {
            assertEquals(1.0, tan(Math.PI / 4.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("tan(PI/3) = sqrt(3)")
        void testTan_PI_3() {
            assertEquals(Math.sqrt(3.0), tan(Math.PI / 3.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("tan(-PI/4) = -1")
        void testTan_Negative_PI_4() {
            assertEquals(-1.0, tan(-Math.PI / 4.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("tan(PI/2) is undefined")
        void testTan_PI_2_Undefined() {
            assertThrows(ArithmeticException.class, () -> tan(Math.PI / 2.0));
        }
    }

    @Nested
    @DisplayName("Comparison with Math.tan(x)")
    class CompareMathTan {
        @ParameterizedTest
        @ValueSource(doubles = {-1.0, -Math.PI / 3, -0.5, 0.5, Math.PI / 6, 1.0, 1.5, 2.0, 2.5, 3.0})
        @DisplayName("Various x where tan is defined")
        void testRandomDefined(double x) {
            double epsilon = 1e-5;
            if (Math.abs(Math.cos(x)) > UNDEFINED_EPS) {
                double expected = Math.tan(x);
                double actual = tan(x);
                assertEquals(expected, actual, epsilon,
                        () -> String.format("tan(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {-0.8, 0.2, Math.PI / 5, 2.8};
            for (double x : xs) {
                if (Math.abs(Math.cos(x)) > UNDEFINED_EPS) {
                    double expected = Math.tan(x);
                    double actual = tan(x);
                    assertEquals(expected, actual, 1e-4,
                            () -> String.format("tan(%.3f): expected %.10f but got %.10f", x, expected, actual));
                }
            }
        }
    }

    @Nested
    @DisplayName("Undefined cases for tan(x)")
    class UndefinedCases {
        @ParameterizedTest
        @ValueSource(doubles = {Math.PI / 2.0, -Math.PI / 2.0, 3 * Math.PI / 2.0, -3 * Math.PI / 2.0})
        @DisplayName("tan((2n+1)*PI/2) throws ArithmeticException")
        void testTan_OddMultiplePI_2(double x) {
            assertThrows(ArithmeticException.class, () -> tan(x));
        }

        @ParameterizedTest
        @ValueSource(doubles = {Math.PI / 2.0 + UNDEFINED_EPS / 2.0, -Math.PI / 2.0 - UNDEFINED_EPS / 2.0})
        @DisplayName("tan(x) near (2n+1)*PI/2 throws ArithmeticException")
        void testTan_Near_OddMultiplePI_2(double x) {
            assertThrows(ArithmeticException.class, () -> tan(x));
        }
    }

    @Nested
    @DisplayName("Symmetry and periodicity of tan(x)")
    class SymmetryAndPeriodicity {
        @ParameterizedTest
        @ValueSource(doubles = {0.2, 1.1, -0.6, Math.PI / 6})
        @DisplayName("tan(-x) = -tan(x) (Odd function)")
        void testTan_OddFunction(double x) {
            if (Math.abs(Math.cos(x)) > UNDEFINED_EPS && Math.abs(Math.cos(-x)) > UNDEFINED_EPS) {
                assertEquals(-tan(x), tan(-x), DEFAULT_EPS,
                        () -> String.format("tan(-%.3f) should be equal to -tan(%.3f)", x, x));
            }
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.4, -0.9, Math.PI / 3, 2.1})
        @DisplayName("tan(x + PI) = tan(x) (Periodicity)")
        void testTan_Periodicity(double x) {
            if (Math.abs(Math.cos(x)) > UNDEFINED_EPS && Math.abs(Math.cos(x + Math.PI)) > UNDEFINED_EPS) {
                assertEquals(tan(x), tan(x + Math.PI), DEFAULT_EPS,
                        () -> String.format("tan(%.3f + PI) should be equal to tan(%.3f)", x, x));
            }
        }
    }

    @Test
    @DisplayName("Mock Test: tan should call sin and cos and handle division by zero")
    void tanShouldCallSinAndCosAndHandleZeroDivision() {
        try (MockedStatic<Sin> mockedSinStatic = mockStatic(Sin.class);
             MockedStatic<Cos> mockedCosStatic = mockStatic(Cos.class)) {
            double xDefined = Math.PI / 6.0;
            double sinValueDefined = 0.5;
            double cosValueDefined = Math.sqrt(3) / 2.0;
            double expectedTanValue = sinValueDefined / cosValueDefined;

            mockedSinStatic.when(() -> sin(xDefined)).thenReturn(sinValueDefined);
            mockedCosStatic.when(() -> cos(xDefined)).thenReturn(cosValueDefined);

            assertEquals(expectedTanValue, tan(xDefined), DEFAULT_EPS,
                    () -> String.format("tan(%.3f) should return sin/cos", xDefined));
            mockedSinStatic.verify(() -> sin(xDefined));
            mockedCosStatic.verify(() -> cos(xDefined));

            double xUndefined = Math.PI / 2.0;
            double cosValueUndefined = 1e-8; // Close to zero

            mockedSinStatic.when(() -> sin(xUndefined)).thenReturn(1.0);
            mockedCosStatic.when(() -> cos(xUndefined)).thenReturn(cosValueUndefined);

            assertThrows(ArithmeticException.class, () -> tan(xUndefined));
            mockedSinStatic.verify(() -> sin(xUndefined));
            mockedCosStatic.verify(() -> cos(xUndefined));
        }
    }
}