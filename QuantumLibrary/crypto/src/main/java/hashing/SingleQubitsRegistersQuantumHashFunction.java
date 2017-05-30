package hashing;

import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class SingleQubitsRegistersQuantumHashFunction implements QuantumHashFunction {



    @Override
    public QuantumHashResult hash(int number) {
        List<QuantumRegister> registers = new ArrayList<>();

        return new QuantumHashResult(registers);
    }

    @Override
    public boolean compareWithHash(QuantumHashResult expectedHash, int number) {
        return false;
    }
}
