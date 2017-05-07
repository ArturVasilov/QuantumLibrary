package ru.kpfu.arturvasilov.core;

import java.util.Arrays;

/**
 * @author Artur Vasilov
 */
public class ComplexVector {

    private final Complex[] vector;

    public ComplexVector(int n) {
        vector = new Complex[n];
        for (int i = 0; i < n; i++) {
            vector[i] = new Complex();
        }
    }

    public ComplexVector(Complex... numbers) {
        vector = new Complex[numbers.length];
        System.arraycopy(numbers, 0, vector, 0, numbers.length);
    }

    public Complex get(int position) {
        if (position < 0 || position >= vector.length) {
            throw new IllegalArgumentException("Illegal index for getting value in complex vector");
        }
        return vector[position];
    }

    public void set(int position, Complex value) {
        if (position < 0 || position >= vector.length) {
            throw new IllegalArgumentException("Illegal index for setting value in complex vector");
        }
        vector[position] = value;
    }

    public int size() {
        return vector.length;
    }

    public ComplexVector multiplyOnMatrix(ComplexMatrix matrix) {
        ComplexVector result = new ComplexVector(vector.length);
        for (int i = 0; i < vector.length; i++) {
            result.set(i, new Complex());
        }
        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result.set(i, result.get(i).add(matrix.getValue(i, j).multiply(vector[j])));
            }
        }
        return result;
    }

    public ComplexMatrix ketBraTensor() {
        int n = vector.length;
        ComplexMatrix matrix = new ComplexMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.setValue(i, j, vector[i].multiply(vector[j]));
            }
        }
        return matrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComplexVector that = (ComplexVector) o;
        return Arrays.equals(vector, that.vector);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vector);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("|\t");
        for (Complex element : vector) {
            builder.append(element).append("\t");
        }
        return builder.append("|").toString();
    }
}
