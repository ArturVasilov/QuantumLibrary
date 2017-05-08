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
                currentMatrix.setValue(secondIndex, secondIndex, result.getValue(firstIndex, firstIndex).conjugate().multiply(multiplier));

                result = currentMatrix.multiply(result);
                currentMatrix = currentMatrix.transpose();

                int[] indexes = new OperationIndices(qubits.length, firstIndex, secondIndex).calculateIndexesOfQubits();

                ComplexMatrix tildaMatrix = new ComplexMatrix(2);
                tildaMatrix.setValue(0, 0, currentMatrix.getValue(firstIndex, firstIndex));
                tildaMatrix.setValue(0, 1, currentMatrix.getValue(firstIndex, secondIndex));
                tildaMatrix.setValue(1, 0, currentMatrix.getValue(secondIndex, firstIndex));
                tildaMatrix.setValue(1, 1, currentMatrix.getValue(secondIndex, secondIndex));
                transformCNOT(indexes, tildaMatrix, commands);
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

        transformCNOT(indexes, currentMatrix, commands);

        return commands;
    }

    private void transformCNOT(int[] indexes, ComplexMatrix currentTildaMatrix,
                               List<LogicalAddressingCommandFromClient> commands) {
        //addressOfQubit - адрес кубита, к которому применяется контрол нот
        //countOfQubit нужен для того, чтобы знать, сколько букв C
        //кол-во C = countOfQubit-1

        int CountOfQubit = qubits.length;
        if (CountOfQubit > 2) {
            //начало записи в массив
            int helpQubitIndex; //indexes of helper |0> qubits
            String[][] GIGANT = new String[indexes.length][CountOfQubit - 2];

            for (int i = 0; i < indexes.length; i++) {
                helpQubitIndex = CountOfQubit + 1;
                String[] s = new String[CountOfQubit - 2];
                int qubitsIndex = 0;

                int counter = 0;
                while (counter < 2) {
                    if (qubits[qubitsIndex] == indexes[i]) {
                        qubitsIndex++;
                    } else {
                        if (counter == 0) {
                            s[0] = "" + qubits[qubitsIndex];
                        } else {
                            s[0] = s[0] + "&" + qubits[qubitsIndex] + ">" + helpQubitIndex;
                        }
                        qubitsIndex++;
                        counter++;
                    }
                }

                helpQubitIndex++;

                int count = 1;
                while (count < CountOfQubit - 1 - 1) {
                    int secondIndex = s[count - 1].indexOf(">");
                    //helpQubitIndex = CountOfQubit+count+1;
                    s[count] = s[count - 1].substring(secondIndex + 1) + "&";
                    if (qubits[qubitsIndex] == indexes[i]) {
                        qubitsIndex++;
                    }
                    if (count != CountOfQubit - 3) {
                        //последний элемент - записываем в кубит indexes[i];
                        s[count] = s[count] + qubits[qubitsIndex] + ">" + helpQubitIndex;
                    } else {
                        s[count] = s[count] + qubits[qubitsIndex] + ">" + indexes[i];
                    }

                    qubitsIndex++;
                    count++;
                    helpQubitIndex++;
                }

                System.arraycopy(s, 0, GIGANT[i], 0, s.length);
            }

            //конец записи в массив
            String str;
            int ind1;
            int ind2;
            int qubit1;
            int qubit2;
            int qubit3;

            for (int i = 0; i < GIGANT.length - 1; i++) {
                for (int j = 0; j < GIGANT[i].length; j++) {
                    str = GIGANT[i][j];
                    ind1 = str.indexOf("&");
                    ind2 = str.indexOf(">");

                    qubit1 = Integer.parseInt(str.substring(0, ind1));
                    qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                    qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                    transformCCNOT(qubit1, qubit2, qubit3, commands);
                }
                //возвращаем значение нулевому вспомогательному кубиту
                //последнее значение не смотрим, потому что там нет
                for (int j = GIGANT[i].length - 2; j > -1; j--) {
                    str = GIGANT[i][j];
                    ind1 = str.indexOf("&");
                    ind2 = str.indexOf(">");

                    qubit1 = Integer.parseInt(str.substring(0, ind1));
                    qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                    qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                    if (qubit3 > CountOfQubit) {
                        transformCCNOT(qubit1, qubit2, qubit3, commands);
                    }
                }
            }

            //Tilda
            for (int j = 0; j < GIGANT[GIGANT.length - 1].length - 1; j++) {
                str = GIGANT[GIGANT.length - 1][j];
                ind1 = str.indexOf("&");
                ind2 = str.indexOf(">");

                qubit1 = Integer.parseInt(str.substring(0, ind1));
                qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                transformCCNOT(qubit1, qubit2, qubit3, commands);
            }
            str = GIGANT[GIGANT.length - 1][GIGANT[GIGANT.length - 1].length - 1];
            ind1 = str.indexOf("&");
            ind2 = str.indexOf(">");
            qubit1 = Integer.parseInt(str.substring(0, ind1));
            qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));

            transformCCNOT(qubit1, qubit2, Math.max(qubit1, qubit2) + 1, commands);
            transformTilda(Math.max(qubit1, qubit2) + 1, qubit2, currentTildaMatrix, commands);
            transformCCNOT(qubit1, qubit2, Math.max(qubit1, qubit2) + 1, commands);

            for (int j = GIGANT[GIGANT.length - 1].length - 2; j > -1; j--) {
                str = GIGANT[GIGANT.length - 1][j];
                ind1 = str.indexOf("&");
                ind2 = str.indexOf(">");

                qubit1 = Integer.parseInt(str.substring(0, ind1));
                qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                if (qubit3 > CountOfQubit) {
                    transformCCNOT(qubit1, qubit2, qubit3, commands);
                }
            }

            //возвращаем базис
            //System.out.println("=============================================================================");

            for (int i = GIGANT.length - 2; i > -1; i--) {
                for (int j = 0; j < GIGANT[i].length - 1; j++) {
                    str = GIGANT[i][j];
                    ind1 = str.indexOf("&");
                    ind2 = str.indexOf(">");

                    qubit1 = Integer.parseInt(str.substring(0, ind1));
                    qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                    qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                    transformCCNOT(qubit1, qubit2, qubit3, commands);
                }

                for (int j = GIGANT[i].length - 2; j > -1; j--) {
                    str = GIGANT[i][j];
                    ind1 = str.indexOf("&");
                    ind2 = str.indexOf(">");

                    qubit1 = Integer.parseInt(str.substring(0, ind1));
                    qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                    qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                    if (qubit3 > CountOfQubit) {
                        transformCCNOT(qubit1, qubit2, qubit3, commands);
                    }
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
                transformTilda(2, 1, currentTildaMatrix, commands);
            } else {
                transformTilda(1, 2, currentTildaMatrix, commands);
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

    // разложение тильда матрицы на повороты
    //после разложения получим матрицы A,B,C и контролируемый оператор переделаем в неконтролируемый
    private void transformTilda(int controlQubitNumber, int toQubitNumber, ComplexMatrix tildaMatrix,
                                List<LogicalAddressingCommandFromClient> commands) {
        Complex det1 = tildaMatrix.getValue(0, 0).multiply(tildaMatrix.getValue(1, 1));
        Complex det2 = tildaMatrix.getValue(0, 1).multiply(tildaMatrix.getValue(1, 0));
        Complex det = det1.sub(det2); // определитель матрицы

        double gamma = Math.acos(det.getReal()) / 2;

        Complex A = tildaMatrix.getValue(0, 0).multiply(new Complex(Math.cos(gamma), Math.sin(gamma)));
        Complex B = tildaMatrix.getValue(0, 1).multiply(new Complex(Math.cos(gamma), Math.sin(gamma)));

        double Teta = 2 * Math.acos(A.getReal());

        if (Math.abs(Teta) > 0.0001) {
            //TODO
            double nx = (-1) * B.getImaginary() / Math.sin(Teta / 2);//координата вектора n по x
            double ny = (-1) * B.getReal() / Math.sin(Teta / 2);
            double nz = (-1) * A.getImaginary() / Math.sin(Teta / 2);

            double alpha = 0, beta = 0, lambda = 0;
            //альфа, бета и лямбда нужны, чтобы разложить tildaMatrix на A,B,C
            if (nx != 0 && Math.cos(Teta) != 0) {
                lambda = (Math.atan(ny / nx) + Math.atan(nz * Math.sin(Teta) / Math.cos(Teta))) / 2;
                alpha = ((-1) * Math.atan(ny / nx) + Math.atan(nz * Math.sin(Teta) / Math.cos(Teta))) / 2;
                if (Math.cos(lambda + alpha) != 0) {
                    beta = Math.acos(Math.cos(Teta) / Math.cos(lambda + alpha));
                }
            }

            commands.addAll(operatorCNOT(controlQubitNumber, toQubitNumber));
        }
    }

    private void transformCCNOT(int qubit1, int qubit2, int qubit3,
                                List<LogicalAddressingCommandFromClient> commands) {
        //реализация огроменной схемы из H, T, T*, S

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
        //System.out.println("PHASE(PI/2) " + qubit);
        //System.out.println("QET(PI/2) " + qubit);
        //System.out.println("PHASE(PI/2) " + qubit);

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
        //System.out.println("CQET(PI)" + qubit1 + " " + qubit2);
        //System.out.println("PHASE(PI/2) " + qubit1);

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
        //System.out.println("PHASE(PI/4) " + qubit);

        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );
        return commands;
    }

    public List<LogicalAddressingCommandFromClient> operatorTHermitian(int qubit) {
        //System.out.println("PHASE(-PI/2) " + qubit);

        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                -Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );
        return commands;
    }

    public List<LogicalAddressingCommandFromClient> operatorS(int qubit) {
        //System.out.println("PHASE(PI/2) " + qubit);

        List<LogicalAddressingCommandFromClient> commands = new ArrayList<>();
        commands.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI / 2,
                new LogicalQubitAddressFromClient(qubit))
        );
        return commands;
    }


}
