package memorymanager.controller.execution.results;

import memorymanager.controller.addresses.MemoryStateKeeper;
import memorymanager.service_for_controller.OwnerData;
import memorymanager.service_for_controller.addresses.LogicalQubitAddressForController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Artur Vasilov
 */
public class FinalResults {

    //Map for retrieving OwnerData using LogicalQubitAddressForController (for return results)
    private Map<LogicalQubitAddressForController, OwnerData> logicalQubitAddressOwnerDataMap;

    private MemoryStateKeeper mMemoryStateKeeper;


    public FinalResults(MemoryStateKeeper memoryStateKeeper) {
        mMemoryStateKeeper = memoryStateKeeper;
        logicalQubitAddressOwnerDataMap = new HashMap<>();
    }

    public boolean putOwnerData(LogicalQubitAddressForController logicalQubitAddressForController, OwnerData ownerData) {
        if (!logicalQubitAddressOwnerDataMap.containsKey(logicalQubitAddressForController)) {
            logicalQubitAddressOwnerDataMap.put(logicalQubitAddressForController, ownerData);
            return true;
        }
        return false;
    }

    public Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> collectFinalResults(List<LowLevelResult> lowLevelResults) {
        Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> finalResults = new HashMap<>();
        for (LowLevelResult lowLevelResult : lowLevelResults) {
            LogicalQubitAddressForController logicalQubitAddress = mMemoryStateKeeper.getLogicalQubitAddressByPhysical(lowLevelResult.getGlobalQubitAddress());

            OwnerData ownerData = logicalQubitAddressOwnerDataMap.get(logicalQubitAddress);
            if (!finalResults.containsKey(ownerData)) {
                Map<LogicalQubitAddressForController, Boolean> measureResultMap = new HashMap<LogicalQubitAddressForController, Boolean>();
                measureResultMap.put(logicalQubitAddress, lowLevelResult.isOne());
                finalResults.put(ownerData, measureResultMap);
            } else {
                finalResults.get(ownerData).put(logicalQubitAddress, lowLevelResult.isOne());
            }
        }
        logicalQubitAddressOwnerDataMap.clear();
        mMemoryStateKeeper.clearMemoryData();
        return finalResults;
    }
}
