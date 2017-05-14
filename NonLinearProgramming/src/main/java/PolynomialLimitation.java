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
     * Return null in case of dimension difference or null sended.
     *
     * @param coordinates table with double values might be sended to check.
     * @return information is the sended point is good for limitation or not. Might
     *      be null in case of dimension difference or null gained.
     */
    public boolean checkCoordinates(Double[] coordinates) {
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
     * Simple constructor sets the private fields of goal function. Do not check for
     * null or not equality.
     *
     * @param coefficients One for every dimension.
     * @param indicators One for every dimension.
     * @param constantTerm Constant value connected with limitation.
     * @param greaterOrEqual Inequality token, inform if this token i greaterOrEqual.
     */
    PolynomialLimitation(Double[] coefficients, Integer[] indicators,
                         Double constantTerm, Boolean greaterOrEqual) {
        super(coefficients,indicators);
        this.constantTerm = constantTerm;
        this.greaterOrEqual = greaterOrEqual;
    }
}