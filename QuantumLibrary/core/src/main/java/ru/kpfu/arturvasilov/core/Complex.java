package ru.kpfu.arturvasilov.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class for representing complex number
 * <p>
 * Enough theory can be found here https://en.wikipedia.org/wiki/Complex_number
 *
 * @author Artur Vasilov
 */
public class Complex {

    private static final int DECIMAL_NUMBER_COMPARE_SCALE = 8;

    private BigDecimal a;
    private BigDecimal b;

    public Complex() {
        this(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
    }

    public Complex(double a, double b) {
        this(BigDecimal.valueOf(a), BigDecimal.valueOf(b));
    }

    public Complex(BigDecimal a, BigDecimal b) {
        this.a = a;
        this.b = b;
    }

    public static Complex zero() {
        return new Complex();
    }

    //TODO : rename to one
    public static Complex unit() {
        return new Complex(1, 0);
    }

    /**
     * Compares values using {@link BigDecimal#compareTo(Object)} method
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
        boolean sameReal = a.setScale(DECIMAL_NUMBER_COMPARE_SCALE, BigDecimal.ROUND_HALF_UP)
                .compareTo(complex.a.setScale(DECIMAL_NUMBER_COMPARE_SCALE, RoundingMode.HALF_UP)) == 0;
        boolean sameImaginary = b.setScale(DECIMAL_NUMBER_COMPARE_SCALE, BigDecimal.ROUND_HALF_UP)
                .compareTo(complex.b.setScale(DECIMAL_NUMBER_COMPARE_SCALE, BigDecimal.ROUND_UP)) == 0;
        return sameReal && sameImaginary;
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            return a.toString();
        }
        return String.format("%s %s %si", a.toString(), b.signum() > 0 ? "+" : "-", b.abs().toString());
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
        return new Complex(a.add(value.a), b.add(value.b));
    }

    public Complex sub(Complex value) {
        return new Complex(a.subtract(value.a), b.subtract(value.b));
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
        BigDecimal resultA = a.multiply(value.a).subtract(b.multiply(value.b));
        BigDecimal resultB = a.multiply(value.b).add(b.multiply(value.a));
        return new Complex(resultA, resultB);
    }

    public double norma() {
        return doubleA() * doubleA() + doubleB() * doubleB();
    }

    /**
     * Creates complex value which is conjugate to current complex number
     * <p>
     * ~first(a, b) = result(a, -b)
     */
    public Complex conjugate() {
        return new Complex(a, b.multiply(BigDecimal.valueOf(-1)));
    }

    public BigDecimal getA() {
        return a;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    public double doubleA() {
        return a.doubleValue();
    }

    public double doubleB() {
        return b.doubleValue();
    }
}
