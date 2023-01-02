package fortytwo.computor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class EquationHandler {

    private String equation = null;
    private String reducedEquation;
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
        CANT_SOLVE
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
        System.out.println("!!!" + firstRoot + " " + secondRoot); // TODO: remove sout
    }
    public String getEquation() {
        return "";
    }

    public String getReducedForm() {

        if (solutionStatus == Solution.UNSET || solutionStatus == Solution.UNSOLVED) {
            throw new ArithmeticException("An equation was not calculated yet");
        }
        return "";
    }

    @Override
    public String toString() {
        return getEquation();
    }
}
