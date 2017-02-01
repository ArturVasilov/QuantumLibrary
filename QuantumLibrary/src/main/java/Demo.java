import java.math.BigDecimal;

/**
 * @author Artur Vasilov
 */
public class Demo {

    public static void main(String[] args) {
        QuantumBitsSet quantumBitsSet = QuantumBitsSet.createPreparedQubitsSet(1);
        ComplexMatrix operatorS = ComplexMatrix.zeros(2);
        operatorS.setValue(0, 0, new Complex(1, 0));
        operatorS.setValue(1, 1, new Complex(0, 1));
        quantumBitsSet.applyOperator(operatorS);
    }

}
