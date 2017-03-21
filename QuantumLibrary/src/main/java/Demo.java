/**
 * @author Artur Vasilov
 */
public class Demo {

    public static void main(String[] args) {
        QuantumRegister register = new QuantumRegister(1, "0");
        System.out.println(BooleanFunction.arrayToString(register.measure()));

        ComplexMatrix hadamarOperator = new ComplexMatrix(2);
        hadamarOperator.setValue(0, 0, new Complex(1 / Math.sqrt(2), 0));
        hadamarOperator.setValue(0, 1, new Complex(1 / Math.sqrt(2), 0));
        hadamarOperator.setValue(1, 0, new Complex(1 / Math.sqrt(2), 0));
        hadamarOperator.setValue(1, 1, new Complex(-1 / Math.sqrt(2), 0));
        register.apply(hadamarOperator);

        System.out.println(BooleanFunction.arrayToString(register.measure()));
    }

}
