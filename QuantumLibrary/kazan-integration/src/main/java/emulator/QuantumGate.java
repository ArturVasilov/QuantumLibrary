package emulator;

/**
 * @author Artur Vasilov
 */
public abstract class QuantumGate {

    protected int qubitsNumber;
    protected int size;

    //Gate matrices
    public static Complex[][] identityGateMatrix() {
        Complex result[][] = {
                {Complex.unit(), Complex.zero()},
                {Complex.zero(), Complex.unit()}
        };
        return result;
    }

    public static Complex[][] swapGateMatrix() {
        Complex result[][] = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.unit(), Complex.zero()},
                {Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit()}
        };
        return result;
    }

    @Override
    public String toString() {
        Complex[][] matrix = new Complex[0][];
        try {
            matrix = this.getMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result = result + matrix[i][j] + " ";
            }
            result = result + "\n";
        }
        return result;
    }

    public abstract Complex[][] getMatrix() throws Exception;
}
