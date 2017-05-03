package memorymanager.controller.execution.commands;

import com.google.gson.annotations.SerializedName;

/**
 * first 3 commands constitute the universal basis
 *
 * @author Artur Vasilov
 */
public enum CommandTypes {

    @SerializedName("CQET")
    CQET,
    @SerializedName("QET")
    QET,
    @SerializedName("PHASE")
    PHASE,

    MEASURE,
    INIT
}