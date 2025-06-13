package functions.log;

import org.junit.jupiter.api.*;
import ru.rmntim.functions.log.Ln;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static ru.rmntim.functions.log.Ln.ln;
import static ru.rmntim.functions.log.Log10.log10;
import static ru.rmntim.system.Solver.setEpsilon;
import static org.mockito.Mockito.mockStatic;


public class FuncLog10Test {
    private static final double DEFAULT_EPS = 1e-6;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values for log10")
    class KnownValues {
        @Test
        @DisplayName("log10(1) = 0")
        void testLog10_1() {
            assertEquals(0.0, log10(1.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log10(10) = 1")
        void testLog10_10() {
            assertEquals(1.0, log10(10.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log10(1/10) = -1")
        void testLog10_one_tenth() {
            assertEquals(-1, log10(1.0 / 10.0), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.log10(x)")
    class CompareMathLog10 {
        @Test
        @DisplayName("Various x > 0")
        void testRandomPositive() {
            double[] xs = {0.1, 0.5, 2.0, 10.0, 100.0, 1000.0};
            for (double x : xs) {
                double expected = Math.log10(x);
                double actual = log10(x);
                assertEquals(expected, actual, DEFAULT_EPS,
                        () -> String.format("log10(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }

        @Test
        @DisplayName("With larger epsilon")
        void testWithLargerEpsilon() {
            setEpsilon(1e-4);
            double[] xs = {0.2, 10.0, 100.0};
            for (double x : xs) {
                double expected = Math.log10(x);
                double actual = log10(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("log10(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }

    @Nested
    @DisplayName("Invalid and boundary cases for log10")
    class InvalidAndBoundary {
        @Test
        @DisplayName("x <= 0 throws IllegalArgumentException")
        void testNonPositive() {
            assertThrows(IllegalArgumentException.class, () -> log10(0.0));
            assertThrows(IllegalArgumentException.class, () -> log10(-1.0));
        }

        @Test
        @DisplayName("Monotonicity: log10(x) increases with x")
        void testMonotonicity() {
            double x1 = 0.5, x2 = 5.0;
            double logVal1 = log10(x1);
            double logVal2 = log10(x2);
            assertTrue(logVal1 < logVal2,
                    () -> String.format("Expected log10(%.3f)=%.5f < log10(%.3f)=%.5f", x1, logVal1, x2, logVal2));
        }
    }

    @Test
    @DisplayName("Mock Test: log10 should call ln with correct arguments")
    void log10_ShouldCallLnWithCorrectArguments() {
        try (MockedStatic<Ln> mockedLnStatic = mockStatic(Ln.class)) {
            double x = 1000.0;
            double mockedLnX = Math.log(x);
            double mockedLn10 = Math.log(10.0);

            mockedLnStatic.when(() -> ln(x)).thenReturn(mockedLnX);
            mockedLnStatic.when(() -> ln(10.0)).thenReturn(mockedLn10);

            double result = log10(x);

            assertEquals(3.0, result, DEFAULT_EPS);
            mockedLnStatic.verify(() -> ln(x));
            mockedLnStatic.verify(() -> ln(10.0));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0.01", "0.2", "3.5", "10.5", "50.0", "200.0"
    })
    @DisplayName("Parameterized test for log10 checking different values by mocking ln")
    void log10ShouldCallLnWithParameters(double x) {
        try (MockedStatic<Ln> mockedLnStatic = mockStatic(Ln.class)) {
            double mockLnXValue = Math.random();
            double mockLn10Value = Math.random();

            if (mockLn10Value == 0) mockLn10Value = 0.1;

            mockedLnStatic.when(() -> ln(x)).thenReturn(mockLnXValue);
            mockedLnStatic.when(() -> ln(10.0)).thenReturn(mockLn10Value);

            double expected = mockLnXValue / mockLn10Value;
            double actual = log10(x);

            assertEquals(expected, actual, DEFAULT_EPS, "Crashed for x = " + x + " (log10)");
            mockedLnStatic.verify(() -> ln(x));
            mockedLnStatic.verify(() -> ln(10.0));
        }
    }
}