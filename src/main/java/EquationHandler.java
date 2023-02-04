import java.util.Arrays;
import java.util.List;

public class EquationHandler {

    private String equation = null;
    private String reducedEquation = "";
    private int polynominalDegree = 0;
    private List<Term> terms;
    private List<Term> reducedTerms;
    enum Solution {
        UNSET,
        UNSOLVED,
        NO_SOLUTIONS,
        ONE_SOLUTION,
        TWO_SOLUTIONS,
        INFINITE_SOLUTIONS,
        CANT_SOLVE,
    }
    private Solution solutionStatus = Solution.UNSET;

    private double firstRoot;
    private double secondRoot;

    public void setEquation(String equation) {
        this.equation = equation;
        solutionStatus = Solution.UNSOLVED;
    }
    public boolean isEquationFormRight(String equation) {
        return EquationParser.isEquationFormRight(equation);
    }

    private void findPolynomialDegree() {
        for (Term t : terms) {
            if (t.getDegree() > polynominalDegree) {
                polynominalDegree = t.getDegree();
            }
        }
    }

    private void calculateReducedEquation() {

        Term[] reduced = new Term[polynominalDegree + 1];
        for (int i = 0; i < polynominalDegree + 1; ++i) {
            reduced[i] = new Term(0, i);
        }
        for (Term t : terms) {
            reduced[t.getDegree()] = reduced[t.getDegree()].add(t);
        }
        reducedTerms = Arrays.asList(reduced);
        int i = reducedTerms.size() - 1;
        while (reducedTerms.get(i).getMultiplier() == 0 && i != 0) {
            reducedTerms.remove(i);
            --polynominalDegree;
            --i;
        }
        for (Term t : reducedTerms) {
            reducedEquation += t.toString().replace("-", "- ").replace("+", "+ ") + " ";
        }
        if (reducedEquation.startsWith("+")) {
            reducedEquation = reducedEquation.substring(2);
        }
        reducedEquation += "= 0";
    }

    public void calculate() {

        if (solutionStatus == Solution.UNSET) {
            throw new ArithmeticException("Can't calculate: equation is not set");
        }
        terms = EquationParser.getEquationAsTerms(equation);
        findPolynomialDegree();
        calculateReducedEquation();

        if (polynominalDegree == 0) {
            solutionStatus = reducedTerms.get(0).getMultiplier() == 0 ? Solution.INFINITE_SOLUTIONS : Solution.NO_SOLUTIONS;

        } else if (polynominalDegree == 1) {
            firstRoot = reducedTerms.get(0).getMultiplier() / (-1 * reducedTerms.get(1).getMultiplier());
            solutionStatus = Solution.ONE_SOLUTION;

        } else if (polynominalDegree == 2) {

            double discriminant = (Math.pow(reducedTerms.get(1).getMultiplier(), 2)) - (4 * reducedTerms.get(0).getMultiplier() * reducedTerms.get(2).getMultiplier());
            if (discriminant < 0) {
                solutionStatus = Solution.NO_SOLUTIONS;
            } else if (discriminant == 0) {
                solutionStatus = Solution.ONE_SOLUTION;
                firstRoot = (reducedTerms.get(1).getMultiplier() * -1) / (2 * reducedTerms.get(0).getMultiplier());
            } else {
                solutionStatus = Solution.TWO_SOLUTIONS;
                firstRoot = (reducedTerms.get(1).getMultiplier() * -1 + Math.sqrt(discriminant)) / (2 * reducedTerms.get(2).getMultiplier());
                secondRoot = (reducedTerms.get(1).getMultiplier() * -1 - Math.sqrt(discriminant)) / (2 * reducedTerms.get(2).getMultiplier());
            }
        } else {
            solutionStatus = Solution.CANT_SOLVE;
            return;
        }
    }

    public String getEquation() {
        return equation;
    }
    public String getReducedForm() {

        if (solutionStatus == Solution.UNSET || solutionStatus == Solution.UNSOLVED) {
            throw new ArithmeticException("An equation was not calculated yet");
        }
        return reducedEquation;
    }
    public int getPolynominalDegree() {
        return polynominalDegree;
    }

    public String getResult() {
        String result = "Reduced form: ";
        result += reducedEquation;
        result += "\nPolynomial degree: " + polynominalDegree;
        switch (solutionStatus) {
            case UNSET:
                return "An equation was not set to the handler";
            case UNSOLVED:
                return "The equation was not calculated yet";
            case INFINITE_SOLUTIONS:
                return result + "\nThe equation has infinite number of solutions";
            case NO_SOLUTIONS:
                return result + "\nThe equation has no solutions";
            case ONE_SOLUTION:
                if (polynominalDegree == 2) {
                    result += "\nDiscriminant equals to zero, there is one solution:\n";
                } else {
                    result += "\nThe solution is:\n";
                }
                return result + firstRoot;
            case TWO_SOLUTIONS:
                return result + "\nDiscriminant is strictly positive, the two solutions are:\n" + firstRoot + '\n' + secondRoot;
            case CANT_SOLVE:
                return result + "\nThe polynomial degree is strictly greater than 2, I can't solve";
        }
        return null;
    }

    @Override
    public String toString() {
        return getEquation();
    }
}
