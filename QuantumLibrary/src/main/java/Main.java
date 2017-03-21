import java.util.Arrays;

/**
 * @author Artur Vasilov
 */
public class Main {

    public static void main(String[] args) {
        BooleanFunction xFunction = new BooleanFunction(1) {
            @Override
            protected boolean actualCall(boolean[] arguments) {
                return arguments[0];
            }
        };
        System.out.println("x is balanced? " + isBalanced(xFunction));
        System.out.println("x is probably constant? " + isProbablyConstant(xFunction, 14));

        BooleanFunction zeroFunction = new BooleanFunction(30) {
            @Override
            protected boolean actualCall(boolean[] arguments) {
                return false;
            }
        };
        System.out.println("0 is balanced? " + isBalanced(zeroFunction));
        System.out.println("0 is probably constant? " + isProbablyConstant(zeroFunction, 15));

        BooleanFunction xorFunction = new BooleanFunction(2) {
            @Override
            protected boolean actualCall(boolean[] arguments) {
                return arguments[0] ^ arguments[1];
            }
        };
        System.out.println("x + y is balanced? " + isBalanced(xorFunction));
        System.out.println("x + y is probably constant? " + isProbablyConstant(xorFunction, 10));

        BooleanFunction maxCountFunction = new BooleanFunction(3) {
            @Override
            protected boolean actualCall(boolean[] arguments) {
                return (arguments[0] && arguments[1])
                        || (arguments[0] && arguments[2])
                        || (arguments[1] && arguments[2]);
            }
        };
        System.out.println("maxcount(x, y, z) is balanced? " + isBalanced(maxCountFunction));
        System.out.println("maxcount(x, y, z) is probably constant? " + isProbablyConstant(maxCountFunction, 30));
    }

    private static boolean isBalanced(BooleanFunction function) {
        int size = function.getSize();
        boolean[] arguments = new boolean[size];
        Arrays.fill(arguments, false);
        boolean first = function.call(arguments);

        for (int i = 0; i < Math.pow(2, size); i++) {
            if (!BooleanFunction.next(arguments)) {
                return false;
            }
            if (first != function.call(arguments)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isProbablyConstant(BooleanFunction function, int iterations) {
        int size = function.getSize();
        boolean[] arguments = new boolean[size];
        Arrays.fill(arguments, false);
        boolean first = function.call(arguments);

        for (int i = 0; i < Math.pow(2, size) && i < iterations; i++) {
            if (!BooleanFunction.next(arguments)) {
                return true;
            }
            if (first != function.call(arguments)) {
                return false;
            }
        }
        return true;
    }

}
