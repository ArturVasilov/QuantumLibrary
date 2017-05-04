package api.model;

import api.QuantumManager;
import ru.kpfu.arturvasilov.core.Complex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artur Vasilov
 */
public class QuantumMemory {

    private final QuantumProcessorHelper helper;
    private final QuantumMemoryInfo info;
    private final Map<QuantumMemoryAddress, QuantumManager.Qubit> qubits = new HashMap<>();

    QuantumMemory(QuantumMemoryInfo info, QuantumProcessorHelper helper) {
        this.info = info;
        this.helper = helper;
    }

    QuantumMemoryInfo getInfo() {
        return info;
    }

    private boolean addressIsUsed(QuantumMemoryAddress address) {
        return qubits.containsKey(address);
    }

    private boolean addressIsOutOfRanges(QuantumMemoryAddress address) {
        return address.getFrequency() > info.getMaximumAvailableFrequency()
                || address.getFrequency() < info.getMinimumAvailableFrequency()
                || address.getTimeDelay() > info.getTimeInterval();
    }

    QuantumManager.Qubit initQubitForAddress(QuantumMemoryAddress address,
                                             Complex alpha, Complex beta) throws Exception {
        if (addressIsUsed(address)) {
            throw new Exception("This address is already used!");
        }

        if (addressIsOutOfRanges(address)) {
            throw new Exception("Address is out of available range");
        }

        QuantumManager.Qubit qubit = helper.initNewQubit(alpha, beta);
        qubits.put(address, qubit);
        return qubit;
    }

    QuantumManager.Qubit initQubitForAddress(QuantumMemoryAddress address) throws Exception {
        Complex alpha = Complex.zero(), beta = Complex.zero();
        switch (address.getMemoryHalf()) {
            case HALF_0:
                alpha = Complex.one();
                break;
            case HALF_1:
                beta = Complex.one();
                break;
        }
        return initQubitForAddress(address, alpha, beta);
    }

    void saveQubit(QuantumMemoryAddress address, QuantumManager.Qubit qubit) {
        qubits.put(address, qubit);
    }

    QuantumManager.Qubit popQubit(QuantumMemoryAddress address) {
        QuantumManager.Qubit qubit = qubits.get(address);
        qubits.remove(address);
        return qubit;
    }
}
