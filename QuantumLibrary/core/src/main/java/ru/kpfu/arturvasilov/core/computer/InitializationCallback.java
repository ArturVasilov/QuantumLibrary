package ru.kpfu.arturvasilov.core.computer;

/**
 * @author Artur Vasilov
 */
public interface InitializationCallback {

    void onInitializationSucceed();

    void onInitializationFailed();
}
