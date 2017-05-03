package memorymanager.controller.addresses;

import api.model.QuantumMemoryAddress;

/**
 * @author Artur Vasilov
 */
public class GlobalQubitAddress {

    private long globalId;

    private QuantumMemoryAddress quantumMemoryAddress;

    public GlobalQubitAddress(long globalId, QuantumMemoryAddress quantumMemoryAddress) {
        this.globalId = globalId;
        this.quantumMemoryAddress = quantumMemoryAddress;
    }

    public long getGlobalId() {
        return globalId;
    }

    public QuantumMemoryAddress getQuantumMemoryAddress() {
        return quantumMemoryAddress;
    }
}
