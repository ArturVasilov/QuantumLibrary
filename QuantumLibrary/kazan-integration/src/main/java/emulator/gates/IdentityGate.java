package emulator.gates;

import emulator.Complex;
import emulator.QuantumGate;

/**
 * @author Artur Vasilov
 */
public class IdentityGate extends QuantumGate {

    public IdentityGate() {
        this.qubitsNumber = 1;
        this.size = 2;
    }

    @Override
    public Complex[][] getMatrix() {
        Complex result[][] = {
                {Complex.unit(), Complex.zero()},
                {Complex.zero(), Complex.unit()}
        };
        return result;
    }
}
