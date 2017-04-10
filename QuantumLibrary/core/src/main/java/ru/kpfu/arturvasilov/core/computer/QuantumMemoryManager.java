package ru.kpfu.arturvasilov.core.computer;

/**
 * @author Artur Vasilov
 */
public interface QuantumMemoryManager {

    QuantumRegister createNewRegister(int qubitsCount, String initialState);

    void destroyRegister(long registerId);

}
