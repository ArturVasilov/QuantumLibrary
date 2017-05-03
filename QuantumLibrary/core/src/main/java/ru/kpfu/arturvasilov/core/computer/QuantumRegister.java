package ru.kpfu.arturvasilov.core.computer;

import ru.kpfu.arturvasilov.core.ComplexMatrix;

/**
 * @author Artur Vasilov
 */
public interface QuantumRegister {

    void apply(ComplexMatrix operator);

    void applyAtPositions(ComplexMatrix operator, int startQubit);

    boolean[] measure();
}