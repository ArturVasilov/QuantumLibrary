package signature;

import hashing.QuantumHashFunction;
import hashing.QuantumHashResult;
import org.apache.commons.math3.util.Pair;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author Artur Vasilov
 */
public class QuantumSignatureUser {

    private static final int SIGNATURE_QUBITS_COUNT = 8;

    private static final int MAXIMUM_RANDOM_NUMBER = 2 << SIGNATURE_QUBITS_COUNT;

    private final QuantumHashFunction hashFunction;

    private final Random random;

    private final List<Pair<Integer, Integer>> privateKey;

    private final List<Pair<QuantumHashResult, QuantumHashResult>> publicKey;

    public QuantumSignatureUser(QuantumHashFunction hashFunction) {
        this.hashFunction = hashFunction;
        random = new SecureRandom();
        privateKey = new ArrayList<>();
        publicKey = new ArrayList<>();
    }

    public void generateKey() {
        discardKeys();

        for (int i = 0; i < SIGNATURE_QUBITS_COUNT; i++) {
            int zeroKey = random.nextInt(MAXIMUM_RANDOM_NUMBER);
            int oneKey = random.nextInt(MAXIMUM_RANDOM_NUMBER);
            privateKey.add(new Pair<>(zeroKey, oneKey));

            QuantumHashResult zeroHash = hashFunction.hash(zeroKey);
            QuantumHashResult oneHash = hashFunction.hash(oneKey);
            publicKey.add(new Pair<>(zeroHash, oneHash));
        }
    }

    public SignedBitMessage createMessage(int bit) {
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Using this scheme you can only sign a single bit (0 or 1)");
        }

        List<Integer> signature = new ArrayList<>();
        for (int i = 0; i < SIGNATURE_QUBITS_COUNT; i++) {
            if (bit == 0) {
                signature.add(privateKey.get(i).getFirst());
            } else {
                signature.add(privateKey.get(i).getSecond());
            }
        }
        return new SignedBitMessage(bit, signature);
    }

    public boolean verify(SignedBitMessage signedBitMessage, QuantumSignatureUser messageOwner) {
        int bit = signedBitMessage.getMessageBit();
        List<Integer> signature = signedBitMessage.getSignature();
        for (int i = 0; i < signature.size(); i++) {
            QuantumHashResult publicKeyHash = bit == 0
                    ? messageOwner.publicKey.get(i).getFirst()
                    : messageOwner.publicKey.get(i).getSecond();
            boolean equalQubit = hashFunction.compareWithHash(publicKeyHash, signature.get(i));
            if (!equalQubit) {
                return false;
            }
        }
        return true;
    }

    public void discardKeys() {
        privateKey.clear();
        publicKey.stream().map(pair -> {
            List<QuantumRegister> registers = new ArrayList<>();
            registers.addAll(pair.getFirst().getHashRegisters());
            registers.addAll(pair.getSecond().getHashRegisters());
            return registers;
        })
                .flatMap(Collection::stream)
                .forEach(register -> QuantumComputer.destroyRegister(register.getId()));
        publicKey.clear();
    }
}