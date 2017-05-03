package memorymanager.controller.execution.commands;

import com.sun.istack.internal.NotNull;
import memorymanager.controller.addresses.GlobalQubitAddress;

public class InitCommand extends PhysicalAddressingCommand {

    public InitCommand(@NotNull GlobalQubitAddress qubit_Part1, @NotNull GlobalQubitAddress qubit_Part2) {
        setCommandType(CommandTypes.INIT);
        setFirstQubit_Part1(qubit_Part1);
        setFirstQubit_Part2(qubit_Part2);
    }
}
