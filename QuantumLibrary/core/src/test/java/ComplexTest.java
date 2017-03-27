import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.kpfu.arturvasilov.core.Complex;

import static org.junit.Assert.assertEquals;

/**
 * @author Artur Vasilov
 */
@RunWith(JUnit4.class)
public class ComplexTest {

    @Test
    public void testAdd1() throws Exception {
        Complex first = new Complex(1, -1);
        Complex second = new Complex(2, 1);
        assertEquals(new Complex(3, 0), first.add(second));
    }

    @Test
    public void testAdd2() throws Exception {
        Complex first = new Complex(33, 74);
        Complex second = new Complex(-87, 16);
        assertEquals(new Complex(-54, 90), first.add(second));
    }

    @Test
    public void testMultiply1() throws Exception {
        Complex first = new Complex(3, 4);
        Complex second = new Complex(5, 1);
        assertEquals(new Complex(11, 23), first.multiply(second));
    }

    @Test
    public void testMultiply2() throws Exception {
        Complex first = new Complex(-10, 2);
        Complex second = new Complex(0, 6);
        assertEquals(new Complex(-12, -60), first.multiply(second));
    }
}