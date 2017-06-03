package signature;

import ru.kpfu.arturvasilov.core.computer.QuantumRegister;

import java.util.List;

/**
 * @author Artur Vasilov
 */
public class SignedBitMessage {

    private final int messageBit;

    private final List<Integer> signature;

    public SignedBitMessage(int messageBit, List<Integer> signature) {
        this.messageBit = messageBit;
        this.signature = signature;
    }

    public int getMessageBit() {
        return messageBit;
    }

    public List<Integer> getSignature() {
        return signature;
    }
}