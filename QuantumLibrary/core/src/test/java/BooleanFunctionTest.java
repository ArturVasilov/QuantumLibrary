import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.kpfu.arturvasilov.core.BooleanFunction;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Artur Vasilov
 */
@RunWith(JUnit4.class)
public class BooleanFunctionTest {

    @Test
    public void testNumberToBinary() throws Exception {
        int number = 46;
        boolean[] expectedResult = {true, false, true, true, true, false}; //101110 = 46
        assertArrayEquals(expectedResult, BooleanFunction.binaryRepresentation(number));
     }
}
