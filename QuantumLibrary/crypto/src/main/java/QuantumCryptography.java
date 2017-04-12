import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class QuantumCryptography {

    public static QuantumRegister singleQubitHash(int number) {
        QuantumRegister register = QuantumComputer.createNewRegister(1, "0");

        int bitsCount = 32 - Integer.numberOfLeadingZeros(number);
        int power = (int) Math.pow(2, bitsCount);

        //TODO : here we must use only unitary operators

        double[][] hashingOperator = new double[][]{
                new double[]{Math.cos(2 * Math.PI * number / power), 0},
                new double[]{Math.sin(2 * Math.PI * number / power), 1}
        };
        register.apply(ComplexMatrix.fromRealArray(hashingOperator));

        return register;
    }

}
