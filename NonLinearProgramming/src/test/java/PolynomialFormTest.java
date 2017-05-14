import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Szymon on 14.05.2017.
 */
public class PolynomialFormTest {
    @Test
    public void calculatePolynomialValue() throws Exception {
        Double[] testCoef1 = {2.0,3.0,-1.0};
        Integer[] testIndi1 = {1,1,2};
        Double[] coords1 = {3.0,1.0,6.0};
        Double[] testCoef2 = {5.0,-5.0,-5.0};
        Integer[] testIndi2 = {1,1,1};
        Double[] coords2 = {2.0,1.0,1.0};
        Double[] testCoef3 = {2.0,3.0,-1.0};
        Integer[] testIndi3 = {1,-1,2};
        Double[] coords3 = {3.0,1.0,6.0};
        Double[] testCoef4 = {2.0,3.0,-1.0};
        Integer[] testIndi4 = {1,1};
        Double[] coords4 = {3.0,1.0,6.0};

        assertEquals(-27.0, new PolynomialForm(testCoef1,testIndi1).
                calculatePolynomialValue(coords1),0.000005);
        assertEquals(0.0, new PolynomialForm(testCoef2,testIndi2).
                calculatePolynomialValue(coords2),0.0000005);
        /*assertEquals(-27.0, new PolynomialForm(testCoef3,testIndi3).
                calculatePolynomialValue(coords3),0.000005);
        assertEquals(-27.0, new PolynomialForm(testCoef4,testIndi4).
                calculatePolynomialValue(coords4),0.000005);
         TODO exceptions
         */
    }

}