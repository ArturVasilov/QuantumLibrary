package integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class OperationIndices {

    private final int length;
    private final String fromCode;
    private final String toCode;

    public OperationIndices(int length, int firstIndex, int secondIndex) {
        this.length = length;

        String fromCode = Integer.toString(firstIndex, 2);
        this.fromCode = String.join("", Collections.nCopies(length - fromCode.length(), "0")) + fromCode;

        String toCode = Integer.toString(secondIndex, 2);
        this.toCode = String.join("", Collections.nCopies(length - toCode.length(), "0")) + toCode;
    }

    public int[] calculateIndexesOfQubits() {
        String[] greyCode = createGreyCode();
        int[] result = new int[greyCode.length - 1];
        Arrays.fill(result, 0);
        for (int i = 0; i < greyCode.length - 1; i++) {
            for (int j = 0; j < greyCode[i].length(); j++) {
                if (greyCode[i].charAt(j) != greyCode[i + 1].charAt(j)) {
                    result[i] = j + 1;
                }
            }
        }
        return result;
    }

    String[] createGreyCode() {
        List<String> result = new ArrayList<>();
        String currentCode = String.valueOf(fromCode.toCharArray());
        result.add(currentCode);
        for (int i = length - 1; i >= 0; i--) {
            if (currentCode.charAt(i) != toCode.charAt(i)) {
                char[] code = currentCode.toCharArray();
                code[i] = toCode.charAt(i);
                currentCode = String.valueOf(code);
                result.add(currentCode);
            }
        }
        return result.toArray(new String[result.size()]);
    }
}