import ru.kpfu.arturvasilov.core.BooleanFunction;
import ru.kpfu.arturvasilov.core.Operators;
import ru.kpfu.arturvasilov.core.computer.InitializationCallback;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;
import ru.kpfu.arturvasilov.core.universal.UniversalMemoryManager;

/**
 * @author Artur Vasilov
 */
public class Demo {

    public static void main(String[] args) {
        QuantumComputer.init(new UniversalMemoryManager(), new InitializationCallback() {
            @Override
            public void onInitializationSucceed() {
                Demo.onInitializationSucceed();
            }

            @Override
            public void onInitializationFailed(Throwable error) {
                Demo.onInitializationFailed(error);
            }
        });

    }

    private static void onInitializationSucceed() {
        /*QuantumRegister register = QuantumComputer.createNewRegister("00000");
        register.apply(Operators.hadamar().tensorPow(5));
        boolean[] result = register.measure();
        int number = BooleanFunction.arrayToInt(result);
        System.out.println("Random number = " + number);*/

        BooleanFunction xorFunction = new BooleanFunction(2) {
            @Override
            protected boolean actualCall(boolean[] arguments) {
                return arguments[0] ^ arguments[1];
            }
        };

        QuantumRegister register = QuantumComputer.createNewRegister("001");
        register.apply(Operators.hadamar().tensorPow(3));
        register.apply(Operators.oracle(xorFunction));
        register.applyAtPositions(Operators.hadamar().tensorPow(2), 0);

        String measureResult = BooleanFunction.arrayToString(register.measure());
        boolean isConstant = measureResult.substring(0, measureResult.length() - 1).equals("00");
        System.out.println("x + y is balanced? " + !isConstant);

        QuantumComputer.destroyRegister(register.getId());
    }

    private static void onInitializationFailed(Throwable error) {
        System.out.println(String.format("Error during initialization: %s", error.getMessage()));
    }

}
