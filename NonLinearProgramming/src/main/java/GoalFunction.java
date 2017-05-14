/**
 * Covered data of goal functions, lets user to calculate values of coordinates
 * for holds function and could represent it in String.
 *
 * Created by Szymon on 12.05.2017.
 */
public class GoalFunction extends PolynomialForm {

    private Boolean maximalize;

    /**
     * Information about inequality token. Might be null if null provided
     * in constructor.
     *
     * @return Information if the function is going to maximalize.
     */
    public Boolean getMaximalize() { return maximalize; }

    /**
     * For set of coordinates return value of holding goal function. If there is difference
     * in dimension or null sended, return null.
     *
     * @param coordinates set of double values represents point in universe.
     * @return value of holding goal function, null if there is difference
     *                  in dimension or null sended.
     */
    public Double calculateFunctionValue(Double[] coordinates) {
        return calculatePolynomialValue(coordinates);
    }

    /**
     * Represents the goal function in one String.
     *
     * @return String with info about function. If there is null in information, null
     *      written.
     */
    @Override
    public String toString() {
        StringBuilder functionBuilder = new StringBuilder();

        functionBuilder.append("F (...) = ");
        for(int i = 0; i < coefficients.length; ++i)
        {
            functionBuilder.append(coefficients[i]);
            functionBuilder.append(" x_");
            functionBuilder.append(i+1);
            functionBuilder.append("^(");
            functionBuilder.append(indicators[i]);
            functionBuilder.append(") ");
            if (i != coefficients.length - 1)
                functionBuilder.append("+ ");
        }

        if(maximalize)
            functionBuilder.append(" <- maximalize\n");
        else
            functionBuilder.append(" <- minimalize\n");
        return functionBuilder.toString();
    }

    /**
     * Simple constructor sets the private fields of goal function. Do not check for
     * null or not equality.
     *
     * @param coefficients One for every dimension.
     * @param indicators One for every dimension.
     * @param maximalize Information about inequality token.
     */
    GoalFunction(Double[] coefficients, Integer[] indicators, Boolean maximalize) {
        super(coefficients,indicators);
        this.maximalize = maximalize;
    }
}