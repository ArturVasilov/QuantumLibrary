package emulator;

import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.ComplexVector;

import java.util.Random;

/**
 * @author Artur Vasilov
 */
public class QuantumRegister {

    private int qubitsNumber;
    private int size;
    private ComplexMatrix stateMatrix;

    public QuantumRegister(int qubitsNumber) {
        this.setQubitsNumber(qubitsNumber);
    }

    public QuantumRegister(int qubitsNumber, ComplexMatrix stateMatrix) throws Exception {
        this.qubitsNumber = qubitsNumber;
        size = ((int) Math.pow(2, qubitsNumber));
        this.stateMatrix = stateMatrix;
        if (size != stateMatrix.matrix[0].length) {
            throw new Exception();
        }
    }

    public QuantumRegister(int qubitsNumber, ComplexVector configuration) throws Exception {
        this(qubitsNumber, configuration.ketBraTensor());
    }

    public ComplexMatrix getStateMatrix() {
        return stateMatrix;
    }

    public int getQubitsNumber() {
        return qubitsNumber;
    }

    public void setQubitsNumber(int qubitsNumber) {
        this.qubitsNumber = qubitsNumber;
        this.size = ((int) Math.pow(2, qubitsNumber));
        Complex[] vector = new Complex[size];
        vector[0] = Complex.one();
        for (int i = 1; i < vector.length; i++) {
            vector[i] = Complex.zero();
        }
    }

    public void multiplyOnMatrix(ComplexMatrix matrix) throws Exception {
        if (matrix.matrix.length != size) {
            throw new Exception();
        }
        stateMatrix = matrix.multiply(stateMatrix);
    }

    @Override
    public String toString() {
        return stateMatrix.toString();
    }

    public void performAlgorythm(QuantumAlgorithm algorythm) throws Exception {
        performTransformationWithMatrix(algorythm.getMatrix());
    }

    public void performAlgorythm(OneStepOneQubitGateAlgorithm algorythm) throws Exception {
        performTransformationWithMatrix(algorythm.getMatrix());
    }

    private void performTransformationWithMatrix(ComplexMatrix U) {
        stateMatrix = U.multiply(stateMatrix);
        stateMatrix = stateMatrix.multiply(U.conjugateTranspose());
    }

    /// Измерение
    public int measureQubit(int qubit) throws Exception {
        if (qubit >= qubitsNumber) {
            throw new Exception();
        }
        ComplexMatrix P0 = ComplexMatrix.zeros(size);
        int pow2n_q = (int) Math.pow(2, qubitsNumber - qubit);
        int pow2n_q_1 = (int) Math.pow(2, qubitsNumber - qubit - 1);
        // нужно пройти по всем состояниям, где текущий кубит 0
        for (int i = 0; i < size; i += pow2n_q) {
            for (int j = i; j < i + pow2n_q_1; j++) {
                P0.setValue(j, j, Complex.one());
            }
        }

        int result = 0;

        ComplexMatrix P0Transpose = P0.conjugateTranspose();
        ComplexMatrix P0Transpose_P0 = P0Transpose.multiply(P0);
        ComplexMatrix P0Transpose_P0_ro = P0Transpose_P0.multiply(stateMatrix);

        double p0Norma = P0Transpose_P0_ro.trace().getReal();

        //measure and normalize
        ComplexMatrix Pm;
        if (new Random().nextDouble() > p0Norma) {
            result = 1;
            //Configure P1 projector
            Pm = ComplexMatrix.zeros(size);
            for (int i = pow2n_q_1; i < size; i += pow2n_q) {
                for (int j = i; j < i + pow2n_q_1; j++) {
                    Pm.setValue(j, j, Complex.one());
                }
            }
        } else {
            Pm = P0;
        }

        ComplexMatrix PmTranspose = Pm.conjugateTranspose();
        ComplexMatrix Pm_ro = Pm.multiply(stateMatrix);
        ComplexMatrix Pm_ro_PmTranspose = Pm_ro.multiply(PmTranspose);
        ComplexMatrix PmTranspose_Pm_ro = PmTranspose.multiply(Pm).multiply(stateMatrix);

        Complex trace = PmTranspose_Pm_ro.trace();
        Complex divider = new Complex(1 / trace.getReal(), 0);

        stateMatrix = Pm_ro_PmTranspose.multiply(divider);
        return result;
    }
}
