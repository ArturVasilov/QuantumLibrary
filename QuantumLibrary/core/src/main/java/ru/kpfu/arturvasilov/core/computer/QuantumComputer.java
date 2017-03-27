package ru.kpfu.arturvasilov.core.computer;

/**
 * @author Artur Vasilov
 */
public final class QuantumComputer {

    private static QuantumComputer quantumComputer;

    private QuantumComputer() {
    }

    public static void init(InitializationCallback callback) {
        quantumComputer = new QuantumComputer();
        callback.onInitializationSucceed();
    }

}
