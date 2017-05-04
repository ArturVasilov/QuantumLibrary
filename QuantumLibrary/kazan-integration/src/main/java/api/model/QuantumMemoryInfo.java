package api.model;

/**
 * @author Artur Vasilov
 */
public class QuantumMemoryInfo {

    private final double maximumAvailableFrequency;
    private final double minimumAvailableFrequency;
    private final double timeInterval;

    public QuantumMemoryInfo(double maximumAvailableFrequency, double minimumAvailableFrequency, double timeInterval) {
        this.maximumAvailableFrequency = maximumAvailableFrequency;
        this.minimumAvailableFrequency = minimumAvailableFrequency;
        this.timeInterval = timeInterval;
    }

    public double getMaximumAvailableFrequency() {
        return maximumAvailableFrequency;
    }

    public double getMinimumAvailableFrequency() {
        return minimumAvailableFrequency;
    }

    public double getTimeInterval() {
        return timeInterval;
    }
}
