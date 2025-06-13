 package system;

 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.params.ParameterizedTest;
 import org.junit.jupiter.params.provider.CsvFileSource;
 import org.junit.jupiter.params.provider.ValueSource;
 import org.mockito.MockedStatic;
 import ru.rmntim.functions.log.*;
 import ru.rmntim.functions.trigon.*;
 import ru.rmntim.system.LogarithmicSystemPart;
 import ru.rmntim.system.Solver;
 import ru.rmntim.system.TrigonometricSystemPart;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertTrue;
 import static org.mockito.Mockito.*;
 import static ru.rmntim.system.Solver.setEpsilon;
 import static ru.rmntim.system.Solver.solvingSystem;
 import static ru.rmntim.system.TrigonometricSystemPart.calculate1;
 import static ru.rmntim.system.LogarithmicSystemPart.calculate2;

 import static ru.rmntim.functions.log.Ln.ln;
 import static ru.rmntim.functions.log.Log2.log2;
 import static ru.rmntim.functions.log.Log3.log3;
 import static ru.rmntim.functions.log.Log5.log5;
 import static ru.rmntim.functions.log.Log10.log10;


 public class SolverSystemTest {
     private static final double DEFAULT_EPS = 1e-6;

     @BeforeEach
     void setup() {
         setEpsilon(DEFAULT_EPS);
     }

     @ParameterizedTest @CsvFileSource(resources = "/testLogWithAllStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void Second_AllStubs(double x,
                          double vLn, double v2, double v3, double v5, double v10,
                          double expected) {
         try (MockedStatic<Ln>     lnMock = mockStatic(Ln.class);
              MockedStatic<Log2>   m2     = mockStatic(Log2.class);
              MockedStatic<Log3>   m3     = mockStatic(Log3.class);
              MockedStatic<Log5>   m5     = mockStatic(Log5.class);
              MockedStatic<Log10>  m10    = mockStatic(Log10.class))
         {
             lnMock.when(() -> ln(x)).thenReturn(vLn);
             m2.when(()   -> log2(x)).thenReturn(v2);
             m3.when(()   -> log3(x)).thenReturn(v3);
             m5.when(()   -> log5(x)).thenReturn(v5);
             m10.when(()  -> log10(x)).thenReturn(v10);

             double actual = Solver.solvingSystem(x);
             assertEquals(expected, actual, DEFAULT_EPS);

             lnMock.verify(() -> Ln.ln(x), atLeastOnce());
             m2.verify(() -> Log2.log2(x), atLeastOnce());
             m3.verify(() -> Log3.log3(x), atLeastOnce());
             m5.verify(() -> Log5.log5(x), atLeastOnce());
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testLogWithAllFunc.csv", delimiter = ';', useHeadersInDisplayName = true)
     void Second_AllFunc(double x, double expected) {
         double actual = Solver.solvingSystem(x);
         assertEquals(expected, actual, DEFAULT_EPS);
     }

     @ParameterizedTest @CsvFileSource(resources = "/testLogWithLog2Log3Log5Log10Stubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void Second_Log2Log3Log5Log10_Stubs(double x,
                                         double vLn,
                                         double expected) {
         try (MockedStatic<Ln> lnMock = mockStatic(Ln.class)) {
             lnMock.when(() -> ln(x)).thenReturn(vLn);
             lnMock.when(() -> Ln.ln(2.0)).thenReturn(0.693147180559);
             lnMock.when(() -> Ln.ln(3.0)).thenReturn(1.098612288668);
             lnMock.when(() -> Ln.ln(5.0)).thenReturn(1.609437912);
             lnMock.when(() -> Ln.ln(10.0)).thenReturn(2.302585092);

             double actual = Solver.solvingSystem(x);
             assertEquals(expected, actual, 1e-3);

             lnMock.verify(() -> Ln.ln(x), atLeastOnce());
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testLogWithLog3Log5Log10Stubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void Second_Log3Log5Log10_Stubs(double x,
                                     double vLn, double v2,
                                     double expected) {
         try (MockedStatic<Ln>   lnMock = mockStatic(Ln.class);
              MockedStatic<Log2> m2     = mockStatic(Log2.class))
         {
             lnMock.when(() -> ln(x)).thenReturn(vLn);
             lnMock.when(() -> Ln.ln(2.0)).thenReturn(0.693147180559);
             lnMock.when(() -> Ln.ln(3.0)).thenReturn(1.098612288668);
             lnMock.when(() -> Ln.ln(5.0)).thenReturn(1.609437912);
             lnMock.when(() -> Ln.ln(10.0)).thenReturn(2.302585092);
             m2.when(()   -> log2(x)).thenReturn(v2);
             double actual = Solver.solvingSystem(x);
             assertEquals(expected, actual, 1e-3);

             lnMock.verify(() -> Ln.ln(x), atLeastOnce());
             m2.verify(() -> Log2.log2(x), atLeastOnce());
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testLogWithLog5Log10Stubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void Second_Log5Log10_Stubs(double x,
                                 double vLn, double v2, double v3,
                                 double expected) {
         try (MockedStatic<Ln>   lnMock = mockStatic(Ln.class);
              MockedStatic<Log2> m2     = mockStatic(Log2.class);
              MockedStatic<Log3> m3     = mockStatic(Log3.class))
         {
             lnMock.when(() -> ln(x)).thenReturn(vLn);
             lnMock.when(() -> Ln.ln(2.0)).thenReturn(0.693147180559);
             lnMock.when(() -> Ln.ln(3.0)).thenReturn(1.098612288668);
             lnMock.when(() -> Ln.ln(5.0)).thenReturn(1.609437912);
             lnMock.when(() -> Ln.ln(10.0)).thenReturn(2.302585092);
             m2.when(()   -> log2(x)).thenReturn(v2);
             m3.when(()   -> log3(x)).thenReturn(v3);
             double actual = Solver.solvingSystem(x);
             assertEquals(expected, actual, 1e-3);

             lnMock.verify(() -> Ln.ln(x), atLeastOnce());
             m2.verify(() -> Log2.log2(x), atLeastOnce());
             m3.verify(() -> Log3.log3(x), atLeastOnce());
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testLogWithLog10Stubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void Second_Log10_Stubs(double x,
                             double vLn, double v2, double v3, double v5,
                             double expected) {
         try (MockedStatic<Ln>   lnMock = mockStatic(Ln.class);
              MockedStatic<Log2> m2     = mockStatic(Log2.class);
              MockedStatic<Log3> m3     = mockStatic(Log3.class);
              MockedStatic<Log5> m5     = mockStatic(Log5.class))
         {
             lnMock.when(() -> ln(x)).thenReturn(vLn);
             lnMock.when(() -> Ln.ln(2.0)).thenReturn(0.693147180559);
             lnMock.when(() -> Ln.ln(3.0)).thenReturn(1.098612288668);
             lnMock.when(() -> Ln.ln(5.0)).thenReturn(1.609437912);
             lnMock.when(() -> Ln.ln(10.0)).thenReturn(2.302585092);

             m2.when(()   -> log2(x)).thenReturn(v2);
             m3.when(()   -> log3(x)).thenReturn(v3);
             m5.when(()   -> log5(x)).thenReturn(v5);
             double actual = Solver.solvingSystem(x);
             assertEquals(expected, actual, 1e-3);

             lnMock.verify(() -> Ln.ln(x), atLeastOnce());
             m2.verify(() -> Log2.log2(x), atLeastOnce());
             m3.verify(() -> Log3.log3(x), atLeastOnce());
             m5.verify(() -> Log5.log5(x), atLeastOnce());
         }
     }


     @ParameterizedTest @CsvFileSource(resources = "/testTrigWithAllStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void first_AllStubs(double x,
                         double vSin, double vCos, double vTan,
                         double vCot, double vSec, double vCsc,
                         double expected) {
         try (MockedStatic<Sin>  mSin = mockStatic(Sin.class);
              MockedStatic<Cos>  mCos = mockStatic(Cos.class);
              MockedStatic<Tan>  mTan = mockStatic(Tan.class);
              MockedStatic<Cot>  mCot = mockStatic(Cot.class);
              MockedStatic<Sec>  mSec = mockStatic(Sec.class);
              MockedStatic<Csc>  mCsc = mockStatic(Csc.class))
         {
             mSin.when(() -> Sin.sin(x)).thenReturn(vSin);
             mCos.when(() -> Cos.cos(x)).thenReturn(vCos);
             mTan.when(() -> Tan.tan(x)).thenReturn(vTan);
             mCot.when(() -> Cot.cot(x)).thenReturn(vCot);
             mSec.when(() -> Sec.sec(x)).thenReturn(vSec);
             mCsc.when(() -> Csc.csc(x)).thenReturn(vCsc);

             double actual = Solver.solvingSystem(x);
             assertTrue(Math.abs(expected - actual) / Math.abs(expected) < 1e-9);
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testTrigWithAllFunc.csv", delimiter = ';', useHeadersInDisplayName = true)
     void first_AllFunc(double x, double expected) {
         double actual = Solver.solvingSystem(x);
         assertTrue(Math.abs(expected - actual) / Math.abs(expected) < 1e-9);
     }

     @ParameterizedTest @CsvFileSource(resources = "/testTrigWithCosTanCotSecCscStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void first_CosTanSecCsc_Stubs(double x, double vSin, double expected) {
         try (MockedStatic<Sin> mSin = mockStatic(Sin.class)) {
             mSin.when(() -> Sin.sin(x)).thenReturn(vSin);
             mSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(Math.sin(x + Math.PI / 2));

             double actual = Solver.solvingSystem(x);
             assertTrue(Math.abs(expected - actual) / Math.abs(expected) < 1e-3);

             mSin.verify(() -> Sin.sin(x), atLeastOnce());
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testTrigWithTanCotSecCscStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void first_TanCotSecCsc_Stubs(double x, double vSin, double vCos, double expected) {
         try (MockedStatic<Sin> mSin = mockStatic(Sin.class);
              MockedStatic<Cos> mCos = mockStatic(Cos.class))
         {
             mSin.when(() -> Sin.sin(x)).thenReturn(vSin);
             mSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(Math.sin(x + Math.PI / 2));
             mCos.when(() -> Cos.cos(x)).thenReturn(vCos);
             double actual = Solver.solvingSystem(x);

             assertTrue(Math.abs(expected - actual) / Math.abs(expected) < 1e-3);
             mSin.verify(() -> Sin.sin(x), atLeastOnce());
             mCos.verify(() -> Cos.cos(x), atLeastOnce());
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testTrigWithCotSecCscStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void first_CotSecCsc_Stubs(double x, double vSin, double vCos, double vTan, double expected) {
         try (MockedStatic<Sin> mSin = mockStatic(Sin.class);
              MockedStatic<Cos> mCos = mockStatic(Cos.class);
              MockedStatic<Tan> mTan = mockStatic(Tan.class))
         {
             mSin.when(() -> Sin.sin(x)).thenReturn(vSin);
             mSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(Math.sin(x + Math.PI / 2));
             mCos.when(() -> Cos.cos(x)).thenReturn(vCos);
             mTan.when(() -> Tan.tan(x)).thenReturn(vTan);

             double actual = Solver.solvingSystem(x);
             assertTrue(Math.abs(expected - actual) / Math.abs(expected) < 1e-3);
             mSin.verify(() -> Sin.sin(x), atLeastOnce());
             mCos.verify(() -> Cos.cos(x), atLeastOnce());
             mTan.verify(() -> Tan.tan(x), atLeastOnce());
         }
     }

     @ParameterizedTest @CsvFileSource(resources = "/testTrigWithSecCscStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void first_SecCsc_Stubs(double x, double vSin, double vCos, double vTan, double vCot, double expected) {
         try (MockedStatic<Sin> mSin = mockStatic(Sin.class);
              MockedStatic<Cos> mCos = mockStatic(Cos.class);
              MockedStatic<Tan> mTan = mockStatic(Tan.class);
              MockedStatic<Cot> mCot = mockStatic(Cot.class))
         {
             mSin.when(() -> Sin.sin(x)).thenReturn(vSin);
             mSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(Math.sin(x + Math.PI / 2));
             mCos.when(() -> Cos.cos(x)).thenReturn(vCos);
             mTan.when(() -> Tan.tan(x)).thenReturn(vTan);
             mCot.when(() -> Cot.cot(x)).thenReturn(vCot);

             double actual = Solver.solvingSystem(x);
             assertTrue(Math.abs(expected - actual) / Math.abs(expected) < 1e-3);

             mSin.verify(() -> Sin.sin(x), atLeastOnce());
             mCos.verify(() -> Cos.cos(x), atLeastOnce());
             mTan.verify(() -> Tan.tan(x), atLeastOnce());
             mCot.verify(() -> Cot.cot(x), atLeastOnce());
         }
     }

     @ParameterizedTest
     @CsvFileSource(resources = "/testTrigWithCscStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
     void first_Csc_Stubs(double x, double vSin, double vCos, double vTan, double vCot, double vSec, double expected) {
         try (MockedStatic<Sin> mSin = mockStatic(Sin.class);
              MockedStatic<Cos> mCos = mockStatic(Cos.class);
              MockedStatic<Tan> mTan = mockStatic(Tan.class);
              MockedStatic<Cot> mCot = mockStatic(Cot.class);
              MockedStatic<Sec> mSec = mockStatic(Sec.class))
         {
             mSin.when(() -> Sin.sin(x)).thenReturn(vSin);
             mSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(Math.sin(x + Math.PI / 2));
             mCos.when(() -> Cos.cos(x)).thenReturn(vCos);
             mTan.when(() -> Tan.tan(x)).thenReturn(vTan);
             mCot.when(() -> Cot.cot(x)).thenReturn(vCot);
             mSec.when(() -> Sec.sec(x)).thenReturn(vSec);

             double actual = Solver.solvingSystem(x);
             assertTrue(Math.abs(expected - actual) / Math.abs(expected) < 1e-3);

             mSin.verify(() -> Sin.sin(x), atLeastOnce());
             mCos.verify(() -> Cos.cos(x), atLeastOnce());
             mTan.verify(() -> Tan.tan(x), atLeastOnce());
             mCot.verify(() -> Cot.cot(x), atLeastOnce());
             mSec.verify(() -> Sec.sec(x), atLeastOnce());
         }
     }

     @Test
     void testProcess_xIsNegativeOrZero() {
         try (MockedStatic<TrigonometricSystemPart> mockedTrigPartStatic = mockStatic(TrigonometricSystemPart.class);
              MockedStatic<LogarithmicSystemPart> mockedLogPartStatic = mockStatic(LogarithmicSystemPart.class)) {

             double expectedTrigResult = -1.0;

             mockedTrigPartStatic.when(() -> calculate1(-5.0)).thenReturn(expectedTrigResult);
             mockedTrigPartStatic.when(() -> calculate1(0.0)).thenReturn(expectedTrigResult);

             assertEquals(expectedTrigResult, solvingSystem(-5.0));
             assertEquals(expectedTrigResult, solvingSystem(0.0));
         }
     }

     @Test
     void testProcess_xIsPositive() {
         try (MockedStatic<TrigonometricSystemPart> mockedTrigPartStatic = mockStatic(TrigonometricSystemPart.class);
              MockedStatic<LogarithmicSystemPart> mockedLogPartStatic = mockStatic(LogarithmicSystemPart.class)) {

             double expectedLogResult = 2.5;

             mockedLogPartStatic.when(() -> calculate2(3.0)).thenReturn(expectedLogResult);

             assertEquals(expectedLogResult, solvingSystem(3.0));
         }
     }

     @ParameterizedTest(name = "Special dots")
     @CsvFileSource(resources = "/SpecialDots.csv", delimiter = ';', useHeadersInDisplayName = true)
     void testCosAtCustomPoints(double x, double y) {
         double actual = Solver.solvingSystem(x);
         assertEquals(
                 y, actual, 1e-3,
                 () -> String.format("cos(%.3f): expected %.3f but got %.3f", x, y, actual)
         );
     }

 }