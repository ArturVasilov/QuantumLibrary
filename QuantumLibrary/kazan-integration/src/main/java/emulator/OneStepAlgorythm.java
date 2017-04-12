package emulator;

import emulator.gates.UGate;

import java.util.HashMap;
import java.util.List;

/**
 * Algorythm matrix must math to qubits order in algorythm scheme.
 * <p>
 * To not use controlQubit pass value NotAnIndex
 *
 * @author Artur Vasilov
 */

public class OneStepAlgorythm extends QuantumAlgorithm {

    public static final int NotAnIndex = -1;

    public OneStepAlgorythm(int qubitsInRegister,
                            int controlQubitIndex,
                            List<Integer> gateQubitIndexes,
                            Complex[][] transformationMatrix) throws Exception {
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
        QuantumGate gate = new UGate(gateQubitIndexes.size(), transformationMatrix);
        gates.put(gateId, gate);
        algorithmSchemeMatrix = algSheme;
        qubitsNumber = qubitsInRegister;
        size = (int) Math.pow(2, qubitsNumber);
    }
}
