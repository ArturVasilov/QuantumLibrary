package hashing.tests;

import hashing.QuantumHashResult;
import ru.kpfu.arturvasilov.core.universal.UniversalQuantumRegister;

/**
 * @author Artur Vasilov
 */
public class UniversalTestFunction implements QuantumHashesEqualityTestFunction {

    @Override
    public boolean equals(QuantumHashResult first, QuantumHashResult second) {
        if (first.getHashRegisters().size() != second.getHashRegisters().size()) {
            return false;
        }
        for (int i = 0; i < first.getHashRegisters().size(); i++) {
            UniversalQuantumRegister firstRegister = (UniversalQuantumRegister) first.getHashRegisters().get(i);
            UniversalQuantumRegister secondRegister = (UniversalQuantumRegister) first.getHashRegisters().get(i);
            if (!firstRegister.equals(secondRegister)) {
                return false;
            }
        }
        return true;
    }
}