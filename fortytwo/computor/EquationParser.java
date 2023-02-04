package fortytwo.computor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationParser {

    private static final Pattern number = Pattern.compile("\\d+(.\\d+)?");
    private static final Pattern multiplicationSign = Pattern.compile(" *\\* *");
    private static final Pattern equationSign = Pattern.compile(" *= *");
    private static final Pattern sign = Pattern.compile("( *(\\+|-) *)?");
    private static final Pattern variable = Pattern.compile("X(\\^\\d+)?");
    private static final Pattern termRegEx = Pattern.compile(
            "(" + sign + number + multiplicationSign + variable
                    + ")|(" + sign + number
                    + ")|(" + sign + variable + ")");

    private  static final Pattern equationRegEx = Pattern.compile("(" + termRegEx + ")+" + equationSign + "(" + termRegEx + ")+");

    public static boolean isEquationFormRight(String equation) {
        return equationRegEx.matcher(equation).matches();
    }

    private static Term getTermFromString(String term) {
        term = term.replace(" ", "");
        if (term.contains("*")) {
            int lastMultiplierIndex = 0;
            int firstDegreeIndex = term.length() - 1;
            for (; term.charAt(lastMultiplierIndex) != '*'; ++lastMultiplierIndex);
            for (; Character.isDigit(term.charAt(firstDegreeIndex)); --firstDegreeIndex);
            return new Term(
                    Double.parseDouble(term.substring(0, lastMultiplierIndex)),
                    term.length() != firstDegreeIndex + 1 ? Integer.valueOf(term.substring(firstDegreeIndex + 1)) : 0
            );
        } else if (term.matches(number.pattern())) {
            return new Term(
                    Double.parseDouble(term),
                    0
            );
        } else {
            int firstDegreeIndex = term.length() - 1;
            for (; Character.isDigit(term.charAt(firstDegreeIndex)); --firstDegreeIndex);
            return new Term(
                    1,
                    term.length() != firstDegreeIndex + 1 ? Integer.parseInt(term.substring(firstDegreeIndex + 1)) : 0
            );
        }
    }

    public static List<Term> getEquationAsTerms(String equation) {

        String[] equationParts = equation.split("=");
        List<Term> terms = new ArrayList<>();

        Matcher matcher = termRegEx.matcher(equationParts[0]);
        while (matcher.find()) {
            terms.add(getTermFromString(equationParts[0].substring(matcher.start(), matcher.end())));
        }
        matcher = termRegEx.matcher(equationParts[1]);
        while (matcher.find()) {
            terms.add(getTermFromString(equationParts[1].substring(matcher.start(), matcher.end())).reverseMultiplierSign());
        }
        return terms;
    }
}
