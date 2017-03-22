/**
 * @author Artur Vasilov
 */
public class Demo {

    public static void main(String[] args) {
        QuantumRegister register = new QuantumRegister(1, "0");
        System.out.println(BooleanFunction.arrayToString(register.measure()));

        register.apply(Operators.hadamar());
        System.out.println(BooleanFunction.arrayToString(register.measure()));
    }

}
