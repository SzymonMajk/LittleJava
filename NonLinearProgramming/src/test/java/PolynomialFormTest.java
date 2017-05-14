import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Szymon on 14.05.2017.
 */
public class PolynomialFormTest {

    @Test
    public void calculatePolynomialValue() throws Exception {
        Double[] testCoef = {2.0,3.0,-1.0};
        Integer[] testIndi = {1,1,2};
        Double[] coords = {3.0,1.0,6.0};

        assertEquals(-27.0, new PolynomialForm(testCoef,testIndi).
                calculatePolynomialValue(coords),0.000005);
    }

    @Test
    public void calculatePolynomialValueWithEqual() throws Exception {
        Double[] testCoef = {5.0,-5.0,-5.0};
        Integer[] testIndi = {1,1,1};
        Double[] coords = {2.0,1.0,1.0};

        assertEquals(0.0, new PolynomialForm(testCoef,testIndi).
                calculatePolynomialValue(coords),0.0000005);
    }

    @Test (expected = DifferentDimensionException.class)
    public void calculatePolynomialValueExceptionInForm() throws Exception {
        Double[] testCoef = {5.0,-5.0};
        Integer[] testIndi = {1,1,1};
        Double[] coords = {2.0,1.0,1.0};

        assertEquals(-27.0, new PolynomialForm(testCoef,testIndi).
                calculatePolynomialValue(coords),0.000005);
    }

    @Test (expected = DifferentDimensionException.class)
    public void calculatePolynomialValueExceptionInCalculate() throws Exception {
        Double[] testCoef = {5.0,-5.0,-5.0};
        Integer[] testIndi = {1,1,1};
        Double[] coords = {2.0,1.0};

        assertEquals(-27.0, new PolynomialForm(testCoef,testIndi).
                calculatePolynomialValue(coords),0.000005);
    }

}