package qkd;

import ru.kpfu.arturvasilov.core.BooleanFunction;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.Operators;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.security.SecureRandom;
import java.util.*;

/**
 * @author Artur Vasilov
 */
public class QuantumKeyUser {

    private static final int QUBITS_COUNT = 8;

    private final Random random = new SecureRandom();

    private final String userId;

    private int secretKey;

    private boolean[] currentRegisterValues;
    private boolean[] basis;

    public QuantumKeyUser(String userId) {
        this.userId = userId;
    }

    public void establishConnection(QuantumKeyUser user) {
        clearKeyValues();

        StringBuilder initialState = new StringBuilder();
        for (int i = 0; i < QUBITS_COUNT; i++) {
            if (random.nextBoolean()) {
                currentRegisterValues[i] = true;
                initialState.append("1");
            } else {
                currentRegisterValues[i] = false;
                initialState.append("0");
            }
        }
        QuantumRegister register = QuantumComputer.createNewRegister(initialState.toString());

        ComplexMatrix changeBasisOperation = ComplexMatrix.identity(1);
        for (int i = 0; i < QUBITS_COUNT; i++) {
            if (random.nextBoolean()) {
                basis[i] = true;
                changeBasisOperation = changeBasisOperation.tensorMultiplication(Operators.hadamar());
            } else {
                basis[i] = false;
                changeBasisOperation = changeBasisOperation.tensorMultiplication(ComplexMatrix.identity(2));
            }
        }
        register.apply(changeBasisOperation);

        user.acceptAndMeasureRegister(register, this);
    }

    private void acceptAndMeasureRegister(QuantumRegister register, QuantumKeyUser fromUser) {
        clearKeyValues();

        ComplexMatrix changeBasisOperation = ComplexMatrix.identity(1);
        for (int i = 0; i < QUBITS_COUNT; i++) {
            if (random.nextBoolean()) {
                basis[i] = true;
                changeBasisOperation = changeBasisOperation.tensorMultiplication(Operators.hadamar().conjugateTranspose());
            } else {
                basis[i] = false;
                changeBasisOperation = changeBasisOperation.tensorMultiplication(ComplexMatrix.identity(2));
            }
        }
        register.apply(changeBasisOperation);

        currentRegisterValues = register.measure();

        QuantumComputer.destroyRegister(register.getId());

        compareBasisAndCalculateSecretKey(fromUser, this);
    }

    private void compareBasisAndCalculateSecretKey(QuantumKeyUser alice, QuantumKeyUser bob) {
        List<Boolean> result = new ArrayList<>();
        for (int i = 0; i < QUBITS_COUNT; i++) {
            if (alice.basis[i] = bob.basis[i]) {
                result.add(alice.currentRegisterValues[i]);
            }
        }

        boolean[] secretKeyBinary = new boolean[result.size()];
        for (int i = 0; i < result.size(); i++) {
            secretKeyBinary[i] = result.get(i);
        }
        int secretKey = BooleanFunction.arrayToInt(secretKeyBinary);

        alice.clearKeyValues();
        bob.clearKeyValues();

        alice.secretKey = secretKey;
        bob.secretKey = secretKey;
    }

    private void clearKeyValues() {
        currentRegisterValues = new boolean[QUBITS_COUNT];
        basis = new boolean[QUBITS_COUNT];
        secretKey = -1;
    }

    public String getUserId() {
        return userId;
    }

    public int getSecretKey() {
        return secretKey;
    }
}
