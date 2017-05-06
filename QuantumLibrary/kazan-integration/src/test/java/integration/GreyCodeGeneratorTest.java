package integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Artur Vasilov
 */
@RunWith(JUnit4.class)
public class GreyCodeGeneratorTest {

    @Test
    public void generateNextGreyCode() throws Exception {
        String[] generateCodes = GreyCodeGenerator.GenerateGreyCodeSequence("01001", "10101", 5);
        System.out.println(Arrays.toString(generateCodes));

        String result = GreyCodeGenerator.GenerateNextGreyCode("01001", "10101", 5);
        assertEquals("01101", result);
    }

}