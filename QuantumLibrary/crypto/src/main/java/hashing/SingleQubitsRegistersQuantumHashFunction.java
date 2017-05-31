package hashing;

import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class SingleQubitsRegistersQuantumHashFunction extends BaseQuantumHashFunction {

    public SingleQubitsRegistersQuantumHashFunction(QuantumHashesEqualityTestFunction equalityTestFunction) {
        super(equalityTestFunction);
    }

    @Override
    public QuantumHashResult hash(int number) {
        List<QuantumRegister> registers = new ArrayList<>();

        return new QuantumHashResult(registers);
    }
}
