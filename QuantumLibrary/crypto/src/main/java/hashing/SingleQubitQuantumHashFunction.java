package hashing;

import ru.kpfu.arturvasilov.core.BooleanFunction;
import ru.kpfu.arturvasilov.core.Operators;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class SingleQubitQuantumHashFunction implements QuantumHashFunction {

    @Override
    public QuantumHashResult hash(int number) {
        double divider = Math.pow(2, BooleanFunction.binaryRepresentation(number).length);
        double thetaInRadians = 2 * Math.PI * number / divider;
        QuantumRegister register = QuantumComputer.createNewRegister("0");
        register.apply(Operators.rotationY(thetaInRadians));
        return new QuantumHashResult(register);
    }

    @Override
    public boolean compareWithHash(QuantumHashResult expectedHash, int number) {
        return false;
    }
}
