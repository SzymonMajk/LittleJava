import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Szymon on 14.05.2017.
 */
public class PolynomialLimitationTest {
    @Test
    public void checkCoordinates() throws Exception {
        Double[] testCoef1 = {2.0,3.0,-1.0};
        Integer[] testIndi1 = {1,1,2};
        Double[] coords1 = {3.0,1.0,6.0};
        Double testConstant1 = 2.0;
        Double[] testCoef2 = {5.0,2.0,-1.0};
        Integer[] testIndi2 = {1,1,1};
        Double[] coords2 = {2.0,-1.0,2.0};
        Double testConstant2 = 6.0;

        PolynomialLimitation testLimit = new
                PolynomialLimitation(testCoef1,testIndi1,testConstant1,false);
        assertTrue(testLimit.checkCoordinates(coords1));
        testLimit = new
                PolynomialLimitation(testCoef1,testIndi1,testConstant1,true);
        assertFalse(testLimit.checkCoordinates(coords1));
        testLimit = new
                PolynomialLimitation(testCoef2,testIndi2,testConstant2,false);
        assertTrue(testLimit.checkCoordinates(coords2));
        testLimit = new
                PolynomialLimitation(testCoef2,testIndi2,testConstant2,true);
        assertTrue(testLimit.checkCoordinates(coords2));
    }
}