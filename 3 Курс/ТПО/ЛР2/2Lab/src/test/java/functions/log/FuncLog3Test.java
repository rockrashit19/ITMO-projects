package functions.log;

import org.junit.jupiter.api.*;
import ru.rmntim.functions.log.Ln;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static ru.rmntim.functions.log.Log3.log3;
import static ru.rmntim.functions.log.Ln.ln;
import static ru.rmntim.system.Solver.setEpsilon;
import static org.mockito.Mockito.mockStatic;


public class FuncLog3Test {
    private static final double DEFAULT_EPS = 1e-6;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for log3")
    class KnownValues {
        @Test
        @DisplayName("log3(1) = 0")
        void testLog3_1() {
            assertEquals(0.0, log3(1.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log3(3) = 1")
        void testLog3_3() {
            assertEquals(1.0, log3(3.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log3(1/3) = -1")
        void testLog3_one_third() {
            assertEquals(-1, log3(1.0 / 3.0), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.log(x)/Math.log(3)")
    class CompareMathLog {
        @Test
        @DisplayName("Various x > 0")
        void testRandomPositive() {
            double[] xs = {0.1, 0.5, 1.5, 3.0, 9.0, 27.0};
            for (double x : xs) {
                double expected = Math.log(x) / Math.log(3.0);
                double actual = log3(x);
                assertEquals(expected, actual, DEFAULT_EPS,
                        () -> String.format("log3(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.2, 3.0, 9.0};
            for (double x : xs) {
                double expected = Math.log(x) / Math.log(3.0);
                double actual = log3(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("log3(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }

    @Nested
    @DisplayName("Invalid and boundary cases for log3")
    class InvalidAndBoundary {
        @Test
        @DisplayName("x <= 0 throws IllegalArgumentException, Mock Test")
        void testNonPositive() {
            assertThrows(IllegalArgumentException.class, () -> log3(0.0));
            assertThrows(IllegalArgumentException.class, () -> log3(-1.0));
        }

        @Test
        @DisplayName("Monotonicity: log3(x) increases with x")
        void testMonotonicity() {
            double x1 = 0.5, x2 = 3.5;
            double logVal1 = log3(x1);
            double logVal2 = log3(x2);
            assertTrue(logVal1 < logVal2,
                    () -> String.format("Expected log3(%.3f)=%.5f < log3(%.3f)=%.5f", x1, logVal1, x2, logVal2));
        }
    }

    @Test
    @DisplayName("Mock Test: log3 should call ln with correct arguments")
    void log3_ShouldCallLnWithCorrectArguments() {
        try (MockedStatic<Ln> mockedLnStatic = mockStatic(Ln.class)) {
            double x = 27.0;
            double mockedLnX = Math.log(x);
            double mockedLn3 = Math.log(3.0);

            mockedLnStatic.when(() -> ln(x)).thenReturn(mockedLnX);
            mockedLnStatic.when(() -> ln(3.0)).thenReturn(mockedLn3);

            double result = log3(x);

            assertEquals(3.0, result, DEFAULT_EPS);
            mockedLnStatic.verify(() -> ln(x));
            mockedLnStatic.verify(() -> ln(3.0));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0.1", "0.5", "1.5", "3.7", "9.0", "30.0"
    })
    @DisplayName("Parameterized test for log3 checking different values by mocking ln")
    void log3ShouldCallLnWithParameters(double x) {
        try (MockedStatic<Ln> mockedLnStatic = mockStatic(Ln.class)) {
            double mockLnXValue = Math.random();
            double mockLn3Value = Math.random();

            if (mockLn3Value == 0) mockLn3Value = 0.1;

            mockedLnStatic.when(() -> ln(x)).thenReturn(mockLnXValue);
            mockedLnStatic.when(() -> ln(3.0)).thenReturn(mockLn3Value);

            double expected = mockLnXValue / mockLn3Value;
            double actual = log3(x);

            assertEquals(expected, actual, DEFAULT_EPS, "Crashed for x = " + x + " (log3)");
            mockedLnStatic.verify(() -> ln(x));
            mockedLnStatic.verify(() -> ln(3.0));
        }
    }
}