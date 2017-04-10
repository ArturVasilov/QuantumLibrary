package ru.kpfu.arturvasilov.core.computer;

/**
 * @author Artur Vasilov
 */
public final class QuantumComputer {

    private static QuantumMemoryManager quantumMemoryManager;

    private QuantumComputer() {
    }

    public static void init(QuantumMemoryManager quantumMemoryManager, InitializationCallback callback) {
        QuantumComputer.quantumMemoryManager = quantumMemoryManager;
        callback.onInitializationSucceed();
    }

    public static QuantumRegister createNewRegister(int qubitsCount, String initialState) {
        checkInitialized();
        return quantumMemoryManager.createNewRegister(qubitsCount, initialState);
    }

    public static void destroyRegister(long registerId) {
        checkInitialized();
        quantumMemoryManager.destroyRegister(registerId);
    }

    public static void checkInitialized() {
        if (quantumMemoryManager == null) {
            throw new IllegalStateException("You should initialize Quantum Computer first");
        }
    }

}
