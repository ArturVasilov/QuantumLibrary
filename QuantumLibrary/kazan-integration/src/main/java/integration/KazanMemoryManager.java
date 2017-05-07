package integration;

import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.Operators;
import ru.kpfu.arturvasilov.core.computer.QuantumMemoryManager;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artur Vasilov
 */
public class KazanMemoryManager implements QuantumMemoryManager {

    private final Map<Long, QuantumRegister> createdRegisters = new HashMap<>();

    // Functions below should be native and should be integrated with real quantum computer, but now we don't have required API

    private static void nativeInit(long registerId, int qubitsCount) {
        // do nothing
    }

    private static void nativeDestroy(long registerId) {
        // do nothing
    }

    @Override
    public QuantumRegister createNewRegister(String initialState) {
        long registerId = System.currentTimeMillis();
        while (createdRegisters.containsKey(registerId)) {
            registerId++;
        }
        int qubitsCount = initialState.length();

        nativeInit(registerId, qubitsCount);
        QuantumRegister quantumRegister = new KazanQuantumRegister(registerId, qubitsCount);

        ComplexMatrix initialStateMatrix = ComplexMatrix.identity(1);
        for (int i = 0; i < initialState.length(); i++) {
            if (initialState.charAt(i) == '0') {
                initialStateMatrix = initialStateMatrix.tensorMultiplication(ComplexMatrix.identity(2));
            } else {
                initialStateMatrix = initialStateMatrix.tensorMultiplication(Operators.pauliX());
            }
        }
        quantumRegister.apply(initialStateMatrix);

        createdRegisters.put(registerId, quantumRegister);
        return quantumRegister;
    }

    @Override
    public void destroyRegister(long registerId) {
        nativeDestroy(registerId);
        createdRegisters.remove(registerId);
    }
}
