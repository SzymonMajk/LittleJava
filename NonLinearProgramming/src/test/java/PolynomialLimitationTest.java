import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Szymon on 14.05.2017.
 */
public class PolynomialLimitationTest {
    @Test
    public void checkCoordinatesNotEqual() throws Exception {
        Double[] testCoef = {2.0,3.0,-1.0};
        Integer[] testIndi = {1,1,2};
        Double[] coords = {3.0,1.0,6.0};
        Double testConstant = 2.0;

        PolynomialLimitation testLimit = new
                PolynomialLimitation(testCoef,testIndi,testConstant,false);
        assertTrue(testLimit.checkCoordinates(coords));
        testLimit = new
                PolynomialLimitation(testCoef,testIndi,testConstant,true);
        assertFalse(testLimit.checkCoordinates(coords));
    }

    @Test
    public void checkCoordinatesEqual() throws Exception {
        Double[] testCoef = {5.0,2.0,-1.0};
        Integer[] testIndi = {1,1,1};
        Double[] coords = {2.0,-1.0,2.0};
        Double testConstant = 6.0;

        PolynomialLimitation testLimit = new
                PolynomialLimitation(testCoef,testIndi,testConstant,false);
        assertTrue(testLimit.checkCoordinates(coords));
        testLimit = new
                PolynomialLimitation(testCoef,testIndi,testConstant,true);
        assertTrue(testLimit.checkCoordinates(coords));
    }
}