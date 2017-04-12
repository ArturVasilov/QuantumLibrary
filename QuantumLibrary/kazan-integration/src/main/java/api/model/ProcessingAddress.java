package api.model;

/**
 * @author Artur Vasilov
 */
public class ProcessingAddress {

    private int proccessingUnitNumber;
    private ProcessingUnitCellAddress proccessingUnitCellAddress;

    public ProcessingAddress(int proccessingUnitNumber, ProcessingUnitCellAddress proccessingUnitCellAddress) {
        this.proccessingUnitNumber = proccessingUnitNumber;
        this.proccessingUnitCellAddress = proccessingUnitCellAddress;
    }

    public ProcessingUnitCellAddress getProccessingUnitCellAddress() {
        return proccessingUnitCellAddress;
    }

    public int getProccessingUnitNumber() {
        return proccessingUnitNumber;
    }
}
