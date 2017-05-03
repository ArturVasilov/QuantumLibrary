package memorymanager.controller.memory;

import memorymanager.controller.execution.commands.PhysicalAddressingCommand;
import memorymanager.controller.execution.results.LowLevelResult;

import java.util.List;

/**
 * Interface for classes, which should operate like quantum memory
 *
 * @author Artur Vasilov
 */
public interface QuantumMemory {

    long getMaxMemoryFrequency();

    long getMinMemoryFrequency();

    long getFrequencyStep();

    int getProcessingUnitsCount();

    long getMaxMemoryTimeCycle();

    long getMemoryTimeStep();

    void initMemory();

    boolean isMemoryAvailable();

    List<LowLevelResult> perform(List<PhysicalAddressingCommand> physicalAddressingCommands);
}
