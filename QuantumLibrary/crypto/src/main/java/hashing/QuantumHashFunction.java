package hashing;

/**
 * @author Artur Vasilov
 */
public interface QuantumHashFunction {

    QuantumHashResult hash(int number);

    boolean compareWithHash(QuantumHashResult expectedHash, int number);
}
