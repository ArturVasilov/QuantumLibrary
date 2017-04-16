package ru.kpfu.arturvasilov.core.universal;

import ru.kpfu.arturvasilov.core.computer.QuantumMemoryManager;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class UniversalMemoryManager implements QuantumMemoryManager {

    @Override
    public QuantumRegister createNewRegister(String initialState) {
        return new UniversalQuantumRegister(initialState);
    }

    @Override
    public void destroyRegister(long registerId) {
        // do nothing, we don't work with real quantum computer here
    }
}
