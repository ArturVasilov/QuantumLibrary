import hashing.QuantumHashFunction;
import hashing.QuantumHashResult;
import hashing.SingleQubitQuantumHashFunction;
import hashing.SwapTestEqualityFunction;
import qkd.QuantumKeyUser;
import ru.kpfu.arturvasilov.core.computer.InitializationCallback;
import ru.kpfu.arturvasilov.core.computer.QuantumComputer;
import ru.kpfu.arturvasilov.core.computer.QuantumRegister;
import ru.kpfu.arturvasilov.core.universal.UniversalMemoryManager;

/**
 * @author Artur Vasilov
 */
public class CryptographyDemo {

    public static void main(String[] args) {
        QuantumComputer.init(new UniversalMemoryManager(), new InitializationCallback() {
            @Override
            public void onInitializationSucceed() {
                CryptographyDemo.onInitializationSucceed();
            }

            @Override
            public void onInitializationFailed(Throwable error) {
                CryptographyDemo.onInitializationFailed(error);
            }
        });
    }

    private static void onInitializationSucceed() {
        /*System.out.println("Demo for BB84 key distribution protocol\n");

        QuantumKeyUser alice = new QuantumKeyUser("ALICE");
        QuantumKeyUser bob = new QuantumKeyUser("BOB");
        alice.establishConnection(bob);

        System.out.println("Alice key: " + alice.getSecretKey());
        System.out.println("Bob key: " + bob.getSecretKey());*/

        System.out.println("Demo for quantum hashing");

        QuantumHashFunction hashFunction = new SingleQubitQuantumHashFunction(new SwapTestEqualityFunction());
        QuantumHashResult hashResult = hashFunction.hash(0);
        System.out.println(String.valueOf(hashFunction.compareWithHash(hashResult, 4)));
    }

    private static void onInitializationFailed(Throwable error) {
        System.out.println(String.format("Error during initialization: %s", error.getMessage()));
    }

}
