package emulator;

import ru.kpfu.arturvasilov.core.ComplexMatrix;

import java.util.HashMap;
import java.util.List;

/**
 * Algorythm matrix must math to qubits order in algorythm scheme.
 * <p>
 * To not use controlQubit pass value EMPTY_ADDRESS
 *
 * @author Artur Vasilov
 */
public class OneStepAlgorithm extends QuantumAlgorithm {

    public static final int EMPTY_ADDRESS = -1;

    public OneStepAlgorithm(int qubitsInRegister,
                            int controlQubitIndex,
                            List<Integer> gateQubitIndexes,
                            ComplexMatrix transformationMatrix) throws Exception {
        stepsNumber = 1;
        QuantumSchemeStepQubitAttributes[][] algSheme = new QuantumSchemeStepQubitAttributes[qubitsInRegister][1];
        String gateId = "Gate";
        for (int index = 0; index < qubitsInRegister; index++) {
            if (gateQubitIndexes.contains(index)) {
                algSheme[index][0] = new QuantumSchemeStepQubitAttributes(gateId, false);
            } else if (index == controlQubitIndex) {
                algSheme[index][0] = new QuantumSchemeStepQubitAttributes(gateId, true);
            } else {
                algSheme[index][0] = new QuantumSchemeStepQubitAttributes();
            }
        }
        gates = new HashMap<>();
        mainGateIDs = new String[]{gateId};
        gates.put(gateId, transformationMatrix);
        algorithmSchemeMatrix = algSheme;
        qubitsNumber = qubitsInRegister;
        size = (int) Math.pow(2, qubitsNumber);
    }
}
