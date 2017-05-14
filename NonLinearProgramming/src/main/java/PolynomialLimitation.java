/**
 * Covered data of polynomial limitation, lets user to check if provided point is
 * fulfil by for limitation and could represent it in String.
 *
 * Created by Szymon on 12.05.2017.
 */
public class PolynomialLimitation extends PolynomialForm {

    private boolean greaterOrEqual;

    private Double constantTerm;

    /**
     * Check if table with double values in argument is good for this limitation.
     * Throw exception if dimensions inequality.
     *
     * @param coordinates table with double values might be sended to check.
     * @return information is the sended point is good for limitation or not.
     * @throws DifferentDimensionException Threw if arguments lengths
     *      are not equal.
     */
    public boolean checkCoordinates(Double[] coordinates) throws DifferentDimensionException {
        if(greaterOrEqual)
            return calculatePolynomialValue(coordinates) >= constantTerm;
        else
            return calculatePolynomialValue(coordinates) <= constantTerm;
    }

    /**
     * Represents the polynomial limitation in one String.
     *
     * @return String with info about polynomial limitation. If there is null in information,
     *      null written.
     */
    @Override
    public String toString() {
        StringBuilder limitBuilder = new StringBuilder();

        for(int i = 0; i < coefficients.length; ++i)
        {
            limitBuilder.append(coefficients[i]);
            limitBuilder.append(" x_(");
            limitBuilder.append(i+1);
            limitBuilder.append(")^(");
            limitBuilder.append(indicators[i]);
            limitBuilder.append(")");
            if (i != coefficients.length - 1)
                limitBuilder.append(" + ");
        }

        if(greaterOrEqual)
            limitBuilder.append(" >= ");
        else
            limitBuilder.append(" <= ");
        limitBuilder.append(constantTerm);
        limitBuilder.append("\n");

        return limitBuilder.toString();
    }

    /**
     * Simple constructor sets the private fields of goal function. Check if
     * any argument is null, then exit program with information about reason.
     * Check if arguments have same length in this case throw exception.
     *
     * @param coefficients One for every dimension.
     * @param indicators One for every dimension.
     * @param constantTerm Constant value connected with limitation.
     * @param greaterOrEqual Inequality token, inform if this token i greaterOrEqual.
     * @throws DifferentDimensionException Threw if arguments lengths
     *      are not equal.
     */
    PolynomialLimitation(Double[] coefficients, Integer[] indicators, Double constantTerm,
                         Boolean greaterOrEqual) throws DifferentDimensionException {
        super(coefficients,indicators);
        if(constantTerm == null || greaterOrEqual == null) {
            System.err.printf("Program exit, null founded during create polynomial limit.");
            System.exit(1);
        }
        this.constantTerm = constantTerm;
        this.greaterOrEqual = greaterOrEqual;
    }
}