package hashing.multiple;

import hashing.BaseQuantumHashFunction;
import hashing.QuantumHashResult;
import hashing.tests.QuantumHashesEqualityTestFunction;
import ru.kpfu.arturvasilov.core.Operators;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class SingleQubitRegistersQuantumHashFunction extends BaseQuantumHashFunction {

    private static final int HASH_QUBITS_COUNT = 8;

    private final ParametersProvider parametersProvider;

    public SingleQubitRegistersQuantumHashFunction(QuantumHashesEqualityTestFunction equalityTestFunction) {
        this(equalityTestFunction, DefaultParametersProvider.getInstance());
    }

    public SingleQubitRegistersQuantumHashFunction(QuantumHashesEqualityTestFunction equalityTestFunction,
                                                   ParametersProvider parametersProvider) {
        super(equalityTestFunction);
        this.parametersProvider = parametersProvider;
    }

    @Override
    public QuantumHashResult hash(int number) {
        List<QuantumRegister> registers = new ArrayList<>();
        int qubitsCount = HASH_QUBITS_COUNT;

        int lParameter = parametersProvider.lParameter(qubitsCount);
        int[] kParameters = parametersProvider.kParameters(qubitsCount);

        for (int i = 0; i < qubitsCount; i++) {
            QuantumRegister register = QuantumComputer.createNewRegister("0");
            double thetaInRadians = Math.PI * kParameters[i] * number / lParameter;
            register.apply(Operators.rotationY(thetaInRadians));
            registers.add(register);
        }

        return new QuantumHashResult(registers);
    }
}
