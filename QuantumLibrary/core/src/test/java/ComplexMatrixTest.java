import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

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
    public void testPauliMatrixXHermitian() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(0, 0));
        matrix.setValue(0, 1, new Complex(1, 0));
        matrix.setValue(1, 0, new Complex(1, 0));
        matrix.setValue(1, 1, new Complex(0, 0));

        assertTrue(matrix.isHermitian());
    }

    @Test
    public void testPauliMatrixYHermitian() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(0, 0));
        matrix.setValue(0, 1, new Complex(0, -1));
        matrix.setValue(1, 0, new Complex(0, 1));
        matrix.setValue(1, 1, new Complex(0, 0));

        assertTrue(matrix.isHermitian());
    }

    @Test
    public void testPauliMatrixZHermitian() throws Exception {
        ComplexMatrix matrix = new ComplexMatrix(2);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(0, 1, new Complex(0, 0));
        matrix.setValue(1, 0, new Complex(0, 0));
        matrix.setValue(1, 1, new Complex(-1, 0));

        assertTrue(matrix.isHermitian());
    }

    @Test
    public void testNonHermitianMatrix() throws Exception {
        ComplexMatrix matrix = ComplexMatrix.identity(3);
        matrix.setValue(1, 0, new Complex(2, 0));

        assertFalse(matrix.isHermitian());
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
         * | 1 1 |
         */
        ComplexMatrix multiplier = ComplexMatrix.identity(2);
        multiplier.setValue(1, 0, new Complex(1, 0));

        ComplexMatrix tensor = matrix.tensorMultiplication(multiplier);

        double[][] array = new double[][]{
                new double[]{5, 0},
                new double[]{5, 5}
        };
        ComplexMatrix expectedMatrix = ComplexMatrix.fromRealArray(array);

        assertEquals(expectedMatrix, tensor);
    }

    @Test
    public void testTensorMultiplication() throws Exception {
        double[][] array = new double[][]{
                new double[]{1, 2},
                new double[]{3, 4}
        };
        ComplexMatrix matrix = ComplexMatrix.fromRealArray(array);

        array = new double[][]{
                new double[]{4, 3},
                new double[]{2, 1}
        };
        ComplexMatrix multiplier = ComplexMatrix.fromRealArray(array);

        ComplexMatrix tensor = matrix.tensorMultiplication(multiplier);

        array = new double[][]{
                new double[]{4, 3, 8, 6},
                new double[]{2, 1, 4, 2},
                new double[]{12, 9, 16, 12},
                new double[]{6, 3, 8, 4},
        };
        ComplexMatrix expectedMatrix = ComplexMatrix.fromRealArray(array);

        assertEquals(expectedMatrix, tensor);
    }

    @Test
    public void testLargeTensorMultiplication() throws Exception {
        double[][] array = new double[][]{
                new double[]{3, 5},
                new double[]{8, 1}
        };
        ComplexMatrix matrix = ComplexMatrix.fromRealArray(array);

        array = new double[][]{
                new double[]{4, 8, 16},
                new double[]{8, 4, 2},
                new double[]{6, 2, 9}
        };
        ComplexMatrix multiplier = ComplexMatrix.fromRealArray(array);

        ComplexMatrix tensor = matrix.tensorMultiplication(multiplier);

        array = new double[][]{
                new double[]{12, 24, 48, 20, 40, 80},
                new double[]{24, 12, 6, 40, 20, 10},
                new double[]{18, 6, 27, 30, 10, 45},
                new double[]{32, 64, 128, 4, 8, 16},
                new double[]{64, 32, 16, 8, 4, 2},
                new double[]{48, 16, 72, 6, 2, 9},
        };
        ComplexMatrix expectedMatrix = ComplexMatrix.fromRealArray(array);

        assertEquals(expectedMatrix, tensor);
    }
}
