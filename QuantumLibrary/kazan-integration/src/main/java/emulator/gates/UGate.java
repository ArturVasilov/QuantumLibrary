package emulator.gates;

import emulator.Complex;
import emulator.QuantumGate;

/**
 * @author Artur Vasilov
 */
public class UGate extends QuantumGate {

    private Complex[][] matrix;

    public UGate(int qubitsNumber, Complex[][] uMatrix) {
        this.qubitsNumber = qubitsNumber;
        this.size = (int) Math.pow(2, qubitsNumber);
        this.matrix = uMatrix;
    }

    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }
}
