package emulator;

/**
 * @author Artur Vasilov
 */
public class QuantumSchemeStepQubitAttributes {

    public static String IdentityGateID = "IdentityGateID";
    public String gateID;

    /*
    * This equals to qubit is "upper than other"
    * */
    boolean control;

    public QuantumSchemeStepQubitAttributes(String gateID, boolean control) {
        this.gateID = gateID;
        this.control = control;
    }

    public QuantumSchemeStepQubitAttributes() {
        this.gateID = IdentityGateID;
        this.control = false;
    }
}
