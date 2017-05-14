import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main program class with starting point. Lets user to introduce nonlinear
 * programming problem and uses monte carlo method to provide set of coordinates
 * with best goal function result.
 *
 * Created by Szymon on 12.05.2017.
 */
public class Solver {

    private Integer sampleNumber = 1000000;

    private GoalFunction goalFunction;

    private ConstantLimitations constantLimitations = new ConstantLimitations();

    private ArrayList<PolynomialLimitation> polynomialLimitations =
            new ArrayList<PolynomialLimitation>();

    private Double[] getBetterPoint(Double[] bestSet ,Double[] newSet) {
        if(bestSet == null)
            return newSet;
        if(goalFunction.getMaximalize()) {
            if(new Point(bestSet).calculatePointValue(goalFunction) <
                    new Point(newSet).calculatePointValue(goalFunction))
                return newSet;
            return bestSet;
        }
        else {
            if(new Point(bestSet).calculatePointValue(goalFunction) >
                    new Point(newSet).calculatePointValue(goalFunction))
                return newSet;
            return bestSet;
        }
    }

    private Integer inputDimesion(Scanner userInput) {
        Integer dimension = null;

        try {
            dimension = Integer.parseInt(userInput.nextLine());
        } catch (NumberFormatException e) {
            return null;
        }

        if(dimension > 0)
            return dimension;
        return null;
    }

    private boolean inputConstantLimitations(Scanner userInput, Integer dimension) {
        Double[] rightLimitations = new Double[dimension];
        Double[] leftLimitations = new Double[dimension];
        for(int i = 0; i < leftLimitations.length; ++i)
            leftLimitations[i] = 0.0;

        System.out.printf("One positive double value for every dimension separated " +
                "with white space: \n");
        try {
            String line = userInput.nextLine();
            String[] limitationsEntry = line.split("\\s");

            if(!dimension.equals(limitationsEntry.length)) {
                System.err.printf("Wrong input String format, write all " +
                        "limitations separated with white space! ");
                return false;
            }

            for(int i = 0; i < dimension; ++i) {
                Double limitation = Double.parseDouble(limitationsEntry[i]);
                if(limitation <= 0.0) {
                    System.err.printf("Right limit must be positive double value! ");
                    return false;
                }
                rightLimitations[i] = Double.parseDouble(limitationsEntry[i]);
            }
        } catch (NumberFormatException e) {
            System.err.printf("Limitation must be double value! ");
            return false;
        }
        constantLimitations.setConstrants(leftLimitations,rightLimitations);
        return true;
    }

    private boolean inputGoalFunction(Scanner userInput, Integer dimension) {
        boolean maximalize = false;
        Double[] coefficients = new Double[dimension];
        Integer[] indicators = new Integer[dimension];

        System.out.printf("In one line enter separated with white space coefficients " +
                "and in second line input separated indicators (positive integers), " +
                "both number must be equal to dimension: \n");
        try {
            String line = userInput.nextLine();
            String[] splitedEntry = line.split("\\s");

            if(!dimension.equals(splitedEntry.length)) {
                System.err.printf("Wrong input String format, write all " +
                        "coefficients separated with white space! ");
                return false;
            }

            for(int i = 0; i < dimension; ++i)
                coefficients[i] = Double.parseDouble(splitedEntry[i]);

            line = userInput.nextLine();
            splitedEntry = line.split("\\s");

            if(!dimension.equals(splitedEntry.length)) {
                System.err.printf("Wrong input String format, write all " +
                        "indicators separated with white space! ");
                return false;
            }

            for(int i = 0; i < dimension; ++i) {
                Integer indicator = Integer.parseInt(splitedEntry[i]);
                if(indicator <= 0.0) {
                    System.err.printf("Indicator must be positive integer! ");
                    return false;
                }
                indicators[i] = indicator;
            }
        } catch (NumberFormatException e) {
            System.err.printf("Coefficient must be double, indicator must be positive integer! ");
            return false;
        }

        System.out.printf("Maximalize goal function? [y/n]: ");
        String line = userInput.nextLine();
        if(line != null && !line.equals(""))
            if(line.charAt(0) == 'y' || line.charAt(0) == 'Y')
                maximalize = true;

        goalFunction = new GoalFunction(coefficients,indicators,maximalize);
        return true;
    }

    private Integer inputPolynomialLimitationsNumber(Scanner userInput) {
        Integer limitationsNumber = null;

        try {
            limitationsNumber = Integer.parseInt(userInput.nextLine());
        } catch (NumberFormatException e) {
            return null;
        }

        if(limitationsNumber > 0)
            return limitationsNumber;
        return null;
    }

    private boolean inputPolynomialLimitation(Scanner userInput, Integer dimension) {
        boolean greater = false;
        Double constantValue;
        Double[] coefficients = new Double[dimension];
        Integer[] indicators = new Integer[dimension];

        System.out.printf("In one line enter separated with white space coefficients " +
                "and in second line input separated indicators (positive integers), " +
                "both number must be equal to dimension: \n");
        try {
            String line = userInput.nextLine();
            String[] splitedEntry = line.split("\\s");

            if(!dimension.equals(splitedEntry.length)) {
                System.err.printf("Wrong input String format, write all " +
                        "coefficients separated with white space! ");
                return false;
            }

            for(int i = 0; i < dimension; ++i)
                coefficients[i] = Double.parseDouble(splitedEntry[i]);

            line = userInput.nextLine();
            splitedEntry = line.split("\\s");

            if(!dimension.equals(splitedEntry.length)) {
                System.err.printf("Wrong input String format, write all " +
                        "indicators separated with white space! ");
                return false;
            }

            for(int i = 0; i < dimension; ++i) {
                Integer indicator = Integer.parseInt(splitedEntry[i]);
                if(indicator <= 0.0) {
                    System.err.printf("Indicator must be positive integer! ");
                    return false;
                }
                indicators[i] = indicator;
            }
        } catch (NumberFormatException e) {
            System.err.printf("Coefficient must be double, indicator must be positive integer! ");
            return false;
        }

        System.out.printf("Enter constant value associated with this limitation: ");
        try {
            constantValue = Double.parseDouble(userInput.nextLine());
        } catch (NumberFormatException e) {
            return false;
        }

        System.out.printf("Greater or equal? [y/n]: ");
        String line = userInput.nextLine();
        if(line != null && !line.equals(""))
            if(line.charAt(0) == 'y' || line.charAt(0) == 'Y')
                greater = true;

        polynomialLimitations.
                add(new PolynomialLimitation(coefficients,indicators,constantValue,greater));
        return true;
    }

    /**
     * Lets user to specify math problem. Ask for dimension, goal function and limitations, both
     * constant and polynomial. In case of wrong entry, informs about mistake and repeat input.
     *
     * @param userInput Scanner objects with string lines, which specify details of math problem.
     *                  There is no check for null or empty value.
     */
    public void inputData(Scanner userInput) {
        Integer dimension = null;
        Integer numberOfPolynomialLimitations = null;

        System.out.printf("Enter dimension. ");
        while((dimension = inputDimesion(userInput)) == null)
            System.err.printf("Dimension must be positive integer.\n");
        System.out.printf("Enter constant limitations. ");
        while(!inputConstantLimitations(userInput,dimension))
            System.err.printf("Try again...\n");
        System.out.printf("Enter goal function. ");
        while(!inputGoalFunction(userInput,dimension))
            System.err.printf("Try again...\n");
        System.out.printf("Enter number of polynomial limitations. ");
        while((numberOfPolynomialLimitations = inputPolynomialLimitationsNumber(userInput)) == null)
            System.err.printf("This number must be positive integer.\n");
        for(int i = 0; i < numberOfPolynomialLimitations; ++i)
        {
            System.out.printf("Enter %d polynomial limitation. ",i+1);
            while(!inputPolynomialLimitation(userInput,dimension))
                System.err.printf("Try again...\n");
        }
    }

    /**
     * Simple function which shows details of provided problem in human readable way.
     */
    public void presentProblem() {
        System.out.printf("\n|---------------------|\nThe problem you set:\n");
        System.out.printf("Your goal function:\n" + goalFunction);
        System.out.printf("Your constant limitations:\n"+constantLimitations);
        System.out.printf("Your polynomial limitations:\n");
        for(PolynomialLimitation limit : polynomialLimitations)
            System.out.printf(limit.toString());
        System.out.printf("|---------------------|\n\n");
    }

    /**
     * Generate some sample vectors, check for every limitation and return one
     * witch best goal function value. Could return null if it is impossible to
     * generate appropriate point.
     *
     * @return Point object, with best set of coodrinates for this problem. Coordinates
     *      will be null if no set of coordinates could have been generated for limitations.
     */
    public Point proceedMonteCarlo() {
        Double[] bestCoordinates = null;
        Double range = 10.0;

        while(range > 0.000000001) {
            Double[] currentCoordinates;

            Generate : for(int j = 0; j < sampleNumber; ++j) {
                currentCoordinates = constantLimitations.generateCoordinates();
                for(PolynomialLimitation limit : polynomialLimitations)
                    if (!limit.checkCoordinates(currentCoordinates))
                        continue Generate;
                bestCoordinates = getBetterPoint(bestCoordinates,currentCoordinates);
            }
            range = range * 0.5;
            constantLimitations.scaleConstrantsNearPoint(new Point(bestCoordinates),range);
        }

        return new Point(bestCoordinates);
    }

    /**
     * Present details of the best point. If no point could have been found
     * inform about it and please to retry.
     *
     * @param best Point object with best set of coordinates or null value if no
     *             one could have been found.
     */
    public void presentResults(Point best) {
        if (best.getCoordinates() == null) {
            System.out.printf("Impossible to find solution, check limitations and try again.");
            return;
        }

        System.out.printf("Best point: "+best);
        System.out.printf("\nGoalFuntion value for best point: "
                + best.calculatePointValue(goalFunction));
    }

    /**
     * Simple presentation of possible best use of program. Ask user about mathematical
     * problem: dimension, goal function, constant and polynomial limitation, present
     * problem in command line and use monte carlo method to return best possible set
     * of coordinates for this problem, which is presented in command line too. If no
     * appropriate set of coordinates might have been found, inform user about problem.
     *
     * @param args not in use.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Solver s = new Solver();
        Point best;

        s.inputData(sc);
        s.presentProblem();
        best = s.proceedMonteCarlo();
        s.presentResults(best);
    }
}