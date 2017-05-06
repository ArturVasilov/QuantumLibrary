package integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Artur Vasilov
 */
public class GreyCode {

    private final int length;
    private final String fromCode;
    private final String toCode;

    public GreyCode(int length, String fromCode, String toCode) {
        this.length = length;
        this.fromCode = String.join("", Collections.nCopies(length - fromCode.length(), "0")) + fromCode;
        this.toCode = String.join("", Collections.nCopies(length - toCode.length(), "0")) + toCode;
    }

    public String[] createGreyCode() {
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
