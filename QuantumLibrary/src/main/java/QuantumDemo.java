import integration.Quantum;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.Operators;

/**
 * @author Artur Vasilov
 */
public class QuantumDemo {

    public static void main(String[] args) {
        int[] addresses = new int[]{1, 2, 3};

        /*Complex[][] matrix = new Complex[][] {
                {Complex.one(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.one(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.one()},
                {Complex.zero(), Complex.zero(), Complex.one(), Complex.zero()},
        };
        int[] addresses = new int[]{1, 2};*/
        Quantum.runUnitaryCalculation(Operators.toffoli(), addresses);
    }

}
