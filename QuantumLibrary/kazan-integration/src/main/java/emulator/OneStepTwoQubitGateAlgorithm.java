package emulator;

import ru.kpfu.arturvasilov.core.ComplexMatrix;

import java.util.HashMap;

/**
 * @author Artur Vasilov
 */
public class OneStepTwoQubitGateAlgorithm extends QuantumAlgorithm {

    public OneStepTwoQubitGateAlgorithm(int qubitsInRegister,
                                        int firstQubitPosition,
                                        int secondQubitPosition,
                                        ComplexMatrix transformationMatrix) throws Exception {
        stepsNumber = 1;
        QuantumSchemeStepQubitAttributes[][] algSheme = new QuantumSchemeStepQubitAttributes[qubitsInRegister][1];
        String gateId = "ControlledGate";
        for (int index = 0; index < qubitsInRegister; index++) {
            if (index == secondQubitPosition) {
                algSheme[index][0] = new QuantumSchemeStepQubitAttributes(gateId, false);
            } else if (index == firstQubitPosition) {
                algSheme[index][0] = new QuantumSchemeStepQubitAttributes(gateId, true);
            } else {
                algSheme[index][0] = new QuantumSchemeStepQubitAttributes();
            }
        }
        gates = new HashMap<>();
        mainGateIDs = new String[]{gateId};
        gates.put(gateId, transformationMatrix);
        algorithmSchemeMatrix = algSheme;
    }
}
