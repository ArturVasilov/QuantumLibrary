package ru.kpfu.arturvasilov.core.universal;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.ComplexVector;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class UniversalQuantumRegister implements QuantumRegister {

    private final int qubitsCount;

    private ComplexVector register;

    public UniversalQuantumRegister(String initialState) {
        if (initialState == null || initialState.isEmpty()) {
            String message = "Initial state of qubit register is incorrect";
            throw new IllegalArgumentException(message);
        }

        this.qubitsCount = initialState.length();
        int n = (int) Math.pow(2, qubitsCount);
        register = new ComplexVector(n);

        int multiplier = 1;
        int currentNumber = 0;
        for (int i = qubitsCount - 1; i >= 0; i--) {
            if (initialState.charAt(i) == '1') {
                currentNumber += multiplier;
            }
            multiplier *= 2;
        }
        register.set(currentNumber, Complex.one());
    }

    @Override
    public long getId() {
        //id for universal quantum register means nothing
        return 0;
    }

    @Override
    public void apply(ComplexMatrix operator) {
        if (operator.matrix.length != register.size()) {
            throw new IllegalArgumentException("Operator size is incorrect");
        }
        if (!operator.isUnitary()) {
            //throw new IllegalArgumentException("Only unitary operators could be applied");
        }

        register = register.multiplyOnMatrix(operator);
    }

    @Override
    public void applyAtPositions(ComplexMatrix operator, int startQubit) {
        String errorMessage = "Cannot apply operator, check the positions";
        if (startQubit < 0 || startQubit >= qubitsCount) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (!operator.isUnitary()) {
            throw new IllegalArgumentException("Only unitary operators could be applied");
        }

        if ((int) Math.pow(2, startQubit) * operator.matrix.length > register.size()) {
            //operator has larger size than the rest of the register
            throw new IllegalArgumentException(errorMessage);
        }

        ComplexMatrix resultOperator;
        if (startQubit == 0) {
            resultOperator = operator.copy();
        } else {
            resultOperator = ComplexMatrix.identity(2)
                    .tensorPow(startQubit)
                    .tensorMultiplication(operator);
        }

        while (resultOperator.matrix.length < register.size()) {
            resultOperator = resultOperator.tensorMultiplication(ComplexMatrix.identity(2));
        }
        apply(resultOperator);
    }

    @Override
    public QuantumRegister concatWith(QuantumRegister register) {
        //TODO : concat registers
        return null;
    }

    @Override
    public boolean[] measure() {
        boolean[] result = new boolean[qubitsCount];

        int[] generateNumbers = new int[register.size()];
        double[] probabilities = new double[register.size()];
        for (int i = 0; i < register.size(); i++) {
            generateNumbers[i] = i;
            Complex complex = register.get(i);
            probabilities[i] = complex.getReal() * complex.getReal()
                    + complex.getImaginary() * complex.getImaginary();
        }

        EnumeratedIntegerDistribution distribution =
                new EnumeratedIntegerDistribution(generateNumbers, probabilities);
        int measuredNumber = distribution.sample();

        int currentIndex = qubitsCount - 1;
        while (measuredNumber > 0) {
            result[currentIndex] = measuredNumber % 2 == 1;
            measuredNumber /= 2;
            currentIndex--;
        }

        return result;
    }
}
