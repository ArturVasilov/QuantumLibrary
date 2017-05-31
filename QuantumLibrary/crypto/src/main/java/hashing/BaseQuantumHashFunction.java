package hashing;

/**
 * @author Artur Vasilov
 */
public abstract class BaseQuantumHashFunction implements QuantumHashFunction {

    private final QuantumHashesEqualityTestFunction equalityTestFunction;

    public BaseQuantumHashFunction(QuantumHashesEqualityTestFunction equalityTestFunction) {
        this.equalityTestFunction = equalityTestFunction;
    }

    @Override
    public boolean compareWithHash(QuantumHashResult expectedHash, int number) {
        QuantumHashResult hash = hash(number);
        return equalityTestFunction.equals(expectedHash, hash);
    }
}
