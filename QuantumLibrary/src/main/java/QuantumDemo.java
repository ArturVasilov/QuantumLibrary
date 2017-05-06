import integration.Quantum;
import ru.kpfu.arturvasilov.core.Operators;

/**
 * @author Artur Vasilov
 */
public class QuantumDemo {

    public static void main(String[] args) {
        int[] addresses = new int[]{1, 2, 3};
        Quantum.runUnitaryCalculation(Operators.toffoli(), addresses);
    }

}
