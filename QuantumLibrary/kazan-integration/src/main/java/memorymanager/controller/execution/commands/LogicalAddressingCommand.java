package memorymanager.controller.execution.commands;

import com.sun.istack.internal.NotNull;
import memorymanager.service_for_controller.addresses.LogicalQubitAddressForController;

public class LogicalAddressingCommand extends GeneralCommand<LogicalQubitAddressForController> {

    public static class Builder {

        private LogicalAddressingCommand logicalAddressingCommand;

        public Builder() {
            logicalAddressingCommand = new LogicalAddressingCommand();
        }

        public Builder setCommand(@NotNull CommandTypes commandType) {
            logicalAddressingCommand.setCommandType(commandType);
            return this;
        }

        public Builder setCommandParam(@NotNull Double commandParam) {
            logicalAddressingCommand.setCommandParam(commandParam);
            return this;
        }

        public Builder setFirstQubit_Part1(@NotNull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setFirstQubit_Part1(qubitAddress);
            return this;
        }

        public Builder setFirstQubit_Part2(@NotNull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setFirstQubit_Part2(qubitAddress);
            return this;
        }

        public Builder setSecondQubit_Part1(@NotNull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setSecondQubit_Part1(qubitAddress);
            return this;
        }

        public Builder setSecondQubit_Part2(@NotNull LogicalQubitAddressForController qubitAddress) {
            logicalAddressingCommand.setSecondQubit_Part2(qubitAddress);
            return this;
        }

        public LogicalAddressingCommand build() {
            return logicalAddressingCommand;
        }
    }
}


