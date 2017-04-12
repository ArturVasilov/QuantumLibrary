package emulator;

import emulator.gates.UGate;

import java.util.HashMap;

/**
 * @author Artur Vasilov
 */
public class OneStepTwoQubitGateAlgorythm extends QuantumAlgorithm {
    public OneStepTwoQubitGateAlgorythm(int qubitsInRegister,
                                        int firstQubitPosition,
                                        int secondQubitPosition,
                                        Complex[][] transformationMatrix) throws Exception {
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
        QuantumGate gate = new UGate(2, transformationMatrix);
        gates.put(gateId, gate);
        algorithmSchemeMatrix = algSheme;
    }
}
