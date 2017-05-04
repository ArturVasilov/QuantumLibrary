package api;

import emulator.OneStepOneQubitGateAlgorithm;
import emulator.OneStepTwoQubitGateAlgorithm;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.Operators;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Artur Vasilov
 */
public class QuantumMemoryManager extends QuantumManager {

    Set proccessorsUnitsRegisters;

    private int proccessorsUnitsCount = 4;

    public QuantumMemoryManager() {
        proccessorsUnitsRegisters = new HashSet<Qubit>();
    }

//    Base operations

    public boolean qubitIsAlreadyInProccessor(Qubit q) {
        return proccessorsUnitsRegisters.contains(q);
    }

    public void load(Qubit q) throws Exception {
        if (proccessorsUnitsRegisters.size() == proccessorsUnitsCount && !qubitIsAlreadyInProccessor(q)) {
            throw (new Exception("Processor units overflow"));
        }

        proccessorsUnitsRegisters.add(q);
    }

    public void save(Qubit q) {
        proccessorsUnitsRegisters.remove(q);
    }


    public void checkQubitsBeforePerformTransformation(Qubit... qubits) throws Exception {
        for (Qubit q : qubits) {
            if (q.registerAddress.equals(qubitDestroyedRegisterAddress)) {
                throw (new Exception("One ore more qubits already destroyed!"));
            } else if (!qubitIsAlreadyInProccessor(q)) {
                throw (new Exception("Operating qubits are not loaded to proccessor units!"));
            }
        }
    }

    /**
     * This tranformation must be performed for qubit that loaded to proccessor unit
     */
    public void phase(double thetaInRadians, Qubit qubit) throws Exception {
        checkQubitsBeforePerformTransformation(qubit);
        RegisterInfo registerInfo = registers.get(qubit.registerAddress);
        OneStepOneQubitGateAlgorithm oneStepOneQubitGateAlgorithm = new OneStepOneQubitGateAlgorithm(
                registerInfo.register.getQubitsNumber(),
                Operators.phase(thetaInRadians),
                qubit.addressInRegister
        );
        registerInfo.register.performAlgorythm(oneStepOneQubitGateAlgorithm);
    }

    /**
     * This tranformation must be performed for qubit that loaded to proccessor unit
     */
    public void QET(double thetaInRadians, Qubit qubit) throws Exception {
        checkQubitsBeforePerformTransformation(qubit);
        RegisterInfo registerInfo = registers.get(qubit.registerAddress);
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(Math.cos(thetaInRadians / 2), 0));
        matrix.setValue(0, 1, new Complex(0, Math.sin(thetaInRadians / 2)));
        matrix.setValue(1, 0, new Complex(0, Math.sin(thetaInRadians / 2)));
        matrix.setValue(1, 1, new Complex(Math.cos(thetaInRadians / 2), 0));
        OneStepOneQubitGateAlgorithm oneStepOneQubitGateAlgorithm = new OneStepOneQubitGateAlgorithm(
                registerInfo.register.getQubitsNumber(),
                matrix,
                qubit.addressInRegister
        );
        registerInfo.register.performAlgorythm(oneStepOneQubitGateAlgorithm);
    }

    /**
     * This tranformation must be performed for qubits that loaded to proccessor units
     */
    public void cQET(double thetaInRadians, Qubit controllingQubit, Qubit controlledQubit) throws Exception {
        checkQubitsBeforePerformTransformation(controlledQubit, controllingQubit);
        RegisterInfo registerInfo = checkAndMergeRegistersIfNeedForQubits(controllingQubit, controlledQubit);
        ComplexMatrix matrix = ComplexMatrix.identity(4);
        matrix.setValue(0, 0, new Complex(Math.cos(thetaInRadians / 2), 0));
        matrix.setValue(0, 1, new Complex(0, Math.sin(thetaInRadians / 2)));
        matrix.setValue(1, 0, new Complex(0, Math.sin(thetaInRadians / 2)));
        matrix.setValue(1, 1, new Complex(Math.cos(thetaInRadians / 2), 0));
        OneStepTwoQubitGateAlgorithm algorythm = new OneStepTwoQubitGateAlgorithm(
                registerInfo.register.getQubitsNumber(),
                controllingQubit.addressInRegister,
                controlledQubit.addressInRegister,
                matrix
        );
        registerInfo.register.performAlgorythm(algorythm);
    }
}
