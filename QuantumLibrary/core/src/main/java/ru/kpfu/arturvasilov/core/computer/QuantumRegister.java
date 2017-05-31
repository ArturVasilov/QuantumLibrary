package ru.kpfu.arturvasilov.core.computer;

import ru.kpfu.arturvasilov.core.ComplexMatrix;

/**
 * @author Artur Vasilov
 */
public interface QuantumRegister {

    long getId();

    void apply(ComplexMatrix operator);

    void applyAtPositions(ComplexMatrix operator, int startQubit);

    QuantumRegister concatWith(QuantumRegister register);

    boolean[] measure();
}