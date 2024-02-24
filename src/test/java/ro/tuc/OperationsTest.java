package ro.tuc;

import org.junit.Test;
import org.junit.jupiter.api.*;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationsTest {
    static final Logger logger = Logger.getLogger(OperationsTest.class.getName());

    public OperationsTest() {
        new Entry();
    }

    @BeforeAll
    static void initAll() {
        logger.info("Tests Begin");
    }

    @BeforeEach
    void init(TestInfo testInfo) {
        logger.info(() -> String.format("About to execute [%s]",
                testInfo.getDisplayName()));
    }

    @Test
    public void additionValid() {
        String first = "2x + 3";
        String second = "x2 + x + 4";
        Polynomial result = Operations.addition(new Polynomial(first), new Polynomial(second));
        assertEquals("x^2 + 3x + 7", Entry.test(first, second, result));
    }

    @Test
    public void additionInvalid() {
        String first = "5x^^3 + 3";
        String second = "x + x + 4";
        Polynomial result = Operations.addition(new Polynomial(first), new Polynomial(second));
        assertEquals("invalid", Entry.test(first, second, result));
    }

    @Test
    public void subtractionValid() {
        String first = "4x3 + 2x2";
        String second = "3x2 - x2 + 7";
        Polynomial result = Operations.subtraction(new Polynomial(first), new Polynomial(second));
        assertEquals("4x^3 - 7", Entry.test(first, second, result));
    }

    @Test
    public void subtractionInvalid() {
        String first = "5x3 + 3S";
        String second = "x - x + 4";
        Polynomial result = Operations.subtraction(new Polynomial(first), new Polynomial(second));
        assertEquals("invalid", Entry.test(first, second, result));
    }

    @Test
    public void multiplicationValid() {
        String first = "2x + 5";
        String second = "3x - x2 + 7";
        Polynomial result = Operations.multiplication(new Polynomial(first), new Polynomial(second));
        assertEquals("-2x^3 + x^2 + 29x + 35", Entry.test(first, second, result));
    }

    @Test
    public void multiplicationInvalid() {
        String first = "5x3 /+ 3";
        String second = "x - x + 4";
        Polynomial result = Operations.multiplication(new Polynomial(first), new Polynomial(second));
        assertEquals("invalid", Entry.test(first, second, result));
    }

    @Test
    public void divisionValid() {
        String first = "4x3 + 2x2";
        String second = "2x2 - 3x";
        String result = Operations.division(new Polynomial(first), new Polynomial(second));
        assertEquals("2x + 4, R: 12x", result);
    }

    @Test
    public void divisionInvalid() {
        String first = "5x3 + 3x";
        String second = "0";
        String result = Operations.division(new Polynomial(first), new Polynomial(second));
        assertEquals("NaN", result);
    }

    @Test
    public void derivativeValid() {
        String first = "4x3 + 2x2";
        String second = "";
        Polynomial result = Operations.derivative(new Polynomial(first));
        assertEquals("12x^2 + 4x", Entry.test(first, second, result));
    }

    @Test
    public void derivativeInvalid() {
        String first = "5x3 + 3xx";
        String second = "";
        Polynomial result = Operations.derivative(new Polynomial(first));
        assertEquals("invalid", Entry.test(first, second, result));
    }

    @Test
    public void integrationValid() {
        String first = "4x3 + 3x2";
        String second = "";
        Polynomial result = Operations.integration(new Polynomial(first));
        assertEquals("x^4 + x^3", Entry.test(first, second, result));
    }

    @Test
    public void integrationInvalid() {
        String first = "40z / 20 + 1";
        String second = "";
        Polynomial result = Operations.integration(new Polynomial(first));
        assertEquals("invalid", Entry.test(first, second, result));
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        logger.info(() -> String.format("About to execute [%s]",
                testInfo.getDisplayName()));
    }

    @AfterAll
    public static void tearDownAll(TestInfo testInfo) {
        logger.info(() -> String.format("Finished executing [%s]",
                testInfo.getDisplayName()));
    }

}
