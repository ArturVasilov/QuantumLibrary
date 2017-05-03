package memorymanager.controller.execution.results;

import memorymanager.controller.addresses.GlobalQubitAddress;

public class LowLevelResult {

    private GlobalQubitAddress mGlobalQubitAddress;

    private boolean mMeasureResult; // true = 1, false = 0

    public LowLevelResult(GlobalQubitAddress globalQubitAddress, boolean measureResult) {
        this.mGlobalQubitAddress = globalQubitAddress;
        this.mMeasureResult = measureResult;
    }

    public GlobalQubitAddress getGlobalQubitAddress() {
        return mGlobalQubitAddress;
    }

    public boolean isOne() {
        return mMeasureResult;
    }
}
