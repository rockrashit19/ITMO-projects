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
import static ru.rmntim.functions.trigon.Csc.csc;
import static ru.rmntim.functions.trigon.Sin.sin;

public class FuncCscTest {
    private static final double DEFAULT_EPS = 1e-6;
    private static final double UNDEFINED_EPS = 1e-10;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for csc(x)")
    class KnownValues {
        @Test
        @DisplayName("csc(PI/6) = 2")
        void testCsc_PI_6() {
            assertEquals(2.0, csc(Math.PI / 6.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("csc(PI/2) = 1")
        void testCsc_PI_2() {
            assertEquals(1.0, csc(Math.PI / 2.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("csc(5*PI/6) = 2")
        void testCsc_5PI_6() {
            assertEquals(2.0, csc(5.0 * Math.PI / 6.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("csc(3*PI/2) = -1")
        void testCsc_3PI_2() {
            assertEquals(-1.0, csc(3.0 * Math.PI / 2.0), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with 1/Math.sin(x)")
    class CompareReciprocalMathSin {
        @ParameterizedTest
        @ValueSource(doubles = {0.1, Math.PI / 4, 1.5, 2 * Math.PI / 3, 2.5, 7 * Math.PI / 4, 5.0})
        @DisplayName("Various x where csc is defined")
        void testRandomDefined(double x) {
            if (Math.abs(Math.sin(x)) > UNDEFINED_EPS) {
                double expected = 1.0 / Math.sin(x);
                double actual = csc(x);
                assertEquals(expected, actual, DEFAULT_EPS,
                        () -> String.format("csc(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.3, Math.PI / 5, 2.8, 5.7};
            for (double x : xs) {
                if (Math.abs(Math.sin(x)) > UNDEFINED_EPS) {
                    double expected = 1.0 / Math.sin(x);
                    double actual = csc(x);
                    assertEquals(expected, actual, 1e-4,
                            () -> String.format("csc(%.3f): expected %.10f but got %.10f", x, expected, actual));
                }
            }
        }
    }

    @Nested
    @DisplayName("Undefined cases for csc(x)")
    class UndefinedCases {

        @Test
        @DisplayName("Mock Test: csc should call sin and handle division by zero")
        void cscShouldCallSinAndHandleZeroDivision() {
            try (MockedStatic<Sin> mockedSinStatic = mockStatic(Sin.class)) {
                double xDefined = Math.PI / 3.0;
                double sinValueDefined = Math.sqrt(3) / 2.0;
                double expectedCscValue = 1.0 / sinValueDefined;

                mockedSinStatic.when(() -> sin(xDefined)).thenReturn(sinValueDefined);
                assertEquals(expectedCscValue, csc(xDefined), DEFAULT_EPS,
                        () -> String.format("csc(%.3f) should return 1/sin", xDefined));
                mockedSinStatic.verify(() -> sin(xDefined));

                double xUndefined = 0.0;
                double sinValueUndefined = 1e-11; // Close to zero

                mockedSinStatic.when(() -> sin(xUndefined)).thenReturn(sinValueUndefined);
                assertThrows(ArithmeticException.class, () -> csc(xUndefined));
                mockedSinStatic.verify(() -> sin(xUndefined));
            }
        }
    }
}