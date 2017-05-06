import integration.Quantum;
import ru.kpfu.arturvasilov.core.Complex;

/**
 * @author Artur Vasilov
 */
public class QuantumDemo {

    public static void main(String[] args) {
        Complex[][] matrix = new Complex[][] {
                {Complex.one(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.one(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.one(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.one(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.one(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.one(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.one()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(), Complex.one(), Complex.zero()},
        };
        int[] addresses = new int[]{1, 2, 3};

        /*Complex[][] matrix = new Complex[][] {
                {Complex.one(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.one(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.one()},
                {Complex.zero(), Complex.zero(), Complex.one(), Complex.zero()},
        };
        int[] addresses = new int[]{1, 2};*/
        Quantum.runUntitarCalculation(matrix, addresses);
    }

}
