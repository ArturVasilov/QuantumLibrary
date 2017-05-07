package integration;

import com.google.gson.GsonBuilder;
import memorymanager.controller.execution.commands.CommandTypes;
import memorymanager.service_for_controller.addresses.LogicalQubitAddressFromClient;
import memorymanager.service_for_controller.commands.CommandsFromClientDTO;
import memorymanager.service_for_controller.commands.LogicalAddressingCommandFromClient;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;

import java.util.LinkedList;
import java.util.List;

public class Quantum {

    public static CommandsFromClientDTO commandsFromClientDTO = new CommandsFromClientDTO();
    public static List<LogicalAddressingCommandFromClient> commandFromClientList = new LinkedList<>();

    public static void BeginQuantum() {
        commandsFromClientDTO = new CommandsFromClientDTO();
        commandFromClientList = new LinkedList<>();
    }

    public static void EndQuantum() {
        commandsFromClientDTO.setLogicalAddressingCommandFromClientList(commandFromClientList);
        //serviceManager.putCommandsToExequtionQueue("1", new GsonBuilder().create().toJson(commandsFromClientDTO));
        String ss = new GsonBuilder().create().toJson(commandsFromClientDTO);
        System.out.println("ЗАВЕРШЕНО!");
        System.out.println("ss=" + ss);
    }

    public static void runUnitaryCalculation(ComplexMatrix operator, int[] qubits) {
        BeginQuantum();

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

                ComplexMatrix resTilda = new ComplexMatrix(2);
                resTilda.setValue(0, 0, currentMatrix.getValue(firstIndex, firstIndex));
                resTilda.setValue(0, 1, currentMatrix.getValue(firstIndex, secondIndex));
                resTilda.setValue(1, 0, currentMatrix.getValue(secondIndex, firstIndex));
                resTilda.setValue(1, 1, currentMatrix.getValue(secondIndex, secondIndex));
                unusualCNOTAndTilda(indexes, qubits, resTilda);
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

        unusualCNOTAndTilda(indexes, qubits, currentMatrix);

        EndQuantum();
    }


    public static void unusualCNOTAndTilda(int[] indexes, int[] qubits, ComplexMatrix resTilda) {
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
                    if (qubits[qubitsIndex] == indexes[i])
                        qubitsIndex++;
                    else {
                        if (counter == 0)
                            s[0] = "" + qubits[qubitsIndex];
                        else
                            s[0] = s[0] + "&" + qubits[qubitsIndex] + ">" + helpQubitIndex;
                        qubitsIndex++;
                        counter++;
                    }
                }

                helpQubitIndex++;

                int count = 1;
                //заполняем s[i]
                while (count < CountOfQubit - 1 - 1) {
                    int secondIndex = s[count - 1].indexOf(">");
                    //helpQubitIndex = CountOfQubit+count+1;
                    s[count] = s[count - 1].substring(secondIndex + 1) + "&";
                    if (qubits[qubitsIndex] == indexes[i])
                        qubitsIndex++;
                    if (count != CountOfQubit - 3)//последний элемент - записываем в кубит indexes[i];
                        s[count] = s[count] + qubits[qubitsIndex] + ">" + helpQubitIndex;
                    else
                        s[count] = s[count] + qubits[qubitsIndex] + ">" + indexes[i];

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
                    CCNOT(qubit1, qubit2, qubit3);
                    //System.out.println("CCNOT="+qubit1+", "+qubit2+" TO "+qubit3);
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
                        CCNOT(qubit1, qubit2, qubit3);
                        //System.out.println("Возвращение CCNOT="+qubit1+", "+qubit2+" TO "+qubit3);
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
                CCNOT(qubit1, qubit2, qubit3);
                //System.out.println("Tilda CCNOT="+qubit1+" "+qubit2+" "+qubit3);
            }
            str = GIGANT[GIGANT.length - 1][GIGANT[GIGANT.length - 1].length - 1];
            ind1 = str.indexOf("&");
            ind2 = str.indexOf(">");
            qubit1 = Integer.parseInt(str.substring(0, ind1));
            qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
            qubit3 = Integer.parseInt(str.substring(ind2 + 1));

            System.out.println("Tilda " + qubit1 + " " + qubit2 + " " + qubit3);
            CCNOT(qubit1, qubit2, Math.max(qubit1, qubit2) + 1);
            decompositionTildaOperator(Math.max(qubit1, qubit2) + 1, qubit2, resTilda);
            CCNOT(qubit1, qubit2, Math.max(qubit1, qubit2) + 1);

            for (int j = GIGANT[GIGANT.length - 1].length - 2; j > -1; j--) {
                str = GIGANT[GIGANT.length - 1][j];
                ind1 = str.indexOf("&");
                ind2 = str.indexOf(">");

                qubit1 = Integer.parseInt(str.substring(0, ind1));
                qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                if (qubit3 > CountOfQubit) {
                    CCNOT(qubit1, qubit2, qubit3);
                    //System.out.println("Tilda возвращение CCNOT="+qubit1+" "+qubit2+" "+qubit3);
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
                    CCNOT(qubit1, qubit2, qubit3);
                    //System.out.println("CCNOT="+qubit1+", "+qubit2+" TO "+qubit3);
                }

                for (int j = GIGANT[i].length - 2; j > -1; j--) {
                    str = GIGANT[i][j];
                    ind1 = str.indexOf("&");
                    ind2 = str.indexOf(">");

                    qubit1 = Integer.parseInt(str.substring(0, ind1));
                    qubit2 = Integer.parseInt(str.substring(ind1 + 1, ind2));
                    qubit3 = Integer.parseInt(str.substring(ind2 + 1));
                    if (qubit3 > CountOfQubit) {
                        CCNOT(qubit1, qubit2, qubit3);
                        //System.out.println("Возвращение CCNOT="+qubit1+", "+qubit2+" TO "+qubit3);
                    }
                }

            }


        } else {
            for (int i = 0; i < indexes.length - 1; i++) {
                if (indexes[i] == 1) {
                    CNOT(2, 1);
                } else {
                    CNOT(1, 2);
                }
            }
            if (indexes[indexes.length - 1] == 1) {
                decompositionTildaOperator(2, 1, resTilda);
            } else {
                decompositionTildaOperator(1, 2, resTilda);
            }

            for (int i = indexes.length - 1; i > -1; i--) {
                if (indexes[i] == 1)
                    CNOT(2, 1);
                else
                    CNOT(1, 2);
            }


        }

    }

    // разложение тильда матрицы на повороты
    //после разложения получим матрицы A,B,C и контролируемый оператор переделаем в неконтролируемый
    public static void decompositionTildaOperator(int controlQubitNumber, int toQubitNumber, ComplexMatrix resTilda) {
        Complex det1 = resTilda.getValue(0, 0).multiply(resTilda.getValue(1, 1));
        Complex det2 = resTilda.getValue(0, 1).multiply(resTilda.getValue(1, 0));
        Complex det = det1.sub(det2); // определитель матрицы

        double gamma = Math.acos(det.getReal()) / 2;

        Complex A = resTilda.getValue(0, 0).multiply(new Complex(Math.cos(gamma), Math.sin(gamma)));
        Complex B = resTilda.getValue(0, 1).multiply(new Complex(Math.cos(gamma), Math.sin(gamma)));

        double Teta = 2 * Math.acos(A.getReal());

        if (Math.abs(Teta) > 0.0001) {
            double nx = (-1) * B.getImaginary() / Math.sin(Teta / 2);//координата вектора n по x
            double ny = (-1) * B.getReal() / Math.sin(Teta / 2);
            double nz = (-1) * A.getImaginary() / Math.sin(Teta / 2);

            double alpha = 0, beta = 0, lambda = 0;
            //альфа, бета и лямбда нужны, чтобы разложить resTilda на A,B,C
            if (nx != 0 && Math.cos(Teta) != 0) {
                lambda = (Math.atan(ny / nx) + Math.atan(nz * Math.sin(Teta) / Math.cos(Teta))) / 2;
                alpha = ((-1) * Math.atan(ny / nx) + Math.atan(nz * Math.sin(Teta) / Math.cos(Teta))) / 2;
                if (Math.cos(lambda + alpha) != 0) {
                    beta = Math.acos(Math.cos(Teta) / Math.cos(lambda + alpha));
                }

            }


            System.out.println(alpha + " " + beta + " " + lambda);
            CNOT(controlQubitNumber, toQubitNumber);
        }
    }


    //преобразование CCNOT в CNOT
    public static void CCNOT(int qubit1, int qubit2, int qubit3) {
        //реализация огроменной схемы из H, T, T*, S
        Hadamard(qubit3);
        CNOT(qubit2, qubit3);
        THerm(qubit3);
        CNOT(qubit1, qubit3);
        T(qubit3);
        CNOT(qubit2, qubit3);
        THerm(qubit3);
        CNOT(qubit1, qubit3);
        T(qubit3);
        Hadamard(qubit3);
        THerm(qubit2);
        CNOT(qubit1, qubit2);
        THerm(qubit2);
        CNOT(qubit1, qubit2);
        S(qubit2);
        T(qubit1);
        System.out.println("----------------------------------------------------------------------------------");
    }

    public static void Hadamard(int qubit) {
        //System.out.println("Hadamar");
        commandFromClientList.add(new LogicalAddressingCommandFromClient(CommandTypes.PHASE, Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.QET, Math.PI, new LogicalQubitAddressFromClient(qubit)));
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE, Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        System.out.println("PHASE(PI/2) " + qubit);
        System.out.println("QET(PI/2) " + qubit);
        System.out.println("PHASE(PI/2) " + qubit);

    }

    public static void CNOT(int qubit1, int qubit2) {
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
				CommandTypes.CQET, Math.PI, new LogicalQubitAddressFromClient(qubit1), new LogicalQubitAddressFromClient(qubit2)));
		commandFromClientList.add(new LogicalAddressingCommandFromClient(
				CommandTypes.PHASE, Math.PI / 4, new LogicalQubitAddressFromClient(qubit1)));
        System.out.println("CQET " + qubit1 + " " + qubit2);
        System.out.println("PHASE(PI/2) " + qubit1);
    }

    public static void T(int qubit) {
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE, Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        System.out.println("PHASE(PI/4) " + qubit);
    }

    public static void THerm(int qubit) {
        //T*
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE, -Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        System.out.println("PHASE(-PI/2) " + qubit);
    }

    public static void S(int qubit) {
        //System.out.println("S");
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE, Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        System.out.println("PHASE(PI/2) " + qubit);
    }

}



