package hashing.tests;

import hashing.QuantumHashResult;

/**
 * @author Artur Vasilov
 */
public interface QuantumHashesEqualityTestFunction {

    boolean equals(QuantumHashResult first, QuantumHashResult second);
}
