package emulator;

import java.util.Random;

/**
 * @author Artur Vasilov
 */
public class QuantumRegister {
    private int qubitsNumber;
    private int size;
    private Complex[] vector;

    public QuantumRegister(int qubitsNumber) {
        this.setQubitsNumber(qubitsNumber);
    }

    public QuantumRegister(int qubitsNumber, Complex[] vector) throws Exception {
        this.qubitsNumber = qubitsNumber;
        size = ((int) Math.pow(2, qubitsNumber));
        this.vector = vector;
        if (size != vector.length) {
            throw new Exception();
        }
    }


//    public QuantumRegister (Qubit... qubits){
//        qubitsNumber = qubits.length;
//        size = ((int) Math.pow(2, qubitsNumber));
//        vector = qubits[0].getVector();
//        for (int i=1; i< qubits.length; i++){
//            vector = ComplexMath.tensorMultiplication(vector, qubits[i].getVector());
//        }
//    }

    public Complex[] getVector() {
        return vector;
    }

    public int getQubitsNumber() {
        return qubitsNumber;
    }

    public void setQubitsNumber(int qubitsNumber) {
        this.qubitsNumber = qubitsNumber;
        this.size = ((int) Math.pow(2, qubitsNumber));
        this.vector = new Complex[size];
        this.vector[0] = Complex.unit();
        for (int i = 1; i < this.vector.length; i++) {
            vector[i] = Complex.zero();
        }
    }

    public void multiplyOnMatrix(Complex[][] matrix) throws Exception {
        if (matrix.length != size) {
            throw new Exception();
        }
        this.vector = ComplexMath.multiplication(matrix, size, vector);
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < size; i++) {
            result = result + vector[i] + " |" + i + "> ";
        }
        return result;
    }

    public void performAlgorythm(QuantumAlgorithm algorythm) throws Exception {
        vector = ComplexMath.multiplication(algorythm.getMatrix(), size, vector);
    }

    public void performAlgorythm(OneStepOneQubitGateAlgorythm algorythm) throws Exception {
        vector = ComplexMath.multiplication(algorythm.getMatrix(), size, vector);
    }


    /// Измерение
    public int measureQubit(int qubit, boolean needIncreaseQubitsNumber) throws Exception {
        if (qubit >= qubitsNumber) {
            throw new Exception();
        }
        Complex[][] P0 = ComplexMath.zeroMatrix(size, size);
        int pow2n_q = (int) Math.pow(2, qubitsNumber - qubit);
        int pow2n_q_1 = (int) Math.pow(2, qubitsNumber - qubit - 1);
        // нужно пройти по всем состояниям, где текущий кубит 0
        for (int i = 0; i < size; i += pow2n_q) {
            for (int j = i; j < i + pow2n_q_1; j++) {
                P0[j][j] = Complex.unit();
            }
        }
        Complex[][] vectorBra = new Complex[1][size];
        Complex[][] vectorKet = new Complex[size][1];
        for (int i = 0; i < size; i++) {
            vectorKet[i][0] = vector[i];
        }
        vectorBra[0] = vector.clone();
        Complex[][] p0 = ComplexMath.matricesMultiplication(vectorBra, 1, size,
                P0, size, size);
        p0 = ComplexMath.matricesMultiplication(p0, 1, size, vectorKet, size, 1);
        double p0Norma = p0[0][0].mod();
        int result = 0;

        //measure and normalize
        Complex[][] Pm;
        if (new Random().nextDouble() > p0Norma) {
            result = 1;
//            Configure P1 projector
            Pm = ComplexMath.zeroMatrix(size, size);
            for (int i = pow2n_q_1; i < size; i += pow2n_q) {
                for (int j = i; j < i + pow2n_q_1; j++) {
                    Pm[j][j] = Complex.unit();
                }
            }
        } else {
            Pm = P0;
        }

        //norm
        vector = ComplexMath.multiplication(Pm, size, vector);
        double norma = 0.0;
        for (int i = 0; i < size; i++) {
            norma += Math.pow(vector[i].mod(), 2);
        }
        norma = Math.sqrt(norma);

        Complex complexNorma = new Complex(norma, 0);

        for (int i = 0; i < size; i++) {
            vector[i] = Complex.devide(vector[i], complexNorma);
        }

        if (needIncreaseQubitsNumber) {
            int oldSize = size;
            size /= 2;
            qubitsNumber--;

            Complex[] newVector = new Complex[size];
            int firstIndex = result == 0 ? 0 : pow2n_q_1;

            int z = 0;

            for (int i = firstIndex; i < oldSize; i += pow2n_q) {
                for (int j = i; j < i + pow2n_q_1; j++) {
                    newVector[z] = vector[j];
                    z++;
                }
            }

            vector = newVector;
        }

        return result;
    }
}
