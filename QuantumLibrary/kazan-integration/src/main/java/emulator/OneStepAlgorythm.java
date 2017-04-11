package emulator;

import emulator.gates.UGate;

import java.util.HashMap;

/**
 * @author Artur Vasilov
 */
public class OneStepAlgorythm extends QuantumAlgorithm {
    public OneStepAlgorythm(int qubitsInRegister,
                            int firstQubitPosition,
                            int numberOfQubitsInGate,
                            Complex[][] transformationMatrix) throws Exception {
        stepsNumber = 1;
        QuantumSchemeStepQubitAttributes[][] algSheme = new QuantumSchemeStepQubitAttributes[qubitsInRegister][1];
        String gateId = "Gate";
        for (int i = 0; i < qubitsInRegister; i++) {
            if (i >= firstQubitPosition && i < firstQubitPosition + numberOfQubitsInGate) {
                algSheme[i][0] = new QuantumSchemeStepQubitAttributes(gateId, false);
            } else {
                algSheme[i][0] = new QuantumSchemeStepQubitAttributes();
            }
        }
        gates = new HashMap<>();
        mainGateIDs = new String[]{gateId};
        QuantumGate gate = new UGate(numberOfQubitsInGate, transformationMatrix);
        gates.put(gateId, gate);
        algorithmSchemeMatrix = algSheme;
        qubitsNumber = qubitsInRegister;
        size = (int) Math.pow(2, qubitsNumber);
    }
}
