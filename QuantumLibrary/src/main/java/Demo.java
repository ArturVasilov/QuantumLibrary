import ru.kpfu.arturvasilov.core.BooleanFunction;
import ru.kpfu.arturvasilov.core.Operators;
import ru.kpfu.arturvasilov.core.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class Demo {

    public static void main(String[] args) {
        //ru.kpfu.arturvasilov.core.QuantumRegister register = new ru.kpfu.arturvasilov.core.QuantumRegister(1, "0");
        //System.out.println(ru.kpfu.arturvasilov.core.BooleanFunction.arrayToString(register.measure()));

        //register.apply(ru.kpfu.arturvasilov.core.Operators.hadamar());
        //System.out.println(ru.kpfu.arturvasilov.core.BooleanFunction.arrayToString(register.measure()));

      //  ru.kpfu.arturvasilov.core.QuantumRegister register = new ru.kpfu.arturvasilov.core.QuantumRegister(5, "00000");
       // register.applyAtPositions(ru.kpfu.arturvasilov.core.Operators.hadamar().tensorPow(2), 2);
        //System.out.println(ru.kpfu.arturvasilov.core.BooleanFunction.arrayToString(register.measure()));

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
