package api.model;

import api.QuantumManager;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;

/**
 * @author Artur Vasilov
 */
public class QuantumProcessorHelper extends QuantumManager {

    void physicalQET(Qubit a, Qubit b, double thetaPhaseInRadians) throws Exception {
        RegisterInfo regInfo = checkAndMergeRegistersIfNeedForQubits(a, b);
        ComplexMatrix matrix = ComplexMatrix.identity(4);
        matrix.setValue(1, 1, new Complex(Math.cos(thetaPhaseInRadians / 2), 0));
        matrix.setValue(1, 2, new Complex(0, Math.sin(thetaPhaseInRadians / 2)));
        matrix.setValue(2, 1, new Complex(0, Math.sin(thetaPhaseInRadians / 2)));
        matrix.setValue(2, 2, new Complex(Math.cos(thetaPhaseInRadians / 2), 0));
        performTransitionForQubits(null, matrix, regInfo, a, b);
    }

    void physicalCQET(Qubit a, Qubit control, Qubit b, double thetaInRadians) throws Exception {
        RegisterInfo regInfo = checkAndMergeRegistersIfNeedForQubits(a, control, b);
        int minAddressOfAB = Math.min(qubitAddressInRegister(a), qubitAddressInRegister(b));
        int maxAddressOfAB = Math.max(qubitAddressInRegister(a), qubitAddressInRegister(b));

        if (qubitAddressInRegister(control) < qubitAddressInRegister(a) &&
                qubitAddressInRegister(control) < qubitAddressInRegister(b)) {
            //control qubit is first
            ComplexMatrix matrix = ComplexMatrix.identity(8);
            matrix.setValue(1, 1, new Complex(Math.cos(thetaInRadians / 2), 0));
            matrix.setValue(1, 2, new Complex(0, Math.sin(thetaInRadians / 2)));
            matrix.setValue(2, 1, new Complex(0, Math.sin(thetaInRadians / 2)));
            matrix.setValue(2, 2, new Complex(Math.cos(thetaInRadians / 2), 0));
            performTransitionForQubits(null, matrix, regInfo, a, b, control);
        } else if (qubitAddressInRegister(control) > minAddressOfAB &&
                qubitAddressInRegister(control) < maxAddressOfAB) {
            //control qubit is between a and b
            ComplexMatrix matrix = ComplexMatrix.zeros(8);
            matrix.setValue(0, 2, Complex.unit());
            matrix.setValue(1, 3, new Complex(Math.cos(thetaInRadians / 2), 0));
            matrix.setValue(1, 4, new Complex(0, Math.sin(thetaInRadians / 2)));
            matrix.setValue(2, 0, Complex.unit());
            matrix.setValue(3, 1, Complex.unit());
            matrix.setValue(4, 3, new Complex(0, Math.sin(thetaInRadians / 2)));
            matrix.setValue(4, 4, new Complex(Math.cos(thetaInRadians / 2), 0));
            matrix.setValue(5, 5, Complex.unit());
            matrix.setValue(6, 6, Complex.unit());
            matrix.setValue(7, 7, Complex.unit());
            performTransitionForQubits(null, matrix, regInfo, a, b, control);
        } else {
            //control qubit is last
            ComplexMatrix matrix = ComplexMatrix.zeros(8);
            matrix.setValue(0, 4, Complex.unit());
            matrix.setValue(1, 0, Complex.unit());
            matrix.setValue(2, 5, new Complex(Math.cos(thetaInRadians / 2), 0));
            matrix.setValue(2, 6, new Complex(0, Math.sin(thetaInRadians / 2)));
            matrix.setValue(3, 1, Complex.unit());
            matrix.setValue(4, 5, new Complex(0, Math.sin(thetaInRadians / 2)));
            matrix.setValue(4, 6, new Complex(Math.cos(thetaInRadians / 2), 0));
            matrix.setValue(5, 2, Complex.unit());
            matrix.setValue(6, 7, Complex.unit());
            matrix.setValue(7, 3, Complex.unit());
            performTransitionForQubits(null, matrix, regInfo, a, b, control);
        }
    }

    void physicalPHASE(Qubit a, Qubit b, double thetaInRadians) throws Exception {
        RegisterInfo regInfo = checkAndMergeRegistersIfNeedForQubits(a, b);
        ComplexMatrix matrix = ComplexMatrix.identity(4);
        matrix.setValue(1, 1, new Complex(Math.cos(-thetaInRadians / 2.0), Math.sin(-thetaInRadians / 2.0)));
        matrix.setValue(2, 2, new Complex(Math.cos(thetaInRadians / 2.0), Math.sin(thetaInRadians / 2.0)));
        performTransitionForQubits(null, matrix, regInfo, a, b);
    }

    void mergeQubits(Qubit... qubits) throws Exception {
        checkAndMergeRegistersIfNeedForQubits(qubits);
    }
}
