/**
 * @author Artur Vasilov
 */
public class Demo {

    public static void main(String[] args) {
        //QuantumRegister register = new QuantumRegister(1, "0");
        //System.out.println(BooleanFunction.arrayToString(register.measure()));

        //register.apply(Operators.hadamar());
        //System.out.println(BooleanFunction.arrayToString(register.measure()));

      //  QuantumRegister register = new QuantumRegister(5, "00000");
       // register.applyAtPositions(Operators.hadamar().tensorPow(2), 2);
        //System.out.println(BooleanFunction.arrayToString(register.measure()));

        BooleanFunction xorFunction = new BooleanFunction(4) {
            @Override
            protected boolean actualCall(boolean[] arguments) {
                return arguments[0] ^ arguments[1];
            }
        };

        QuantumRegister register = new QuantumRegister(5, "00001");
        register.apply(Operators.hadamar().tensorPow(5));
        register.apply(Operators.oracle(xorFunction));
        register.applyAtPositions(Operators.hadamar().tensorPow(4), 0);

        String measureResult = BooleanFunction.arrayToString(register.measure());
        boolean isConstant = measureResult.substring(0, measureResult.length() - 1).equals("0000");
        System.out.println("x + y is balanced? " + !isConstant);
    }

}
