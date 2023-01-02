package fortytwo.computor;

import static java.lang.System.exit;

public class Computor {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Wrong number of arguments: 1 required");
            exit(1);
        }
        EquationHandler equationHandler = new EquationHandler();
        System.out.println(args[0]);
        if (equationHandler.isEquationFormRight(args[0])) {
            equationHandler.setEquation(args[0]);
            equationHandler.calculate();
            // TODO
        } else {
            System.err.println("Wrong input format"); // TODO
            exit(2);
        }

    }
}
