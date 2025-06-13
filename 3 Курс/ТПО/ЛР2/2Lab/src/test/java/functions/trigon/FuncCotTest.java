package functions.trigon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import ru.rmntim.functions.trigon.Cos;
import ru.rmntim.functions.trigon.Sin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static ru.rmntim.system.Solver.setEpsilon;
import static ru.rmntim.functions.trigon.Cot.cot;
import static ru.rmntim.functions.trigon.Sin.sin;
import static ru.rmntim.functions.trigon.Cos.cos;

public class FuncCotTest {
    private static final double DEFAULT_EPS = 1e-6;
    private static final double UNDEFINED_EPS = 1e-10;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for cot(x)")
    class KnownValues {
        @Test
        @DisplayName("cot(PI/4) = 1")
        void testCot_PI_4() {
            assertEquals(1.0, cot(Math.PI / 4.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cot(PI/3) = 1/sqrt(3)")
        void testCot_PI_3() {
            assertEquals(1.0 / Math.sqrt(3.0), cot(Math.PI / 3.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cot(3*PI/4) = -1")
        void testCot_3PI_4() {
            assertEquals(-1.0, cot(3.0 * Math.PI / 4.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("cot(5*PI/6) = -sqrt(3)")
        void testCot_5PI_6() {
            assertEquals(-Math.sqrt(3.0), cot(5.0 * Math.PI / 6.0), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.cos(x)/Math.sin(x)")
    class CompareMathCosSin {
        @ParameterizedTest
        @ValueSource(doubles = {0.1, 0.5, Math.PI / 6, 1.5, Math.PI / 3, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0})
        @DisplayName("Various x where cot is defined")
        void testRandomDefined(double x) {
            if (Math.abs(Math.sin(x)) > UNDEFINED_EPS) {
                double expected = Math.cos(x) / Math.sin(x);
                double actual = cot(x);
                assertEquals(expected, actual, DEFAULT_EPS,
                        () -> String.format("cot(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.2, Math.PI / 5, 2.8, 5.7};
            for (double x : xs) {
                if (Math.abs(Math.sin(x)) > UNDEFINED_EPS) {
                    double expected = Math.cos(x) / Math.sin(x);
                    double actual = cot(x);
                    assertEquals(expected, actual, 1e-4,
                            () -> String.format("cot(%.3f): expected %.10f but got %.10f", x, expected, actual));
                }
            }
        }
    }

    @Nested
    @DisplayName("Undefined cases for cot(x)")
    class UndefinedCases {
        @Test
        @DisplayName("Mock Test: cot should call cos and sin and handle division")
        void cotShouldCallCosAndSin() {
            try (MockedStatic<Cos> mockedCosStatic = mockStatic(Cos.class);
                 MockedStatic<Sin> mockedSinStatic = mockStatic(Sin.class)) {
                double x = Math.PI / 3.0;
                double expectedCosValue = 0.5;
                double expectedSinValue = Math.sqrt(3) / 2.0;
                double expectedCotValue = expectedCosValue / expectedSinValue;

                mockedCosStatic.when(() -> cos(x)).thenReturn(expectedCosValue);
                mockedSinStatic.when(() -> sin(x)).thenReturn(expectedSinValue);

                double actualCotValue = cot(x);

                assertEquals(expectedCotValue, actualCotValue, DEFAULT_EPS,
                        () -> String.format("cot(%.3f) should return cos/sin", x));
                mockedCosStatic.verify(() -> cos(x));
                mockedSinStatic.verify(() -> sin(x));
            }
        }

        @Test
        @DisplayName("Mock Test: cot should throw ArithmeticException when sin is close to zero")
        void cotShouldThrowExceptionOnZeroSin() {
            try (MockedStatic<Cos> mockedCosStatic = mockStatic(Cos.class);
                 MockedStatic<Sin> mockedSinStatic = mockStatic(Sin.class)) {
                double x = 0.0;
                double problematicSinValue = 1e-11; // Close to zero

                mockedCosStatic.when(() -> cos(x)).thenReturn(1.0);
                mockedSinStatic.when(() -> sin(x)).thenReturn(problematicSinValue);

                assertThrows(ArithmeticException.class, () -> cot(x));

                mockedCosStatic.verify(() -> cos(x));
                mockedSinStatic.verify(() -> sin(x));
            }
        }
    }
}