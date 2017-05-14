/**
 * Base class for goal function and polynomial limitations. Hold coefficients
 * and indicators in double tables, which are set in constructor. Provides method
 * which can calculate polynomial value for point from argument.
 *
 * Created by Szymon on 12.05.2017.
 */
public class PolynomialForm {

    /**
     * Coefficients of polynomial. One for every dimension. Might be null
     * if null in constructor.
     */
    protected Double[] coefficients;

    /**
     * Indicators of polynomial. One for every dimension. Might be null
     * if null in constructor.
     */
    protected Integer[] indicators;

    /**
     * Return value calculated using coordinates from argument, with coordinates
     * and indicators from private fields. They might be null or not equal to
     * coordinates length in that case, null return.
     *
     * @param coordinates double table with values of point for every dimension. Might
     *                    be null.
     * @return If all length are equal and no null gained, return value for coordinates
     *      otherwise return null.
     */
    protected Double calculatePolynomialValue(Double[] coordinates) {
        if(coordinates == null || coefficients == null || indicators == null
                || coordinates.length != coefficients.length
                || coordinates.length != indicators.length)
            return null;
        Double result = new Double(0);

        for(int i = 0; i < coordinates.length; ++i)
            result += coefficients[i] * Math.pow(coordinates[i],indicators[i]);
        return result;
    }

    /**
     * Simple constructor set arguments in private fields. Does not check
     * if values are null and do not check if values are same length.
     *
     * @param coefficients One for every dimension. Might be null
     *                  if null in constructor.
     * @param indicators One for every dimension. Might be null
     *                  if null in constructor.
     */
    protected PolynomialForm(Double[] coefficients, Integer[] indicators) {
        this.coefficients = coefficients;
        this.indicators = indicators;
    }
}