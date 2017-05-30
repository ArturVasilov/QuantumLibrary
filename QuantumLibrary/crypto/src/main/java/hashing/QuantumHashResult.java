package hashing;

import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class QuantumHashResult {

    private final List<QuantumRegister> hashRegisters;

    public QuantumHashResult(List<QuantumRegister> hashRegisters) {
        this.hashRegisters = new ArrayList<>();
        this.hashRegisters.addAll(hashRegisters);
    }

    public QuantumHashResult(QuantumRegister hashRegister) {
        hashRegisters = new ArrayList<>();
        hashRegisters.add(hashRegister);
    }

    public List<QuantumRegister> getHashRegisters() {
        if (hashRegisters == null) {
            return Collections.emptyList();
        }
        return hashRegisters;
    }
}
