package api.model;

import api.QuantumManager;
import emulator.Complex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artur Vasilov
 */
public class QuantumMemory {
    private QuantumMemoryInfo info;

    QuantumMemoryInfo getInfo() {
        return info;
    }

    void setInfo(QuantumMemoryInfo info) {
        this.info = info;
    }

    QuantumMemory (QuantumMemoryInfo info, QuantumProccessorHelper helper) {
        this.info = info;
        this.helper = helper;
    }

    QuantumProccessorHelper helper;

    private Map<QuantumMemoryAddress, QuantumManager.Qubit> qubits = new HashMap<QuantumMemoryAddress, QuantumManager.Qubit>();

    private boolean addressIsUsed (QuantumMemoryAddress address){
        return qubits.containsKey(address);
    }

    private boolean addressIsOutOfRanges (QuantumMemoryAddress address){
        return address.getFrequency() > info.getMaximumAvailableFrequency()
                || address.getFrequency() < info.getMinimumAvailableFrequency()
                || address.getTimeDelay() > info.getTimeInterval();
    }

    QuantumManager.Qubit initQubitForAddress(QuantumMemoryAddress address,
                                             Complex alpha, Complex beta) throws Exception {
        if (addressIsUsed(address)){
            throw new Exception("This address is already used!");
        }

        if (addressIsOutOfRanges(address)){
            throw new Exception("Address is out of available range");
        }

        QuantumManager.Qubit qubit = helper.initNewQubit(alpha, beta);
        qubits.put(address, qubit);
        return qubit;
    }

    QuantumManager.Qubit initQubitForAddress(QuantumMemoryAddress address) throws Exception {
        Complex alpha = Complex.zero(), beta = Complex.zero();
        switch (address.getMemoryHalf()){
            case HALF_0:
                alpha = Complex.unit();
                break;
            case HALF_1:
                beta = Complex.unit();
                break;
        }
        return initQubitForAddress(address, alpha, beta);
    }


    void saveQubit (QuantumMemoryAddress address, QuantumManager.Qubit qubit){
        qubits.put(address, qubit);
    }

    QuantumManager.Qubit popQubit(QuantumMemoryAddress address){
        QuantumManager.Qubit qubit = qubits.get(address);
        qubits.remove(address);
        return qubit;
    }
}
