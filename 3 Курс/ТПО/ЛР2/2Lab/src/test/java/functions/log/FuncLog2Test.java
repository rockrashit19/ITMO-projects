package functions.log;

import org.junit.jupiter.api.*;
import ru.rmntim.functions.log.Ln;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static ru.rmntim.functions.log.Log2.log2;
import static ru.rmntim.functions.log.Ln.ln;
import static ru.rmntim.system.Solver.setEpsilon;
import static org.mockito.Mockito.mockStatic;


public class FuncLog2Test {
    private static final double DEFAULT_EPS = 1e-6;

    @BeforeEach
    void setup() {
        setEpsilon(DEFAULT_EPS);
    }

    @Nested
    @DisplayName("Known values")
    class KnownValues {
        @Test
        @DisplayName("log2(1) = 0")
        void testLog2_1() {
            assertEquals(0.0, log2(1.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log2(2) = 1")
        void testLog2_2() {
            assertEquals(1.0, log2(2.0), DEFAULT_EPS);
        }

        @Test
        @DisplayName("log(0.5) = -1")
        void testLog2_3() {
            assertEquals(-1, log2(0.5), DEFAULT_EPS);
        }
    }

    @Nested
    @DisplayName("Comparison with Math.log(x)/Math.log(2)")
    class CompareMathLog {
        @Test
        @DisplayName("Various x > 0")
        void testRandomPositive() {
            double[] xs = {0.1, 0.5, 1.5, 2.0, 3.7, 10.0};
            for (double x : xs) {
                double expected = Math.log(x) / Math.log(2);
                double actual = log2(x);
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
                double expected = Math.log(x) / Math.log(2);
                double actual = log2(x);
                assertEquals(expected, actual, 1e-4,
                        () -> String.format("ln(%.3f): expected %.10f but got %.10f", x, expected, actual));
            }
        }
    }

    @Nested
    @DisplayName("Invalid and boundary cases")
    class InvalidAndBoundary {
        @Test
        @DisplayName("x <= 0 throws IllegalArgumentException, Mock Test")
        void testNonPositive() {
            assertThrows(IllegalArgumentException.class, () -> log2(0.0));
            assertThrows(IllegalArgumentException.class, () -> log2(-1.0));
        }

        @Test
        @DisplayName("Monotonicity: log2(x) increases with x")
        void testMonotonicity() {
            double x1 = 0.5, x2 = 2.5;
            double ln1 = log2(x1);
            double ln2 = log2(x2);
            assertTrue(ln1 < ln2,
                    () -> String.format("Expected ln(%.3f)=%.5f < ln(%.3f)=%.5f", x1, ln1, x2, ln2));
        }
    }

    @Test
    @DisplayName("Mock Test")
    void log2_ShouldCallLnWithCorrectArguments() {
        try (MockedStatic<Ln> mockedFuncLog = mockStatic(Ln.class)) {
            double x = 8.0;
            double mockedLnX = 2.0794415416798359;
            double mockedLn2 = 0.6931471805599453;

            mockedFuncLog.when(() -> ln(x)).thenReturn(mockedLnX);
            mockedFuncLog.when(() -> ln(2.0)).thenReturn(mockedLn2);

            double result = log2(x);

            assertEquals(3.0, result, DEFAULT_EPS);
            mockedFuncLog.verify(() -> ln(x));
            mockedFuncLog.verify(() -> ln(2.0));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0.1", "0.5", "1.5", "3.7", "8.0", "10.0"
    })
    @DisplayName("Parameterized test for checking different values")
    void log2ShouldCallLnWithParameters(double x) {
        try (MockedStatic<Ln> mockedLn = mockStatic(Ln.class)) {
            double mockLnX = Math.random();
            double mockLn2 = Math.random();

            mockedLn.when(() -> ln(x)).thenReturn(mockLnX);
            mockedLn.when(() -> ln(2.0)).thenReturn(mockLn2);

            double expected = mockLnX / mockLn2;
            double actual = log2(x);

            assertEquals(expected, actual, DEFAULT_EPS, "Crashed for x = " + x);
            mockedLn.verify(() -> ln(x));
            mockedLn.verify(() -> ln(2.0));
        }
    }
}
