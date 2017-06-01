package hashing.multiple;

/**
 * @author Artur Vasilov
 */
public interface ParametersProvider {

    int lParameter(int qubitsCount);

    int[] kParameters(int qubitsCount);
}
