package api.model;

/**
 * @author Artur Vasilov
 */
public class QuantumMemoryAddress {

    private double frequency;
    private double timeDelay;
    private MemoryHalf memoryHalf;

    public QuantumMemoryAddress(double frequency, double timeDelay, MemoryHalf memoryHalf) {
        this.memoryHalf = memoryHalf;
        this.frequency = frequency;
        this.timeDelay = timeDelay;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getTimeDelay() {
        return timeDelay;
    }

    public MemoryHalf getMemoryHalf() {
        return memoryHalf;
    }
}
