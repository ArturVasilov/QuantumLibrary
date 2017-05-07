package integration;

import com.google.gson.Gson;
import memorymanager.service_for_controller.commands.CommandsFromClientDTO;
import memorymanager.service_for_controller.commands.LogicalAddressingCommandFromClient;
import ru.kpfu.arturvasilov.core.ComplexMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class CommandsBuilder {

    private final int[] qubits;

    private final CommandsFromClientDTO commandsFromClientDTO = new CommandsFromClientDTO();
    private final List<LogicalAddressingCommandFromClient> commandsFromClient = new ArrayList<>();

    public CommandsBuilder(int[] qubits) {
        this.qubits = qubits;
    }

    public String commandsForOperator(ComplexMatrix operator) {


        commandsFromClientDTO.setLogicalAddressingCommandFromClientList(commandsFromClient);
        return new Gson().toJson(commandsFromClientDTO);
    }
}
