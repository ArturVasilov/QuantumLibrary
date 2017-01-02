import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Class for nxn square matrix of complex numbers
 * This is general class for square matrix, but it's widely used
 * as quantum operators representation.
 * That's why size of the matrix is usually a power of two
 * and matrix also should be unitary {@link ComplexMatrix#isUnitary()}
 *
 * @author Artur Vasilov
 */
public class ComplexMatrix {

    public final Complex[][] matrix;

    public ComplexMatrix(int n) {
        matrix = new Complex[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = new Complex[n];
            for (int j = 0; j < n; j++) {
                matrix[i][j] = new Complex();
            }
        }
    }

    /**
     * Creates new matrix with size of n and fills all data with Complex(0, 0)
     * It's useful since many quantum operators have only a small number of non-zero elements.
     *
     * @param n - size of the Matrix
     * @return matrix nxn of complex numbers filled with zeros
     */
    public static ComplexMatrix zeros(int n) {
        ComplexMatrix matrix = new ComplexMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.setValue(i, j, new Complex());
            }
        }
        return matrix;
    }

    /**
     * Creates new identity matrix with size of n.
     * See comments for {@link ComplexMatrix#isIdentityMatrix()}
     *
     * @param n - size of the Matrix
     * @return identity matrix nxn
     */
    public static ComplexMatrix identity(int n) {
        ComplexMatrix matrix = zeros(n);
        for (int i = 0; i < n; i++) {
            matrix.getValue(i, i).setA(BigDecimal.ONE);
        }
        return matrix;
    }

    /**
     * Compares all elements of the matrix with the parameter
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComplexMatrix)) {
            return false;
        }

        ComplexMatrix matrix = (ComplexMatrix) o;

        return Arrays.deepEquals(this.matrix, matrix.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    public Complex getValue(int i, int j) {
        return matrix[i][j];
    }

    public void setValue(int i, int j, Complex complex) {
        matrix[i][j] = complex;
    }

    /**
     * Identity matrix is square nxn matrix with ones on the main diagonal and zeros elsewhere.
     *
     * Identity matrices:
     *
     *           |1 0 0|
     * I(3, 3) = |0 1 0|
     *           |0 0 1|
     *
     *           |1 0 0 . . . 0|
     *           |0 1 0 . . . 0|
     *           |0 0 1 . . . 0|
     * I(n, n) = |. . . . . . .|
     *           |. . . . . . .|
     *           |. . . . . . .|
     *           |0 0 0 . . . 1|
     *
     * @return true iff current matrix is identity
     */
    public boolean isIdentityMatrix() {
        final Complex zero = new Complex(0, 0);
        final Complex one = new Complex(1, 0);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                Complex value = matrix[i][j];
                if (i == j && !value.equals(one)) {
                    return false;
                }
                else if (i != j && !value.equals(zero)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Theory about unitary matrices https://en.wikipedia.org/wiki/Unitary_matrix
     *
     * Unitary matrices play a very important role in quantum mechanics
     * since every quantum operator is unitary matrix (that makes operator invertible)
     *
     * @return true iff current matrix is unitary
     */
    public boolean isUnitary() {
        ComplexMatrix transposed = conjugateTranspose();
        ComplexMatrix result = transposed.multiply(this);
        return result.isIdentityMatrix();
    }

    /**
     * Creates new {@link ComplexMatrix} instance which is result of conjugate transpose matrix
     * Theory of conjugate transpose https://en.wikipedia.org/wiki/Conjugate_transpose
     *
     * In short - it is transposed matrix where all elements are conjugated:
     * A* = ~(A^T)
     *
     * @return new conjugate transposed matrix from current matrix
     */
    public ComplexMatrix conjugateTranspose() {
        int n = matrix.length;
        ComplexMatrix result = new ComplexMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result.setValue(i, j, matrix[j][i].conjugate());
                result.setValue(j, i, matrix[i][j].conjugate());
            }
            result.setValue(i, i, matrix[i][i].conjugate());
        }
        return result;
    }

    /**
     * Creates new {@link ComplexMatrix} instance which is result
     * of multiplication of current matrix with argument matrix
     *
     * Since for quantum algorithms operators' matrices usually have small size,
     * this method uses trivial matrix multiplication algorithm O(n^3).
     *
     * @param multiplier - matrix to multiply. It must have the same size as current matrix.
     *
     * @return multiplication of current matrix and multiplier parameter
     */
    public ComplexMatrix multiply(ComplexMatrix multiplier) {
        int n = matrix.length;
        ComplexMatrix result = new ComplexMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Complex sum = new Complex();
                for (int k = 0; k < n; k++) {
                    sum = sum.add(matrix[i][k].multiply(multiplier.getValue(k, j)));
                }
                result.setValue(i, j, sum);
            }
        }
        return result;
    }

    /**
     * Creates new {@link ComplexMatrix} instance which is result
     * of tensor multiplication of current matrix with argument matrix
     *
     * Tensor multiplication theory https://en.wikipedia.org/wiki/Tensor_product
     *
     * Tensor multiplication of A(nxn) and B(mxm):
     *
     * |a_11 * B, a_12 * B, ..., a_1n * B|
     * |a_21 * B, a_22 * B, ..., a_1n * B|
     * |...      ...      ...  ...    |
     * |a_n1 * B, a_n2 * B, ..., a_nn * B|
     *
     * @param multiplier - matrix to multiply.
     *
     * @return tensor multiplication of current matrix and multiplier parameter
     */
    public ComplexMatrix tensorMultiplication(ComplexMatrix multiplier) {
        int n = matrix.length;
        int m = multiplier.matrix.length;
        ComplexMatrix result = new ComplexMatrix(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Complex element = matrix[i][j];
                for (int k = i * m; k < (i + 1) * m; k++) {
                    for (int l = j * m; l < (j + 1) * m; l++) {
                        result.setValue(k, l, element.multiply(multiplier.getValue(k % m, l % m)));
                    }
                }
            }
        }
        return result;
    }

}
