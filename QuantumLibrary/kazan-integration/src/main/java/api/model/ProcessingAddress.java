package api.model;

/**
 * @author Artur Vasilov
 */
public class ProcessingAddress {

    private final int processingUnitNumber;
    private final ProcessingUnitCellAddress processingUnitCellAddress;

    public ProcessingAddress(int processingUnitNumber, ProcessingUnitCellAddress processingUnitCellAddress) {
        this.processingUnitNumber = processingUnitNumber;
        this.processingUnitCellAddress = processingUnitCellAddress;
    }

    public ProcessingUnitCellAddress getProcessingUnitCellAddress() {
        return processingUnitCellAddress;
    }

    public int getProcessingUnitNumber() {
        return processingUnitNumber;
    }
}
