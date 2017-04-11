package api.model;

/**
 * @author Artur Vasilov
 */
public class QuantumMemoryAddress {

    private final double frequency;
    private final double timeDelay;
    private final MemoryHalf memoryHalf;

    public double getFrequency() {
        return frequency;
    }

    public double getTimeDelay() {
        return timeDelay;
    }

    public MemoryHalf getMemoryHalf() {
        return memoryHalf;
    }

    public QuantumMemoryAddress(double frequency, double timeDelay, MemoryHalf memoryHalf) {
        this.memoryHalf = memoryHalf;
        this.frequency = frequency;
        this.timeDelay = timeDelay;
    }
}
