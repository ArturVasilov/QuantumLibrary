package hashing.multiple;

/**
 * @author Artur Vasilov
 */
public class DefaultParametersProvider implements ParametersProvider {

    public static ParametersProvider getInstance() {
        return new DefaultParametersProvider();
    }

    private DefaultParametersProvider() {
        // do nothing
    }

    @Override
    public int lParameter(int qubitsCount) {
        return 1000;
    }

    @Override
    public int[] kParameters(int qubitsCount) {
        int[] values = new int[qubitsCount];
        int step = lParameter(qubitsCount) / 10;
        int currentValue = step;
        for (int i = 0; i < qubitsCount; i++) {
            values[i] = currentValue;
            currentValue += step;
        }
        return values;
    }
}
