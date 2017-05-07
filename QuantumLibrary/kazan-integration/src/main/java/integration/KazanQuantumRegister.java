package integration;

import memorymanager.service_for_controller.ServiceManager;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

/**
 * @author Artur Vasilov
 */
public class KazanQuantumRegister implements QuantumRegister {

    private final long id;

    private final ServiceManager serviceManager;

    private final int qubitsCount;

    private final int[] qubits;

    public KazanQuantumRegister(long id, int qubitsCount) {
        this.id = id;
        this.qubitsCount = qubitsCount;
        this.qubits = new int[qubitsCount];
        serviceManager = ServiceManager.getServiceManager();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void apply(ComplexMatrix operator) {
        if (!operator.isUnitary()) {
            throw new IllegalArgumentException("Only unitary operators could be applied");
        }

        String commands = new CommandsBuilder(qubits).commandsForOperator(operator);
        serviceManager.putCommandsToExecutionQueue("ARTUR", commands);
    }

    @Override
    public void applyAtPositions(ComplexMatrix operator, int startQubit) {
        String errorMessage = "Cannot apply operator, check the positions";
        if (startQubit < 0 || startQubit >= qubitsCount) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (!operator.isUnitary()) {
            throw new IllegalArgumentException("Only unitary operators could be applied");
        }

        int registerSize = (int) Math.pow(2, qubitsCount);
        if ((int) Math.pow(2, startQubit) * operator.matrix.length > registerSize) {
            //operator has larger size than the rest of the register
            throw new IllegalArgumentException(errorMessage);
        }

        ComplexMatrix resultOperator;
        if (startQubit == 0) {
            resultOperator = operator.copy();
        } else {
            resultOperator = ComplexMatrix.identity(2)
                    .tensorPow(startQubit)
                    .tensorMultiplication(operator);
        }

        while (resultOperator.matrix.length < registerSize) {
            resultOperator = resultOperator.tensorMultiplication(ComplexMatrix.identity(2));
        }
        apply(resultOperator);
    }

    @Override
    public boolean[] measure() {
        return new boolean[0];
    }
}
