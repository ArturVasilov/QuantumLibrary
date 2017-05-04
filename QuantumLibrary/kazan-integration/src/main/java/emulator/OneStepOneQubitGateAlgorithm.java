package emulator;

import ru.kpfu.arturvasilov.core.ComplexMatrix;

/**
 * @author Artur Vasilov
 */
public class OneStepOneQubitGateAlgorithm {

    protected final int qubitsNumber;
    protected final int size;

    /**
     * New gate for register that for transition qubit at position
     */
    private ComplexMatrix matrix;

    public OneStepOneQubitGateAlgorithm(int qubitsNumber, ComplexMatrix oneQubitGateMatrix, int qubitPosition) throws Exception {
        this.qubitsNumber = qubitsNumber;
        this.size = (int) Math.pow(2, qubitsNumber);

        matrix = ComplexMatrix.identity(1);
        for (int i = 0; i < qubitPosition; i++) {
            matrix = matrix.tensorMultiplication(ComplexMatrix.identity(2));
        }
        matrix = matrix.tensorMultiplication(oneQubitGateMatrix);
        for (int i = qubitPosition + 1; i < qubitsNumber; i++) {
            matrix = matrix.tensorMultiplication(ComplexMatrix.identity(2));
        }
    }

    public ComplexMatrix getMatrix() {
        return matrix;
    }
}
