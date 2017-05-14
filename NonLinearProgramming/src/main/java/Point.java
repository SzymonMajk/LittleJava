/**
 * Hold table of double values, represents coordinates in table length dimension
 * universe. Lets to return coordinates, and calculate value, where goal function
 * is necessary. Overrides toString method, where return String with point representation.
 *
 * Created by Szymon on 12.05.2017.
 */
public class Point {

    private Double[] coordinates;

    /**
     * Return point coordinates. Might be null if null sended in constructor.
     *
     * @return Might be null if null sended in constructor.
     */
    public Double[] getCoordinates() {
        return coordinates;
    }

    /**
     * Represent the point. If coordinates are null, then null in place
     * of coordiantes.
     *
     * @return If coordinates are null, then null in place of coordiantes.
     */
    @Override
    public String toString() {
        StringBuilder pointBuilder = new StringBuilder();

        pointBuilder.append("Point coordinates: ");
        for(Double d : coordinates) {
            pointBuilder.append(d);
            pointBuilder.append(" ");
        }

        return pointBuilder.toString();
    }

    /**
     * Set table from argument to private field. Check if
     * argument is null, then exit program with information about reason.
     *
     * @param coordinates represents coordinate for every dimension of point.
     */
    Point(Double[] coordinates) {
        if(coordinates == null) {
            System.err.printf("Program exit, null founded during create point.");
            System.exit(1);
        }
        this.coordinates = coordinates;
    }
}