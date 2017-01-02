import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Artur Vasilov
 */
@RunWith(JUnit4.class)
public class ComplexMatrixTest {

    @Test
    public void testIdentityMatrix() throws Exception {
        ComplexMatrix matrix = ComplexMatrix.identity(2);
        assertTrue(matrix.isIdentityMatrix());
    }

    @Test
    public void testMatrixNotIdentity() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(0, 1, new Complex(1, 0));
        matrix.setValue(1, 0, new Complex(0, 0));
        matrix.setValue(1, 1, new Complex(1, 0));

        assertFalse(matrix.isIdentityMatrix());
    }

    @Test
    public void testMatrixNotIdentityImaginaryPart() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(1, -0.05));
        matrix.setValue(0, 1, new Complex(0, 0));
        matrix.setValue(1, 0, new Complex(0, 0));
        matrix.setValue(1, 1, new Complex(1, 0));

        assertFalse(matrix.isIdentityMatrix());
    }

    @Test
    public void testLargeMatrixIdentity() throws Exception {
        ComplexMatrix matrix = ComplexMatrix.identity(16);
        assertTrue(matrix.isIdentityMatrix());
    }

    @Test
    public void testConjugateTranspose() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(1, -5));
        matrix.setValue(0, 1, new Complex(2, 3));
        matrix.setValue(1, 0, new Complex(8, -1));
        matrix.setValue(1, 1, new Complex(-19, 4));

        ComplexMatrix result = new ComplexMatrix(2);
        result.setValue(0, 0, new Complex(1, 5));
        result.setValue(0, 1, new Complex(8, 1));
        result.setValue(1, 0, new Complex(2, -3));
        result.setValue(1, 1, new Complex(-19, -4));

        assertEquals(result, matrix.conjugateTranspose());
    }

    @Test
    public void testIdentityMatrixUnitary() throws Exception {
        ComplexMatrix matrix = ComplexMatrix.identity(2);
        assertTrue(matrix.isUnitary());
    }

    @Test
    public void testNonUnitaryMatrix() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(0, 1, new Complex(0, 0));
        matrix.setValue(1, 0, new Complex(1, 0));
        matrix.setValue(1, 1, new Complex(1, 0));

        assertFalse(matrix.isUnitary());
    }

    @Test
    public void testPauliMatrixXUnitary() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(0, 0));
        matrix.setValue(0, 1, new Complex(1, 0));
        matrix.setValue(1, 0, new Complex(1, 0));
        matrix.setValue(1, 1, new Complex(0, 0));

        assertTrue(matrix.isUnitary());
    }

    @Test
    public void testPauliMatrixYUnitary() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(0, 0));
        matrix.setValue(0, 1, new Complex(0, -1));
        matrix.setValue(1, 0, new Complex(0, 1));
        matrix.setValue(1, 1, new Complex(0, 0));

        assertTrue(matrix.isUnitary());
    }

    @Test
    public void testPauliMatrixZUnitary() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(0, 1, new Complex(0, 0));
        matrix.setValue(1, 0, new Complex(0, 0));
        matrix.setValue(1, 1, new Complex(-1, 0));

        assertTrue(matrix.isUnitary());
    }

    @Test
    public void testToffoliUnitary() throws Exception {
        ComplexMatrix matrix = ComplexMatrix.zeros(8);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(1, 1, new Complex(1, 0));
        matrix.setValue(2, 2, new Complex(1, 0));
        matrix.setValue(3, 3, new Complex(1, 0));
        matrix.setValue(4, 4, new Complex(1, 0));
        matrix.setValue(5, 5, new Complex(1, 0));
        matrix.setValue(6, 7, new Complex(1, 0));
        matrix.setValue(7, 6, new Complex(1, 0));

        assertTrue(matrix.isUnitary());
    }

    @Test
    public void testSimpleTensor() throws Exception {
        //number 5
        ComplexMatrix matrix = ComplexMatrix.identity(1);
        matrix.setValue(0, 0, new Complex(5, 0));

        /*
         * Matrix:
         *
         * | 1 0 |
         * | 0 1 |
         */
        ComplexMatrix multiplier = ComplexMatrix.identity(2);
        multiplier.setValue(1, 0, new Complex(1, 0));

        ComplexMatrix tensor = matrix.tensorMultiplication(multiplier);

        ComplexMatrix expectedMatrix = new ComplexMatrix(2);
        expectedMatrix.setValue(0, 0, new Complex(5, 0));
        expectedMatrix.setValue(0, 1, new Complex(0, 0));
        expectedMatrix.setValue(1, 0, new Complex(5, 0));
        expectedMatrix.setValue(1, 1, new Complex(5, 0));

        assertEquals(expectedMatrix, tensor);
    }

    @Test
    public void testTensorMultiplication() throws Exception {
        /*
         * Matrix:
         *
         * | 1 2 |
         * | 3 4 |
         */
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(0, 1, new Complex(2, 0));
        matrix.setValue(1, 0, new Complex(3, 0));
        matrix.setValue(1, 1, new Complex(4, 0));

        /*
         * Multiplier matrix:
         *
         * | 4 3 |
         * | 2 1 |
         */
        ComplexMatrix multiplier = new ComplexMatrix(2);
        multiplier.setValue(0, 0, new Complex(4, 0));
        multiplier.setValue(0, 1, new Complex(3, 0));
        multiplier.setValue(1, 0, new Complex(2, 0));
        multiplier.setValue(1, 1, new Complex(1, 0));

        ComplexMatrix tensor = matrix.tensorMultiplication(multiplier);

        /*
         * Expected result:
         *
         * | 4  3  8  6  |
         * | 2  1  4  2  |
         * | 12 9  16 12 |
         * | 6  3  8  4  |
         */
        ComplexMatrix expectedMatrix = new ComplexMatrix(4);
        expectedMatrix.setValue(0, 0, new Complex(4, 0));
        expectedMatrix.setValue(0, 1, new Complex(3, 0));
        expectedMatrix.setValue(0, 2, new Complex(8, 0));
        expectedMatrix.setValue(0, 3, new Complex(6, 0));

        expectedMatrix.setValue(1, 0, new Complex(2, 0));
        expectedMatrix.setValue(1, 1, new Complex(1, 0));
        expectedMatrix.setValue(1, 2, new Complex(4, 0));
        expectedMatrix.setValue(1, 3, new Complex(2, 0));

        expectedMatrix.setValue(2, 0, new Complex(12, 0));
        expectedMatrix.setValue(2, 1, new Complex(9, 0));
        expectedMatrix.setValue(2, 2, new Complex(16, 0));
        expectedMatrix.setValue(2, 3, new Complex(12, 0));

        expectedMatrix.setValue(3, 0, new Complex(6, 0));
        expectedMatrix.setValue(3, 1, new Complex(3, 0));
        expectedMatrix.setValue(3, 2, new Complex(8, 0));
        expectedMatrix.setValue(3, 3, new Complex(4, 0));

        assertEquals(expectedMatrix, tensor);
    }
}
