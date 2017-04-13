import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.computer.InitializationCallback;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
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
        /*BooleanFunction xorFunction = new BooleanFunction(4) {
            @Override
            protected boolean actualCall(boolean[] arguments) {
                return arguments[0] ^ arguments[1] & arguments[2];
            }
        };

        QuantumRegister register = QuantumComputer.createNewRegister(5, "00001");
        register.apply(Operators.hadamar().tensorPow(5));
        register.apply(Operators.oracle(xorFunction));
        register.applyAtPositions(Operators.hadamar().tensorPow(4), 0);

        String measureResult = BooleanFunction.arrayToString(register.measure());
        boolean isConstant = measureResult.substring(0, measureResult.length() - 1).equals("0000");
        System.out.println("x + y is balanced? " + !isConstant);*/

        //System.out.println(QuantumCryptography.singleQubitHash(4).measure()[0]);

        /*ComplexMatrix u = ComplexMatrix.zeros(4);

        u.setValue(0, 0, new Complex(1, 0));
        u.setValue(0, 1, new Complex(1, 0));
        u.setValue(0, 2, new Complex(1, 0));
        u.setValue(0, 3, new Complex(1, 0));

        u.setValue(1, 0, new Complex(1, 0));
        u.setValue(1, 1, new Complex(0, 1));
        u.setValue(1, 2, new Complex(-1, 0));
        u.setValue(1, 3, new Complex(0, -1));

        u.setValue(2, 0, new Complex(1, 0));
        u.setValue(2, 1, new Complex(-1, 0));
        u.setValue(2, 2, new Complex(1, 0));
        u.setValue(2, 3, new Complex(-1, 0));

        u.setValue(3, 0, new Complex(1, 0));
        u.setValue(3, 1, new Complex(0, -1));
        u.setValue(3, 2, new Complex(-1, 0));
        u.setValue(3, 3, new Complex(0, 1));

        u = u.multiply(new Complex(0.5, 0));

        ComplexMatrix u1 = ComplexMatrix.zeros(4);
        u1.setValue(0, 0, new Complex(0.25, 0));
        u1.setValue(0, 1, new Complex(0.25, 0));
        u1.setValue(1, 0, new Complex(0.25, 0));
        u1.setValue(1, 1, new Complex(-0.25, 0));
        u1.setValue(2, 2, new Complex(1, 0));
        u1.setValue(3, 3, new Complex(1, 0));*/

        double[][] u = new double[][]{
                new double[]{0, 0, 0, 1},
                new double[]{0, 1, 0, 0},
                new double[]{1, 0, 0, 0},
                new double[]{0, 0, -1, 0}
        };

        double[][] u1 = new double[][]{
                new double[]{0, 0, 1, 0},
                new double[]{0, 1, 0, 0},
                new double[]{1, 0, 0, 0},
                new double[]{0, 0, 0, 1}
        };

        ComplexMatrix u2 = ComplexMatrix.fromRealArray(u1).multiply(ComplexMatrix.fromRealArray(u)).conjugateTranspose();

        System.out.println(u2.multiply(ComplexMatrix.fromRealArray(u1)).multiply(ComplexMatrix.fromRealArray(u)));
    }

    private static void onInitializationFailed(Throwable error) {
        System.out.println(String.format("Error during initialization: %s", error.getMessage()));
    }

}
