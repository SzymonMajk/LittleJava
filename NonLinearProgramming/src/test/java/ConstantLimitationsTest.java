import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Szymon on 14.05.2017.
 */
public class ConstantLimitationsTest {
    @Test
    public void scaleConstrantsNearPoint() throws Exception {
        Double[] startLeftLimits = {0.0,0.0};
        Double[] startRightLimits = {100.0,100.0};
        Double[] scallingCoords = {40.0, 90.0};
        Point scallingPoint = new Point(scallingCoords);

        ConstantLimitations c = new ConstantLimitations();
        c.setConstrants(startLeftLimits,startRightLimits);

        c.scaleConstrantsNearPoint(scallingPoint,20.0);
        assertEquals("20.0 <= x_(1) <= 60.0\n70.0 <= x_(2) <= 100.0\n",c.toString());
        c.scaleConstrantsNearPoint(scallingPoint,5.0);
        assertEquals("35.0 <= x_(1) <= 45.0\n85.0 <= x_(2) <= 95.0\n",c.toString());
    }

}