package hashing;

import ru.kpfu.arturvasilov.core.Operators;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class SwapTestEqualityFunction implements QuantumHashesEqualityTestFunction {

    @Override
    public boolean equals(QuantumHashResult first, QuantumHashResult second) {
        if (first.getHashRegisters().size() != second.getHashRegisters().size()) {
            return false;
        }
        for (int index = 0; index < first.getHashRegisters().size(); index++) {
            QuantumRegister firstQubit = first.getHashRegisters().get(index);
            QuantumRegister secondQubit = second.getHashRegisters().get(index);
            QuantumRegister resultRegister = QuantumComputer.createNewRegister("0")
                    .concatWith(firstQubit)
                    .concatWith(secondQubit);
            resultRegister.applyAtPositions(Operators.hadamar(), 0);
            resultRegister.apply(Operators.fredkin());
            resultRegister.applyAtPositions(Operators.hadamar(), 0);
            boolean measureResult = resultRegister.measure()[0];
            QuantumComputer.destroyRegister(resultRegister.getId());
            if (measureResult) {
                return false;
            }
        }
        return true;
    }
}
