package integration;

/**
 * @author Artur Vasilov
 */
public class GreyCodeGenerator {

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

}
