package functions.trigon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import ru.rmntim.functions.trigon.Cos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static ru.rmntim.system.Solver.setEpsilon;
import static ru.rmntim.functions.trigon.Sec.sec;
import static ru.rmntim.functions.trigon.Cos.cos;

public class FuncSecTest {
    private static final double DEFAULT_EPS = 1e-6;
    private static final double UNDEFINED_EPS = 1e-10;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for sec(x)")
    class KnownValues {
        @Test
        @DisplayName("sec(0) = 1")
        void testSec_0() {
            assertEquals(1.0, sec(0.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sec(PI/3) = 2")
        void testSec_PI_3() {
            assertEquals(2.0, sec(Math.PI / 3.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sec(PI/2) is undefined")
        void testSec_PI_2_Undefined() {
            assertThrows(ArithmeticException.class, () -> sec(Math.PI / 2.0));
        }

        @Test
        @DisplayName("sec(2*PI/3) = -2")
        void testSec_2PI_3() {
            assertEquals(-2.0, sec(2.0 * Math.PI / 3.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sec(PI) = -1")
        void testSec_PI() {
            assertEquals(-1.0, sec(Math.PI), DEFAULT_EPS);
        }

        @Test
        @DisplayName("sec(3*PI/2) is undefined")
        void testSec_3PI_2_Undefined() {
            assertThrows(ArithmeticException.class, () -> sec(3.0 * Math.PI / 2.0));
        }
    }

    @Nested
    @DisplayName("Comparison with 1/Math.cos(x)")
    class CompareReciprocalMathCos {
        @ParameterizedTest
        @ValueSource(doubles = {0.1, Math.PI / 4, 1.0, 2 * Math.PI / 5, 3.0, 5 * Math.PI / 3, 5.5})
        @DisplayName("Various x where sec is defined")
        void testRandomDefined(double x) {
            if (Math.abs(Math.cos(x)) > UNDEFINED_EPS) {
                double expected = 1.0 / Math.cos(x);
                double actual = sec(x);
                assertEquals(expected, actual, DEFAULT_EPS,
                        () -> String.format("sec(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.2, Math.PI / 6, 2.5, 4 * Math.PI / 3, 5.9};
            for (double x : xs) {
                if (Math.abs(Math.cos(x)) > UNDEFINED_EPS) {
                    double expected = 1.0 / Math.cos(x);
                    double actual = sec(x);
                    assertEquals(expected, actual, 1e-4,
                            () -> String.format("sec(%.3f): expected %.10f but got %.10f", x, expected, actual));
                }
            }
        }
    }

    @Test
    @DisplayName("Mock Test: sec should call cos and handle division by zero")
    void secShouldCallCosAndHandleZeroDivision() {
        try (MockedStatic<Cos> mockedCosStatic = mockStatic(Cos.class)) {
            double xDefined = Math.PI / 4.0;
            double cosValueDefined = Math.sqrt(2) / 2.0;
            double expectedSecValue = 1.0 / cosValueDefined;

            mockedCosStatic.when(() -> cos(xDefined)).thenReturn(cosValueDefined);
            assertEquals(expectedSecValue, sec(xDefined), DEFAULT_EPS,
                    () -> String.format("sec(%.3f) should return 1/cos", xDefined));
            mockedCosStatic.verify(() -> cos(xDefined));

            double xUndefined = Math.PI / 2.0;
            double cosValueUndefined = 1e-11; // Close to zero

            mockedCosStatic.when(() -> cos(xUndefined)).thenReturn(cosValueUndefined);
            assertThrows(ArithmeticException.class, () -> sec(xUndefined));
            mockedCosStatic.verify(() -> cos(xUndefined));
        }
    }
}