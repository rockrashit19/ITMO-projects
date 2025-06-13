package functions.log;

import org.junit.jupiter.api.*;
import ru.rmntim.functions.log.Ln;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static ru.rmntim.functions.log.Ln.ln;
import static ru.rmntim.functions.log.Log5.log5;
import static ru.rmntim.system.Solver.setEpsilon;
import static org.mockito.Mockito.mockStatic;


public class FuncLog5Test {
    private static final double DEFAULT_EPS = 1e-6;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for log5")
    class KnownValues {
        @Test
        @DisplayName("log5(1) = 0")
        void testLog5_1() {
            assertEquals(0.0, log5(1.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log5(5) = 1")
        void testLog5_5() {
            assertEquals(1.0, log5(5.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log5(1/5) = -1")
        void testLog5_one_fifth() {
            assertEquals(-1, log5(1.0 / 5.0), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.log(x)/Math.log(5)")
    class CompareMathLog {
        @Test
        @DisplayName("Various x > 0")
        void testRandomPositive() {
            double[] xs = {0.1, 0.5, 1.5, 5.0, 25.0, 125.0};
            for (double x : xs) {
                double expected = Math.log(x) / Math.log(5.0);
                double actual = log5(x);
                assertEquals(expected, actual, 1e-5,
                        () -> String.format("log5(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.2, 5.0, 25.0};
            for (double x : xs) {
                double expected = Math.log(x) / Math.log(5.0);
                double actual = log5(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("log5(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }

    @Nested
    @DisplayName("Invalid and boundary cases for log5")
    class InvalidAndBoundary {
        @Test
        @DisplayName("x <= 0 throws IllegalArgumentException, Mock Test")
        void testNonPositive() {
            assertThrows(IllegalArgumentException.class, () -> log5(0.0));
            assertThrows(IllegalArgumentException.class, () -> log5(-1.0));
        }

        @Test
        @DisplayName("Monotonicity: log5(x) increases with x")
        void testMonotonicity() {
            double x1 = 0.5, x2 = 3.5;
            double logVal1 = log5(x1);
            double logVal2 = log5(x2);
            assertTrue(logVal1 < logVal2,
                    () -> String.format("Expected log5(%.3f)=%.5f < log5(%.3f)=%.5f", x1, logVal1, x2, logVal2));
        }
    }

    @Test
    @DisplayName("Mock Test: log5 should call ln with correct arguments")
    void log5_ShouldCallLnWithCorrectArguments() {
        try (MockedStatic<Ln> mockedLnStatic = mockStatic(Ln.class)) {
            double x = 125.0;
            double mockedLnX = Math.log(x);
            double mockedLn5 = Math.log(5.0);

            mockedLnStatic.when(() -> ln(x)).thenReturn(mockedLnX);
            mockedLnStatic.when(() -> ln(5.0)).thenReturn(mockedLn5);

            double result = log5(x);

            assertEquals(3.0, result, DEFAULT_EPS);
            mockedLnStatic.verify(() -> ln(x));
            mockedLnStatic.verify(() -> ln(5.0));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0.1", "0.5", "1.5", "5.7", "25.0", "130.0"
    })
    @DisplayName("Parameterized test for log5 checking different values by mocking ln")
    void log5ShouldCallLnWithParameters(double x) {
        try (MockedStatic<Ln> mockedLnStatic = mockStatic(Ln.class)) {
            double mockLnXValue = Math.random();
            double mockLn5Value = Math.random();

            if (mockLn5Value == 0) mockLn5Value = 0.1;

            mockedLnStatic.when(() -> ln(x)).thenReturn(mockLnXValue);
            mockedLnStatic.when(() -> ln(5.0)).thenReturn(mockLn5Value);

            double expected = mockLnXValue / mockLn5Value;
            double actual = log5(x);

            assertEquals(expected, actual, DEFAULT_EPS, "Crashed for x = " + x + " (log5)");
            mockedLnStatic.verify(() -> ln(x));
            mockedLnStatic.verify(() -> ln(5.0));
        }
    }
}