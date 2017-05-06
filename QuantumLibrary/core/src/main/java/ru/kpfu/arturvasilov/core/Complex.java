package ru.kpfu.arturvasilov.core;

/**
 * Class for representing complex number
 * <p>
 * Enough theory can be found here https://en.wikipedia.org/wiki/Complex_number
 *
 * @author Artur Vasilov
 */
public class Complex {

    private static final double EPS = 0.0000001;

    private final double a;
    private final double b;

    public Complex() {
        this(0, 0);
    }

    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public static Complex zero() {
        return new Complex();
    }

    public static Complex one() {
        return new Complex(1, 0);
    }

    /**
     * Compares values with some accuracy
     *
     * @return true iff o is instance of {@link Complex} and (a, b) pair is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complex)) {
            return false;
        }

        Complex complex = (Complex) o;
        boolean sameReal = Math.abs(a - complex.a) < EPS;
        boolean sameImaginary = Math.abs(b - complex.b) < EPS;
        return sameReal && sameImaginary;
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(a);
        result = 31 * result + Double.hashCode(b);
        return result;
    }

    @Override
    public String toString() {
        if (Math.abs(b) < EPS) {
            return Double.toString(a);
        }
        return String.format("%s %s %si", Double.toString(a), b > 0 ? "+" : "-", Double.toString(Math.abs(a)));
    }

    /**
     * Creates new {@link Complex} instance which is result of addition of current value and argument
     * <p>
     * first(a1, b1) + second(a2, b2) = result(a1 + a2, b1 + b2)
     *
     * @param value - complex number for adding to current
     * @return addition of current number and value argument
     */
    public Complex add(Complex value) {
        return new Complex(a + value.a, b + value.b);
    }

    public Complex sub(Complex value) {
        return new Complex(a - value.a, b - value.b);
    }

    /**
     * Creates new {@link Complex} instance which is result of multiplication of current number and argument
     * <p>
     * first(a1, b1) * second(a2, b2) = result(a1 * a2 - b1 * b2, a1 * b2 + a2 * b1)
     *
     * @param value - complex number for multiply with current
     * @return multiplication of current number and value argument
     */
    public Complex multiply(Complex value) {
        double resultA = a * value.a - b * value.b;
        double resultB = a * value.b + b * value.a;
        return new Complex(resultA, resultB);
    }

    public double norma() {
        return getReal() * getReal() + getImaginary() * getImaginary();
    }

    /**
     * Creates complex value which is conjugate to current complex number
     * <p>
     * ~first(a, b) = result(a, -b)
     */
    public Complex conjugate() {
        return new Complex(a, -b);
    }

    public double getReal() {
        return a;
    }

    public double getImaginary() {
        return b;
    }
}
