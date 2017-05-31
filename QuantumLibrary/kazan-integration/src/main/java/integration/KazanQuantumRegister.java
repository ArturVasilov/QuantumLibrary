package integration;

import com.google.gson.Gson;
import memorymanager.service_for_controller.ServiceManager;
import memorymanager.service_for_controller.commands.CommandsFromClientDTO;
import memorymanager.service_for_controller.commands.LogicalAddressingCommandFromClient;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.List;

/**
 * @author Artur Vasilov
 */
public class KazanQuantumRegister implements QuantumRegister {

    private static final String USER_ID = "ARTUR_VASILOV";

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

    public static boolean[] nativeMeasure(long registerId) {
        return new boolean[0];
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

        List<LogicalAddressingCommandFromClient> commandsList = new CommandsBuilder(qubits).commandsForOperator(operator);
        printCommands(commandsList);

        CommandsFromClientDTO commandsFromClient = new CommandsFromClientDTO();
        commandsFromClient.setLogicalAddressingCommandFromClientList(commandsList);
        commandsFromClient.setQubitCount(qubitsCount);
        String commands = new Gson().toJson(commandsList);
        serviceManager.putCommandsToExecutionQueue(USER_ID, commands);
    }

    private void printCommands(List<LogicalAddressingCommandFromClient> commands) {
        for (LogicalAddressingCommandFromClient command : commands) {
            System.out.println(command);
        }
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
    public QuantumRegister concatWith(QuantumRegister register) {
        //TODO : call fake native method
        return null;
    }

    @Override
    public boolean[] measure() {
        return nativeMeasure(id);
    }
}
