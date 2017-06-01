package ru.kpfu.arturvasilov.core.utils;

/**
 * @author Artur Vasilov
 */
public final class MathUtils {

    private MathUtils() {
    }

    public static int log2(int number) {
        if (number == 0) {
            throw new IllegalArgumentException("Log cannot be calculated for 0 value");
        }
        return 31 - Integer.numberOfLeadingZeros(number);
    }
}
