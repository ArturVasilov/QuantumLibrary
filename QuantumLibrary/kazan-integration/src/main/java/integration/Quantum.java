package integration;

import com.google.gson.GsonBuilder;
import emulator.Complex;
import memorymanager.controller.execution.commands.CommandTypes;
import memorymanager.service_for_controller.addresses.LogicalQubitAddressFromClient;
import memorymanager.service_for_controller.commands.CommandsFromClientDTO;
import memorymanager.service_for_controller.commands.LogicalAddressingCommandFromClient;

import java.util.LinkedList;
import java.util.List;

public class Quantum {

    public static int CountOfQubit;
    public static CommandsFromClientDTO commandsFromClientDTO = new CommandsFromClientDTO();
    public static List<LogicalAddressingCommandFromClient> commandFromClientList = new LinkedList<>();

    public static void BeginQuantum() {
        commandsFromClientDTO = new CommandsFromClientDTO();
        commandFromClientList = new LinkedList<LogicalAddressingCommandFromClient>();
    }

    public static void EndQuantum() {
        commandsFromClientDTO.setLogicalAddressingCommandFromClientList(commandFromClientList);
        //serviceManager.putCommandsToExequtionQueue("1", new GsonBuilder().create().toJson(commandsFromClientDTO));
        String ss = new GsonBuilder().create().toJson(commandsFromClientDTO);
        System.out.println("ЗАВЕРШЕНО!");
        System.out.println("ss=" + ss);
    }

    // единичная комплексная матрица
    public static Complex[][] createI(int size) {
        Complex[][] newMatr = new Complex[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    newMatr[i][j] = new Complex(1, 0);
                } else {
                    newMatr[i][j] = new Complex(0, 0);
                }
            }
        return newMatr;
    }

    //проверка равенств матриц
    public static boolean isEqual(Complex[][] matr1, Complex[][] matr2) {
        boolean bool = true;
        if (matr1.length != matr2.length)
            return false;
        else {
            for (int i = 0; i < matr1.length; i++)
                for (int j = 0; j < matr1.length; j++) {
                    Complex a = new Complex(matr1[i][j].getReal(), matr1[i][j].getImaginary());
                    Complex b = new Complex(matr2[i][j].getReal(), matr2[i][j].getImaginary());
                    if (!(a.getReal() == b.getReal() && a.getImaginary() == b.getImaginary()))
                        bool = false;
                }
            return bool;
        }
    }

    //транспонирование матрицы
    public static Complex[][] Transpose(Complex[][] matr) {// матрица обязательно квадратная
        int size = matr.length;
        Complex[][] newMatr = new Complex[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                newMatr[i][j] = matr[j][i];
        return newMatr;
    }

    //эрмитово сопряжение матрицы
    public static Complex[][] HermitSopr(Complex[][] matr) {
        int size = matr.length;
        Complex[][] result;
        result = Transpose(matr);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result[i][j] = result[i][j].conjugate();
        return result;
    }

    //перемножение комплексных матриц
    public static Complex[][] MultiplyMatr(Complex[][] matr1, Complex[][] matr2) {// n m m l => n l
        int n = matr1.length;
        int m = matr2.length;
        int l = matr2[0].length;
        Complex[][] newMatr = new Complex[n][l];
        for (int z = 0; z < n; z++)
            for (int q = 0; q < l; q++)
                newMatr[z][q] = new Complex(0, 0);

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                int k = 0;
                while (k < m) {
                    Complex c1 = new Complex(matr1[i][k].getReal(), matr1[i][k].getImaginary());
                    Complex c2 = new Complex(matr2[k][j].getReal(), matr2[k][j].getImaginary());
                    Complex c3 = Complex.mult(c1, c2);
                    newMatr[i][j] = Complex.add(newMatr[i][j], c3);
                    k++;
                }
            }
        return newMatr;
    }

    //проверка матрицы на унитарность. H - эрмитово сопряжение матрицы
    public static boolean isUnitary(Complex[][] U) {
        boolean HU;
        boolean UH;
        Complex[][] H = HermitSopr(U);
        Complex[][] I = createI(U.length);
        HU = isEqual(MultiplyMatr(H, U), I);
        UH = isEqual(MultiplyMatr(U, H), I);

        return HU && UH;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void runUntitarCalculation(Complex[][] U, int[] qubits) {
        /*
         * commandsFromClientDTO.setQubitCount(qubits.length());
		 * каждый раз придется менять значение. Пока как - под вопросом
		 *
		 * */
        BeginQuantum();

        if (U.length > 1) {
            CountOfQubit = qubits.length;
            System.out.println("Всего кубит=" + CountOfQubit);
            int size = U.length;
            int step = 1;
            int k;
            Complex[][] result = new Complex[size][size];
            for (int z1 = 0; z1 < size; z1++)
                for (int z2 = 0; z2 < size; z2++)
                    result[z1][z2] = new Complex(U[z1][z2].getReal(), U[z1][z2].getImaginary());
            // скопировали U в result

            while (step < size - 1) {
                k = 1;
                while (k < size - step + 1) {

                    Complex[][] res = createI(size);//U1 или U2 или U3 ...
                    //result - матрица на предыдущем шаге. То есть это - результат перемножения
                    double denomination = Math.sqrt(result[0][0].norma() + result[k][0].norma());//знаменатель

                    res[step - 1][step - 1] = Complex.mult(result[step - 1][step - 1].conjugate(), new Complex(1 / denomination, 0));
                    res[step - 1][step - 1 + k] = Complex.mult(result[step - 1 + k][step - 1].conjugate(), new Complex(1 / denomination, 0));
                    res[step - 1 + k][step - 1] = Complex.mult(result[step - 1 + k][step - 1], new Complex(1 / denomination, 0));
                    res[step - 1 + k][step - 1 + k] = Complex.mult(result[step - 1][step - 1], new Complex(1 / denomination, 0));

                    //System.out.println("step = "+step+", k="+k);//проверка правильности
                /*printMatr(res);*/

                    // матрица U(step) создана
                    //перемножаем матрицы
                    result = MultiplyMatr(res, result);

                    /*System.out.println("result");
                     printMatr(result);*/

                    //нам нужна транспонированная матрица res
                    res = Transpose(res);
                    Complex[][] resTilda = new Complex[2][2];//однокубитный оператор V с волной
                    resTilda[0][0] = res[step - 1][step - 1];
                    resTilda[0][1] = res[step - 1][step - 1 + k];
                    resTilda[1][0] = res[step - 1 + k][step - 1];
                    resTilda[1][1] = res[step - 1 + k][step - 1 + k];

                    int firstIndex = step - 1; // порядковый номер первой строки матрицы res, где есть нетривиальные элементы
                    int secondIndex = step - 1 + k; // порядковый номер второй строки матрицы res, где есть нетривиальные элементы

                    // послед-ть кода Грэя
                    String[] GreyCode = GenerateGreyCodeSequence(Long.toString(firstIndex, 2), Long.toString(secondIndex, 2),
                            qubits.length);

                    /*for(int i=0; i < GreyCode.length && !GreyCode[i].equals("0"); i++)
                     System.out.print(GreyCode[i] + "  ");
                     System.out.println();*/

                    int[] indexes = CalculateIndexesOfQubits(GreyCode);
                    /*for(int i=0; i < indexes.length; i++)
                     System.out.print(indexes[i] + "  ");
                     System.out.println();*/

                    //получили индексы кубитов. к которым нужно применить контролируемый нот оператор
                    unusualCNOTAndTilda(indexes, qubits, resTilda);

                    k++;
                }
                step++;
            }
            //System.out.println("step = "+step+", k="+k);

            Complex[][] res = createI(size);//U последняя
            res[size - 2][size - 2] = result[size - 2][size - 2].conjugate();
            res[size - 2][size - 1] = result[size - 1][size - 2].conjugate();
            res[size - 1][size - 2] = result[size - 2][size - 1].conjugate();
            res[size - 1][size - 1] = result[size - 1][size - 1].conjugate();

            res = Transpose(res);

            int firstIndex = size - 2; // номер первой строки
            int secondIndex = size - 1; // номер второй строки

            //Long.toString(firstIndex,2) - двоичное представление числа firstIndex
            String[] GreyCode = GenerateGreyCodeSequence(Long.toString(firstIndex, 2), Long.toString(secondIndex, 2),
                    qubits.length);
            /*for(int i=0; i < GreyCode.length && !GreyCode[i].equals("0"); i++)
             System.out.print(GreyCode[i] + "  ");
             System.out.println();*/

            int[] indexes = CalculateIndexesOfQubits(GreyCode);
            /*for(int i=0; i < indexes.length; i++)
             System.out.print(indexes[i] + "  ");
             System.out.println();*/

            unusualCNOTAndTilda(indexes, qubits, res);
        }

        EndQuantum();
    }

    //даны 2 числа в двоичном представлении. Пошаговое нахождение кодов Грея
    //NumberOfQubits нужен для определения длины строки
    //находит следующее число
    public static String GenerateNextGreyCode(String str1, String str2, int NumberOfQubits) {
        int i;
        //длины str1 и str2 одинаковы. См GenerateGreyCodeSequence
        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();

        boolean bool = false;
        i = NumberOfQubits - 1;//индекс последнего элемента матрицы
        while (i > -1 && !bool) {
            if (c1[i] != c2[i]) {
                c1[i] = c2[i];
                bool = true;
            }
            i--;
        }
        return String.valueOf(c1);

    }


    //генерация последовательности кода Грэя
    //кол-во кубит нужно для определния длины строки - представления числа в двоичной системе счисления
    // пот что 0 м.б. как 00, так и 000 и т.д.
    public static String[] GenerateGreyCodeSequence(String str1, String str2, int NumberOfQubits) {
        String[] result = new String[NumberOfQubits + 1]; // см стр 244 (Нильсен-Чанг). m <= n +1
        int i = 1;
        //результат. Получим эту матрицу. Заполняем нулями
        for (; i < result.length; i++)
            result[i] = "0";
        //относится к str1.
        //если str1="11", NumberOfqubits=5, to c='0','0','0','1','1'
        char[] c = new char[NumberOfQubits];
        for (i = 0; i < c.length; i++) {
            if (i < c.length - str1.length())
                c[i] = '0';
            else
                c[i] = str1.toCharArray()[i - (c.length - str1.length())];
        }
        //записываем в result
        result[0] = String.valueOf(c);//типа как toString();
        //как c1
        char[] c2 = new char[NumberOfQubits];
        for (i = 0; i < c2.length; i++) {
            if (i < c2.length - str2.length())
                c2[i] = '0';
            else
                c2[i] = str2.toCharArray()[i - (c2.length - str2.length())];
        }
        String str2New = String.valueOf(c2);
        /*
         теперь длины str1 и str2 одинаковы
         т.е. они уже result[i] и str2New
         */

        i = 0;
        while (!result[i].equals(str2New)) {
            result[i + 1] = GenerateNextGreyCode(result[i], str2New, NumberOfQubits);
            i++;
        }
        //i увеличили. Последний элемент то, что хотели получить
        result[i] = str2New;
        return result;
    }


    //получаем номера кубитов, к которым нужно применить ту или иную операцию
    public static int[] CalculateIndexesOfQubits(String[] GreyCode) {
        int i = 0;
        int count = 0;
        for (; i < GreyCode.length; i++) {
            if (!GreyCode[i].equals("0"))
                count++;
        }
        //автоматически зап-т нулями
        int[] indexes = new int[count - 1]; // например, если 4 строки, то 3 преобразовния
        i = 0;
        for (; i <= GreyCode.length - 2 && !GreyCode[i + 1].equals("0"); i++) {
            //for(; i < count; i++){ - нельзя. Глючит на indexes[i] = j+1; у GreyCode[] больше стр
            //находим номер кубита, к которому применяется многократный оператор C...CNot
            for (int j = 0; j < GreyCode[i].length(); j++) {
                if (GreyCode[i].charAt(j) != GreyCode[i + 1].charAt(j)) // различаются ровно в 1 элементе
                    indexes[i] = j + 1;
            }
        }
        return indexes;
    }


    public static void unusualCNOTAndTilda(int[] indexes, int[] qubits, Complex[][] resTilda) {
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
                if (indexes[i] == 1)
                    CNOT(2, 1);
                else
                    CNOT(1, 2);
            }
            if (indexes[indexes.length - 1] == 1)
                decompositionTildaOperator(2, 1, resTilda);
            else
                decompositionTildaOperator(1, 2, resTilda);

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
    public static void decompositionTildaOperator(int controlQubitNumber, int toQubitNumber, Complex[][] resTilda) {
        Complex det1 = Complex.mult(resTilda[0][0], resTilda[1][1]);
        Complex det2 = Complex.mult(resTilda[0][1], resTilda[1][0]);
        Complex det = Complex.sub(det1, det2); // определитель матрицы

        double gamma = Math.acos(det.getReal()) / 2;

        Complex A = Complex.mult(resTilda[0][0], new Complex(Math.cos(gamma), Math.sin(gamma)));
        Complex B = Complex.mult(resTilda[0][1], new Complex(Math.cos(gamma), Math.sin(gamma)));

        double Teta = 2 * Math.acos(A.getReal());

        if (Teta != 0.0) {
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

			/*commandFromClientList.add(new LogicalAddressingCommandFromClient(
                    new CommandTypes("PHASE"), new Double((lambda-alpha)/2), new LogicalQubitAddressFromClient(toQubitNumber)));
			*/
            CNOT(controlQubitNumber, toQubitNumber);

			/*commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("PHASE"), new Double(-Math.PI/2), new LogicalQubitAddressFromClient(toQubitNumber)));
			commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("QET"), new Double(beta/2), new LogicalQubitAddressFromClient(toQubitNumber)));
			commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("PHASE"), new Double(Math.PI/2), new LogicalQubitAddressFromClient(toQubitNumber)));
			commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("PHASE"), new Double(-(lambda+alpha)/2), new LogicalQubitAddressFromClient(toQubitNumber)));

			CNOT(controlQubitNumber, toQubitNumber);

			commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("PHASE"), new Double(alpha), new LogicalQubitAddressFromClient(toQubitNumber)));
			commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("PHASE"), new Double(-Math.PI/2), new LogicalQubitAddressFromClient(toQubitNumber)));
			commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("QET"), new Double(beta/2), new LogicalQubitAddressFromClient(toQubitNumber)));
			commandFromClientList.add(new LogicalAddressingCommandFromClient(
					new CommandTypes("PHASE"), new Double(Math.PI/2), new LogicalQubitAddressFromClient(toQubitNumber)));
		*/
        } else {
            //////System.out.println("Teta="+Teta+", остальное не вычисляем. Тождественное преобразование!!!");
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
		/*commandFromClientList.add(new LogicalAddressingCommandFromClient(
				new CommandTypes("CQET"), new Double(Math.PI), new LogicalQubitAddressFromClient(qubit1), new LogicalQubitAddressFromClient(qubit2)));
		commandFromClientList.add(new LogicalAddressingCommandFromClient(
				new CommandTypes("PHASE"), new Double(Math.PI/4), new LogicalQubitAddressFromClient(qubit1)));*/
        System.out.println("CQET " + qubit1 + " " + qubit2);
        System.out.println("PHASE(PI/2) " + qubit1);
    }

    public static void T(int qubit) {
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE, Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        //System.out.println("PHASE(PI/4) " + qubit);
    }

    public static void THerm(int qubit) {
        //T*
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE, -Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        //System.out.println("PHASE(-PI/4) " + qubit);
    }

    public static void S(int qubit) {
        //System.out.println("S");
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE, Math.PI / 2, new LogicalQubitAddressFromClient(qubit)));
        //System.out.println("PHASE(PI/2) " + qubit);
    }

}



