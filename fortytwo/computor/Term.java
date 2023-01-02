package fortytwo.computor;

import java.math.BigDecimal;

public class Term {

    private double multiplier;
    private final int degree;

    public Term(double multiplier, int degree) {
        this.multiplier = multiplier;
        this.degree = degree;
    }

    public Term add(Term other) {
        return new Term(this.multiplier + other.multiplier, degree);
    }

    public Term reverseMultiplierSign() {
        multiplier *= -1;
        return this;
    }

    public int getDegree() {
        return degree;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        // TODO: for degree == 0
        return multiplier + " * X^" + degree;
    }
}
