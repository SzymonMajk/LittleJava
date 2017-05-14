import java.util.Random;

/**
 * Covered data of constant limitations, lets user to set this limitations, generate
 * table with double values, in range of those limitations, could change limitations
 * to be in range of provided point and could represent all limitations informations
 * in String.
 *
 * Created by Szymon on 12.05.2017.
 */
public class ConstantLimitations {

    private Double[] leftConstrants;

    private Double[] rightConstrants;

    /**
     * Lets user to set left and right limitation. If null gained or
     * dimension difference announced, inform in standard error output about
     * problems and do not change private fields, otherwise change private
     * fields.
     *
     * @param left double table with values of minimal limitations.
     * @param right double table with values of maximal limitations.
     */
    public void setConstrants(Double[] left, Double[] right) {
        if(left == null || right == null || left.length != right.length) {
            System.err.printf("Left limits and right limits must be same length!");
            return;
        }
        leftConstrants = left;
        rightConstrants = right;
    }

    /**
     * Change limitation so that, they are in range different for the point
     * or lesser if previous limitation was closer.
     *
     * @param p Point objecy to which limitations are getting closer.
     * @param range maximal distance between provided point and new limitations.
     */
    public void scaleConstrantsNearPoint(Point p, Double range){
        Double[] scalingCoords = p.getCoordinates();

        for(int i = 0; i < leftConstrants.length; ++i) {
            if(scalingCoords[i] - range < 0.0)
                leftConstrants[i] = 0.0;
            else
                leftConstrants[i] = scalingCoords[i] - range;
            if(scalingCoords[i] + range < rightConstrants[i])
                rightConstrants[i] = scalingCoords[i] + range;
        }
    }

    /**
     * Generate new point with random values, generated from range of constant
     * limitations.
     *
     * @return new double table witch values represents values of point in universe.
     */
    public Double[] generateCoordinates() {
        Random generator = new Random();
        Double[] newCoordinates = new Double[leftConstrants.length];
        for(int i = 0; i < leftConstrants.length; ++i) {
            newCoordinates[i] = leftConstrants[i] + ((rightConstrants[i]
                    -leftConstrants[i]))*generator.nextDouble();
        }

        return newCoordinates;
    }

    /**
     * Represents the constant limitations in one String.
     *
     * @return String with info about constant limitations. If there is null in information,
     *      null written.
     */
    @Override
    public String toString() {
        StringBuilder limitBuilder = new StringBuilder();

        for(int i = 0; i < leftConstrants.length; ++i)
        {
            limitBuilder.append(leftConstrants[i]);
            limitBuilder.append(" <= ");
            limitBuilder.append("x_(");
            limitBuilder.append(i+1);
            limitBuilder.append(") <= ");
            limitBuilder.append(rightConstrants[i]);
            limitBuilder.append("\n");
        }
        return limitBuilder.toString();
    }
}