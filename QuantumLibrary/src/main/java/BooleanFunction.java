/**
 * @author Artur Vasilov
 */
public abstract class BooleanFunction {

    private final int size;

    public BooleanFunction(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    /**
     * Must represent result of boolean function of {@link BooleanFunction#size} arguments
     *
     * @param arguments - set of boolean arguments to the function
     */
    public boolean call(boolean[] arguments) {
        if (arguments.length != size) {
            String message = String.format("Arguments size must be the same as function size: " +
                    "function size = %d, arguments size = %d", size, arguments.length);
            throw new IllegalArgumentException(message);
        }
        return actualCall(arguments);
    }

    protected abstract boolean actualCall(boolean[] arguments);

    /**
     * Increment argument number represented by boolean array
     *
     * @return true if number was increased, false when we maximum length is achieved
     */
    public static boolean next(boolean[] argumentsSet) {
        boolean isMaximum = true;
        for (boolean value : argumentsSet) {
            if (!value) {
                isMaximum = false;
                break;
            }
        }
        if (isMaximum) {
            return false;
        }

        for (int i = argumentsSet.length - 1; i >= 0; i--) {
            if (!argumentsSet[i]) {
                argumentsSet[i] = true;
                break;
            } else {
                argumentsSet[i] = false;
            }
        }
        return true;
    }

    public static String arrayToString(boolean[] array) {
        StringBuilder builder = new StringBuilder();
        for (boolean value : array) {
            builder.append(value ? '1' : '0');
        }
        return builder.toString();
    }
}
