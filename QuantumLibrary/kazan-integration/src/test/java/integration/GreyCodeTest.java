package integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Artur Vasilov
 */
@RunWith(JUnit4.class)
public class GreyCodeTest {

    @Test
    public void generateGreyCodeEqualLength() throws Exception {
        String[] greyCode = new OperationIndices(5, 9, 21).createGreyCode();
        String[] expected = new String[]{
                "01001",
                "01101",
                "00101",
                "10101"
        };
        assertArrayEquals(expected, greyCode);
    }

    @Test
    public void generateGreyCodeDifferentLength() throws Exception {
        String[] greyCode = new OperationIndices(6, 7, 26).createGreyCode();
        String[] expected = new String[]{
                "000111",
                "000110",
                "000010",
                "001010",
                "011010",
        };
        assertArrayEquals(expected, greyCode);
    }

}