package ru.kpfu.arturvasilov.core;

import org.apache.commons.math3.complex.ComplexUtils;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

/**
 * @author Artur Vasilov
 */
public class QuantumRegister {

    private final int qubitsCount;

    private final Complex[] register;

    public QuantumRegister(int qubitsCount, String initialState) {
        this.qubitsCount = qubitsCount;
        int n = (int) Math.pow(2, qubitsCount);
        register = new Complex[n];
        for (int i = 0; i < n; i++) {
            register[i] = new Complex();
        }

        if (initialState == null || initialState.length() != qubitsCount) {
            String message = "Initial state of qubit register must have the same length as the register itself";
            throw new IllegalArgumentException(message);
        }

        int multiplier = 1;
        int currentNumber = 0;
        for (int i = qubitsCount - 1; i >= 0; i--) {
            if (initialState.charAt(i) == '1') {
                currentNumber += multiplier;
            }
            multiplier *= 2;
        }
        register[currentNumber] = new Complex(1, 0);
    }

    public void apply(ComplexMatrix operator) {
        if (operator.matrix.length != register.length) {
            throw new IllegalArgumentException("Operator size is incorrect");
        }

        Complex[] result = new Complex[register.length];
        for (int i = 0; i < register.length; i++) {
            result[i] = new Complex();
            for (int j = 0; j < register.length; j++) {
                result[i] = result[i].add(operator.getValue(i, j).multiply(register[j]));
            }
        }

        System.arraycopy(result, 0, register, 0, register.length);
    }

    public void applyAtPositions(ComplexMatrix operator, int startQubit) {
        String errorMessage = "Cannot apply operator, check the positions";
        if (startQubit < 0 || startQubit >= qubitsCount) {
            throw new IllegalArgumentException(errorMessage);
        }
        if ((int) Math.pow(2, startQubit) * operator.matrix.length > register.length) {
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

        while (resultOperator.matrix.length < register.length) {
            resultOperator = resultOperator.tensorMultiplication(ComplexMatrix.identity(2));
        }
        apply(resultOperator);
    }

    public boolean[] measure() {
        boolean[] result = new boolean[qubitsCount];

        int[] generateNumbers = new int[register.length];
        double[] probabilities = new double[register.length];
        for (int i = 0; i < register.length; i++) {
            generateNumbers[i] = i;
            Complex complex = register[i];
            probabilities[i] = complex.doubleA() * complex.doubleA()
                    + complex.doubleB() * complex.doubleB();
        }

        ComplexUtils.convertToComplex(new double[]{0, 0});

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
