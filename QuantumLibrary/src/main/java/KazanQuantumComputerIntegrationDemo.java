import integration.CommandsBuilder;
import memorymanager.service_for_controller.commands.LogicalAddressingCommandFromClient;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;

import java.util.List;

/**
 * @author Artur Vasilov
 */
public class KazanQuantumComputerIntegrationDemo {

    public static void main(String[] args) {
        System.out.println("Demo for Kazan integration");

        int[] addresses = new int[]{0, 1, 2};

        ComplexMatrix matrix = new ComplexMatrix(4);
        matrix.setValue(0, 0, new Complex(1, 0));
        matrix.setValue(0, 1, new Complex(1, 0));
        matrix.setValue(0, 2, new Complex(1, 0));
        matrix.setValue(0, 3, new Complex(1, 0));

        matrix.setValue(1, 0, new Complex(1, 0));
        matrix.setValue(1, 1, new Complex(0, 1));
        matrix.setValue(1, 2, new Complex(-1, 0));
        matrix.setValue(1, 3, new Complex(0, -1));

        matrix.setValue(2, 0, new Complex(1, 0));
        matrix.setValue(2, 1, new Complex(-1, 0));
        matrix.setValue(2, 2, new Complex(1, 0));
        matrix.setValue(2, 3, new Complex(-1, 0));

        matrix.setValue(3, 0, new Complex(1, 0));
        matrix.setValue(3, 1, new Complex(0, -1));
        matrix.setValue(3, 2, new Complex(-1, 0));
        matrix.setValue(3, 3, new Complex(0, 1));

        matrix = matrix.multiply(new Complex(0.5, 0));

        System.out.println("Operator matrix:");
        System.out.println(matrix.toString());
        System.out.println();
        System.out.println("Transforming matrix to base of PHASE, QET and CQET:");

        List<LogicalAddressingCommandFromClient> commandsList = new CommandsBuilder(addresses).commandsForOperator(matrix);
        for (LogicalAddressingCommandFromClient command : commandsList) {
            System.out.println(command);
        }
    }
}