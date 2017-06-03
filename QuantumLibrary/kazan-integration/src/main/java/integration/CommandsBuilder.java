package integration;

import memorymanager.controller.execution.commands.CommandTypes;
import memorymanager.service_for_controller.addresses.LogicalQubitAddressFromClient;
import memorymanager.service_for_controller.commands.LogicalAddressingCommandFromClient;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class CommandsBuilder {

    private final int[] qubits;

    public CommandsBuilder(int[] qubits) {
        this.qubits = qubits;
    }

    public List<LogicalAddressingCommandFromClient> commandsForOperator(ComplexMatrix operator) {
        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();

        ComplexMatrix result = operator.copy();
        int size = operator.matrix.length;

        //all matrices except the last (when size is 2)
        for (int step = 0; step < size - 2; step++) {
            for (int k = 1; k < size - step; k++) {
                ComplexMatrix currentMatrix = ComplexMatrix.identity(size);
                double divider = Math.sqrt(result.getValue(step, step).norma() + result.getValue(k, step).norma());

                //noinspection UnnecessaryLocalVariable
                int firstIndex = step;
                int secondIndex = step + k;

                Complex multiplier = new Complex(1 / divider, 0);
                currentMatrix.setValue(firstIndex, firstIndex, result.getValue(firstIndex, firstIndex).conjugate().multiply(multiplier));
                currentMatrix.setValue(firstIndex, secondIndex, result.getValue(secondIndex, firstIndex).conjugate().multiply(multiplier));
                currentMatrix.setValue(secondIndex, firstIndex, result.getValue(secondIndex, firstIndex).conjugate().multiply(multiplier));
                currentMatrix.setValue(secondIndex, secondIndex, result.getValue(firstIndex, firstIndex).multiply(new Complex(-1, 0)).multiply(multiplier));

                result = currentMatrix.multiply(result);
                currentMatrix = currentMatrix.transpose();

                int[] indexes = new OperationIndices(qubits.length, firstIndex, secondIndex).calculateIndexesOfQubits();

                ComplexMatrix tildaMatrix = new ComplexMatrix(2);
                tildaMatrix.setValue(0, 0, currentMatrix.getValue(firstIndex, firstIndex));
                tildaMatrix.setValue(0, 1, currentMatrix.getValue(firstIndex, secondIndex));
                tildaMatrix.setValue(1, 0, currentMatrix.getValue(secondIndex, firstIndex));
                tildaMatrix.setValue(1, 1, currentMatrix.getValue(secondIndex, secondIndex));
                operatorCNOT(indexes, tildaMatrix, commands);
            }
        }

        //the last matrix of the decomposition
        ComplexMatrix currentMatrix = ComplexMatrix.identity(size);
        int firstIndex = size - 2;
        int secondIndex = size - 1;
        currentMatrix.setValue(firstIndex, firstIndex, result.getValue(firstIndex, firstIndex).conjugate());
        currentMatrix.setValue(firstIndex, secondIndex, result.getValue(secondIndex, firstIndex).conjugate());
        currentMatrix.setValue(secondIndex, firstIndex, result.getValue(firstIndex, secondIndex).conjugate());
        currentMatrix.setValue(secondIndex, secondIndex, result.getValue(secondIndex, secondIndex).conjugate());
        currentMatrix = currentMatrix.transpose();

        int[] indexes = new OperationIndices(qubits.length, firstIndex, secondIndex).calculateIndexesOfQubits();

        operatorCNOT(indexes, currentMatrix, commands);

        return commands;
    }

    private void operatorCNOT(int[] indexes, ComplexMatrix currentTildaMatrix,
                              List<LogicalAddressingCommandFromClient> commands) {
        int qubitsCount = qubits.length;
        if (qubitsCount > 2) {
            int helperQubitIndex;
            String[][] allOperations = new String[indexes.length][qubitsCount - 2];

            for (int i = 0; i < indexes.length; i++) {
                helperQubitIndex = qubitsCount + 1;
                String[] operations = new String[qubitsCount - 2];
                int qubitsIndex = 0;

                int operationsIndex = 0;
                while (operationsIndex < 2) {
                    if (qubits[qubitsIndex] == indexes[i]) {
                        qubitsIndex++;
                    } else {
                        if (operationsIndex == 0) {
                            operations[0] = String.valueOf(qubits[qubitsIndex]);
                        } else {
                            operations[0] += "&" + qubits[qubitsIndex] + ">" + helperQubitIndex;
                        }
                        qubitsIndex++;
                        operationsIndex++;
                    }
                }
                helperQubitIndex++;

                int count = 1;
                while (count < qubitsCount - 2) {
                    int secondIndex = operations[count - 1].indexOf(">");
                    operations[count] = operations[count - 1].substring(secondIndex + 1) + "&";
                    if (qubits[qubitsIndex] == indexes[i]) {
                        qubitsIndex++;
                    }
                    if (count == qubitsCount - 3) {
                        operations[count] += qubits[qubitsIndex] + ">" + indexes[i];
                    } else {
                        operations[count] += qubits[qubitsIndex] + ">" + helperQubitIndex;
                    }

                    qubitsIndex++;
                    count++;
                    helperQubitIndex++;
                }

                System.arraycopy(operations, 0, allOperations[i], 0, operations.length);
            }

            for (int i = 0; i < allOperations.length - 1; i++) {
                for (int j = 0; j < allOperations[i].length; j++) {
                    applyCCNOTIfNeeded(allOperations[i][j], commands, false);
                }
                for (int j = allOperations[i].length - 2; j > -1; j--) {
                    applyCCNOTIfNeeded(allOperations[i][j], commands, true);
                }
            }
            for (int j = 0; j < allOperations[allOperations.length - 1].length - 1; j++) {
                applyCCNOTIfNeeded(allOperations[allOperations.length - 1][j], commands, false);
            }

            String operation = allOperations[allOperations.length - 1][allOperations[allOperations.length - 1].length - 1];
            int firstIndex = operation.indexOf("&");
            int secondIndex = operation.indexOf(">");
            int firstQubit = Integer.parseInt(operation.substring(0, firstIndex));
            int secondQubit = Integer.parseInt(operation.substring(firstIndex + 1, secondIndex));

            operatorCCNOT(firstQubit, secondQubit, Math.max(firstQubit, secondQubit) + 1, commands);
            operatorTilda(Math.max(firstQubit, secondQubit) + 1, secondQubit, currentTildaMatrix, commands);
            operatorCCNOT(firstQubit, secondQubit, Math.max(firstQubit, secondQubit) + 1, commands);

            for (int j = allOperations[allOperations.length - 1].length - 2; j > -1; j--) {
                applyCCNOTIfNeeded(allOperations[allOperations.length - 1][j], commands, true);
            }

            for (int i = allOperations.length - 2; i > -1; i--) {
                for (int j = 0; j < allOperations[i].length - 1; j++) {
                    applyCCNOTIfNeeded(allOperations[i][j], commands, false);
                }

                for (int j = allOperations[i].length - 2; j > -1; j--) {
                    applyCCNOTIfNeeded(allOperations[i][j], commands, true);
                }

            }
        } else {
            for (int i = 0; i < indexes.length - 1; i++) {
                if (indexes[i] == 1) {
                    commands.addAll(operatorCNOT(2, 1));
                } else {
                    commands.addAll(operatorCNOT(1, 2));
                }
            }
            if (indexes[indexes.length - 1] == 1) {
                operatorTilda(2, 1, currentTildaMatrix, commands);
            } else {
                operatorTilda(1, 2, currentTildaMatrix, commands);
            }

            for (int i = indexes.length - 1; i >= 0; i--) {
                if (indexes[i] == 1) {
                    commands.addAll(operatorCNOT(2, 1));
                } else {
                    commands.addAll(operatorCNOT(1, 2));
                }
            }
        }
    }

    private void applyCCNOTIfNeeded(String operation, List<LogicalAddressingCommandFromClient> commands, boolean withCheck) {
        int firstIndex = operation.indexOf("&");
        int secondIndex = operation.indexOf(">");

        int firstQubit = Integer.parseInt(operation.substring(0, firstIndex));
        int secondQubit = Integer.parseInt(operation.substring(firstIndex + 1, secondIndex));
        int thirdQubit = Integer.parseInt(operation.substring(secondIndex + 1));
        if (!withCheck || thirdQubit > qubits.length) {
            operatorCCNOT(firstQubit, secondQubit, thirdQubit, commands);
        }
    }

    private void operatorTilda(int controlQubitNumber, int toQubitNumber, ComplexMatrix tildaMatrix,
                               List<LogicalAddressingCommandFromClient> commands) {
        Complex det = tildaMatrix.getValue(0, 0).multiply(tildaMatrix.getValue(1, 1))
                .sub(tildaMatrix.getValue(0, 1).multiply(tildaMatrix.getValue(1, 0)));
        double gamma = Math.acos(det.getReal()) / 2;
        Complex A = tildaMatrix.getValue(0, 0).multiply(new Complex(Math.cos(gamma), Math.sin(gamma)));
        double theta = 2 * Math.acos(A.getReal());
        if (Math.abs(theta) > 0.0001) {
            commands.addAll(operatorCNOT(controlQubitNumber, toQubitNumber));
        }
    }

    private void operatorCCNOT(int qubit1, int qubit2, int qubit3,
                               List<LogicalAddressingCommandFromClient> commands) {
        commands.addAll(operatorHadamar(qubit3));
        commands.addAll(operatorCNOT(qubit2, qubit3));
        commands.addAll(operatorTHermitian(qubit3));
        commands.addAll(operatorCNOT(qubit1, qubit3));
        commands.addAll(operatorT(qubit3));
        commands.addAll(operatorCNOT(qubit2, qubit3));
        commands.addAll(operatorTHermitian(qubit3));
        commands.addAll(operatorCNOT(qubit1, qubit3));
        commands.addAll(operatorT(qubit3));
        commands.addAll(operatorHadamar(qubit3));
        commands.addAll(operatorTHermitian(qubit2));
        commands.addAll(operatorCNOT(qubit1, qubit2));
        commands.addAll(operatorTHermitian(qubit2));
        commands.addAll(operatorCNOT(qubit1, qubit2));
        commands.addAll(operatorS(qubit2));
        commands.addAll(operatorT(qubit1));
    }

    public List<LogicalAddressingCommandFromClient> operatorHadamar(int qubit) {
        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );

        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.QET,
                Math.PI,
                new LogicalQubitAddressFromClient(qubit))
        );

        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );
        return commands;
    }

    public List<LogicalAddressingCommandFromClient> operatorCNOT(int qubit1, int qubit2) {
        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.CQET,
                Math.PI,
                new LogicalQubitAddressFromClient(qubit1),
                new LogicalQubitAddressFromClient(qubit2))
        );
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI / 4,
                new LogicalQubitAddressFromClient(qubit1))
        );
        return commands;
    }

    public List<LogicalAddressingCommandFromClient> operatorT(int qubit) {
        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );
        return commands;
    }

    public List<LogicalAddressingCommandFromClient> operatorTHermitian(int qubit) {
        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                -Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );
        return commands;
    }

    public List<LogicalAddressingCommandFromClient> operatorS(int qubit) {
        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );
        return commands;
    }
}