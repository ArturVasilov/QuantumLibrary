package ru.kpfu.arturvasilov.core.computer;

/**
 * @author Artur Vasilov
 */
public interface QuantumMemoryManager {

    QuantumRegister createNewRegister(String initialState);

    void destroyRegister(long registerId);

}
