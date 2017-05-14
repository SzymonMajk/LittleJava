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
     * and indicators from private fields. If null gained, inform by standard
     * error output about problems and end program. If dimension difference
     * announced, throw exception. We may be sure about not null and dimension
     * equality of private fields, because they are set and check in constructor.
     *
     * @param coordinates double table with values of point for every dimension. In case
     *                    of null program stop, if dimension with private fields is different
     *                    exception thrown.
     * @return If all length are equal and no null gained, return value for coordinates
     *      otherwise return null.
     * @throws DifferentDimensionException Threw if arguments lengths
     *      are not equal.
     */
    protected Double calculatePolynomialValue(Double[] coordinates) throws DifferentDimensionException {
        if(coordinates == null) {
            System.err.printf("Program exit, null founded during calculating value for point.");
            System.exit(1);
        }
        if(coordinates.length != coefficients.length)
            throw new DifferentDimensionException();

        Double result = new Double(0);

        for(int i = 0; i < coordinates.length; ++i)
            result += coefficients[i] * Math.pow(coordinates[i],indicators[i]);
        return result;
    }

    /**
     * Simple constructor set arguments in private fields. Check if
     * any argument is null, then exit program with information about reason.
     * Check if arguments have same length in this case throw exception.
     *
     * @param coefficients One for every dimension.
     * @param indicators One for every dimension.
     * @throws DifferentDimensionException Throwned if arguments lengths
     *      are not equal.
     */
    protected PolynomialForm(Double[] coefficients, Integer[] indicators)
            throws DifferentDimensionException {
        if(coefficients == null || indicators == null) {
            System.err.printf("Program exit, null founded during create polynomial.");
            System.exit(1);
        }
        if(coefficients.length != indicators.length)
            throw new DifferentDimensionException();
        this.coefficients = coefficients;
        this.indicators = indicators;
    }
}