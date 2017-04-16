package integration;

import ru.kpfu.arturvasilov.core.computer.QuantumMemoryManager;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artur Vasilov
 */
public class KazanMemoryManager implements QuantumMemoryManager {

    private final Map<Long, QuantumRegister> createdRegisters = new HashMap<>();

    @Override
    public QuantumRegister createNewRegister(String initialState) {
        long registerId = System.currentTimeMillis();
        while (createdRegisters.containsKey(registerId)) {
            registerId++;
        }

        QuantumRegister quantumRegister = new KazanQuantumRegister(registerId);
        createdRegisters.put(registerId, quantumRegister);
        return quantumRegister;
    }

    @Override
    public void destroyRegister(long registerId) {
        createdRegisters.remove(registerId);
    }
}
