package api.model;

import api.QuantumManager;

/**
 * @author Artur Vasilov
 */
public class ProcessingUnitCell {
    private QuantumManager.Qubit qubit;

    void loadQubit(QuantumManager.Qubit qubit) {
        this.qubit = qubit;
    }

    QuantumManager.Qubit unloadQubit() {
        QuantumManager.Qubit result = qubit;
        qubit = null;
        return result;
    }

    QuantumManager.Qubit getQubit() {
        return qubit;
    }
}
