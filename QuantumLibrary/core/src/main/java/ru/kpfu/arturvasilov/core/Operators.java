package ru.kpfu.arturvasilov.core;

import java.util.Arrays;

/**
 * @author Artur Vasilov
 */
public final class Operators {

    private Operators() {
    }

    public static ComplexMatrix hadamar() {
        double hadamarValue = 1 / Math.sqrt(2);
        double[][] array = new double[][]{
                new double[]{hadamarValue, hadamarValue},
                new double[]{hadamarValue, -hadamarValue}
        };
        return ComplexMatrix.fromRealArray(array);
    }

    public static ComplexMatrix pauliX() {
        double[][] array = new double[][]{
                new double[]{0, 1},
                new double[]{1, 0}
        };
        return ComplexMatrix.fromRealArray(array);
    }

    public static ComplexMatrix pauliY() {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(0, 0));
        matrix.setValue(0, 1, new Complex(0, -1));
        matrix.setValue(1, 0, new Complex(0, 1));
        matrix.setValue(1, 1, new Complex(0, 0));
        return matrix;
    }

    public static ComplexMatrix pauliZ() {
        double[][] array = new double[][]{
                new double[]{1, 0},
                new double[]{0, -1}
        };
        return ComplexMatrix.fromRealArray(array);
    }

    public static ComplexMatrix toffoli() {
        ComplexMatrix matrix = ComplexMatrix.zeros(8);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(1, 1, new Complex(1, 0));
        matrix.setValue(2, 2, new Complex(1, 0));
        matrix.setValue(3, 3, new Complex(1, 0));
        matrix.setValue(4, 4, new Complex(1, 0));
        matrix.setValue(5, 5, new Complex(1, 0));
        matrix.setValue(6, 7, new Complex(1, 0));
        matrix.setValue(7, 6, new Complex(1, 0));
        return matrix;
    }

    public static ComplexMatrix oracle(BooleanFunction function) {
        int resultSize = function.size() + 1;

        int operatorSize = (int) Math.pow(2, resultSize);
        ComplexMatrix matrix = new ComplexMatrix(operatorSize);

        boolean[] arguments = new boolean[resultSize];
        Arrays.fill(arguments, false);

        do {
            boolean[] functionArgs = new boolean[resultSize - 1];
            System.arraycopy(arguments, 0, functionArgs, 0, resultSize - 1);
            boolean result = function.call(functionArgs) ^ arguments[arguments.length - 1];

            int inState = BooleanFunction.arrayToInt(arguments);
            arguments[resultSize - 1] = result;
            int outState = BooleanFunction.arrayToInt(arguments);
            arguments[resultSize - 1] = result;
            matrix.setValue(inState, outState, new Complex(1, 0));
        } while (BooleanFunction.next(arguments));

        return matrix;
    }
}
