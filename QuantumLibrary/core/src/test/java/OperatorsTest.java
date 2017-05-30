import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.kpfu.arturvasilov.core.Operators;

import static org.junit.Assert.assertTrue;

/**
 * @author Artur Vasilov
 */
@RunWith(JUnit4.class)
public class OperatorsTest {

    @Test
    public void testFredkinOperatorUnitary() throws Exception {
        assertTrue(Operators.fredkin().isUnitary());
    }
}
