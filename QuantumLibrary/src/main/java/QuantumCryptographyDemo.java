import hashing.QuantumHashFunction;
import hashing.QuantumHashResult;
import hashing.multiple.SingleQubitRegistersQuantumHashFunction;
import hashing.tests.QuantumHashesEqualityTestFunction;
import hashing.tests.SwapTestEqualityFunction;
import hashing.tests.UniversalTestFunction;
import qkd.QuantumKeyUser;
import ru.kpfu.arturvasilov.core.computer.InitializationCallback;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.universal.UniversalMemoryManager;
import signature.QuantumSignatureUser;
import signature.SignedBitMessage;

/**
 * @author Artur Vasilov
 */
public class QuantumCryptographyDemo {

    public static void main(String[] args) {
        QuantumComputer.init(new UniversalMemoryManager(), new InitializationCallback() {
            @Override
            public void onInitializationSucceed() {
                System.out.println("Demo for quantum cryptography protocols");
                System.out.println();
                quantumKeyDistributionProtocolDemo();
                System.out.println();
                quantumHashingDemo();
                System.out.println();
                quantumDigitalSignatureDemo();
            }

            @Override
            public void onInitializationFailed(Throwable error) {
                QuantumCryptographyDemo.onInitializationFailed(error);
            }
        });
    }

    private static void quantumKeyDistributionProtocolDemo() {
        System.out.println("Demo for BB84 key distribution protocol:");

        QuantumKeyUser alice = new QuantumKeyUser("ALICE");
        QuantumKeyUser bob = new QuantumKeyUser("BOB");
        alice.establishConnection(bob);

        System.out.println("Alice key: " + alice.getSecretKey());
        System.out.println("Bob key: " + bob.getSecretKey());
    }

    private static void quantumHashingDemo() {
        System.out.println("Demo for quantum hashing:");

        int value = 20;
        int errorHash = 30;

        System.out.println("Real value = " + value);

        QuantumHashFunction hashFunction = new SingleQubitRegistersQuantumHashFunction(new SwapTestEqualityFunction());
        QuantumHashResult hashResult = hashFunction.hash(value);

        System.out.println("Testing for incorrect value (e.g. " + errorHash + "):");
        System.out.println(hashFunction.compareWithHash(hashResult, errorHash));

        System.out.println("Testing for correct value:");
        System.out.println(hashFunction.compareWithHash(hashResult, value));
    }

    private static void quantumDigitalSignatureDemo() {
        System.out.println("Demo for quantum digital signature algorithm:");

        QuantumHashesEqualityTestFunction testFunction = new UniversalTestFunction();
        QuantumHashFunction hashFunction = new SingleQubitRegistersQuantumHashFunction(testFunction);
        QuantumSignatureUser alice = new QuantumSignatureUser(hashFunction);
        QuantumSignatureUser bob = new QuantumSignatureUser(hashFunction);

        alice.generateKey();
        SignedBitMessage aliceMessage = alice.createMessage(1);
        boolean isFromAlice = bob.verify(aliceMessage, alice);

        if (isFromAlice) {
            System.out.println("Message is from Alice");
        } else {
            System.out.println("Error during testing signatures");
        }

        alice.discardKeys();
    }

    private static void onInitializationFailed(Throwable error) {
        System.out.println(String.format("Error during initialization: %s", error.getMessage()));
    }
}