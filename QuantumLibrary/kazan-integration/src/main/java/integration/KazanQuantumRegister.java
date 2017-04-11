package integration;

import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class KazanQuantumRegister implements QuantumRegister {

    //TODO

    private final long id;

    public KazanQuantumRegister(long id) {
        this.id = id;
    }

    @Override
    public void apply(ComplexMatrix operator) {

    }

    @Override
    public void applyAtPositions(ComplexMatrix operator, int startQubit) {

    }

    @Override
    public boolean[] measure() {
        return new boolean[0];
    }
}
