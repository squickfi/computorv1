import static java.lang.System.exit;

public class Computor {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Wrong number of arguments: 1 required");
            exit(1);
        }
        EquationHandler equationHandler = new EquationHandler();
        if (equationHandler.isEquationFormRight(args[0])) {
            equationHandler.setEquation(args[0]);
            equationHandler.calculate();
            System.out.println(equationHandler.getResult());
        } else {
            System.err.println("Wrong input format");
            exit(2);
        }

    }
}
