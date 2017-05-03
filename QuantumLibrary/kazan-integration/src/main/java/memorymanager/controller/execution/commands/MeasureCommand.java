package memorymanager.controller.execution.commands;

import com.sun.istack.internal.NotNull;
import memorymanager.controller.addresses.GlobalQubitAddress;

/**
 * @author Artur Vasilov
 */
public class MeasureCommand extends PhysicalAddressingCommand {

    public MeasureCommand(@NotNull GlobalQubitAddress qubitForMeasure) {
        setCommandType(CommandTypes.MEASURE);
        setFirstQubit_Part1(qubitForMeasure);
    }
}
