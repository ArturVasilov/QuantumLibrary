package emulator;

import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.Operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Algorithm is a matrix. Each cell is the QuantumSchemeStepQubitAttributes
 * (Map) gates :{gateID:specifications (Quantum gate)}.
 * Generally quantum step and algorithm are quantum gates too.
 *
 * @author Artur Vasilov
 */
@SuppressWarnings("Duplicates")
public class QuantumAlgorithm {

    protected int qubitsNumber;
    protected int size;

    protected int stepsNumber;
    protected QuantumSchemeStepQubitAttributes[][] algorithmSchemeMatrix;
    protected String[] mainGateIDs;
    protected Map<String, ComplexMatrix> gates;

    public QuantumAlgorithm() {
        stepsNumber = 0;
    }

    public QuantumAlgorithm(QuantumSchemeStepQubitAttributes[][] algorithmSchemeMatrix, String[] mainGateIDs,
                            Map<String, ComplexMatrix> gates) {
        this.algorithmSchemeMatrix = algorithmSchemeMatrix;
        this.gates = gates;
        this.mainGateIDs = mainGateIDs;
        qubitsNumber = algorithmSchemeMatrix.length;
        stepsNumber = algorithmSchemeMatrix[0].length;
        size = (int) Math.pow(2, qubitsNumber);
    }

    ComplexMatrix generateStepMatrix(int step) throws Exception {
        int mainGateIndexesSum = 0;
        int count = 0;
        String mainGateID = mainGateIDs[step];
        ArrayList<Number> mainGateQubits = new ArrayList<>();
        for (int qubitNum = 0; qubitNum < qubitsNumber; qubitNum++) {//loop for each qubit
            QuantumSchemeStepQubitAttributes qubitParams = algorithmSchemeMatrix[qubitNum][step];
            if (qubitParams.gateID.equals(mainGateID)) {
                mainGateIndexesSum += qubitNum;
                count++;
                mainGateQubits.add(qubitNum);
            }
        }
        ComplexMatrix gateMatrix = gates.get(mainGateID);
        ComplexMatrix result;
        if (checkAdjustment(mainGateQubits)) {
            ComplexMatrix centralMatr = ComplexMatrix.identity(1);
            //if qubits is near to each other just multiply identity gates and mainGate matrices (tensors)
            for (int currentQubit = 0; currentQubit < qubitsNumber; ) {//loop for each qubit
                QuantumSchemeStepQubitAttributes qubitParams = algorithmSchemeMatrix[currentQubit][step];
                if (qubitParams.gateID.equals(mainGateID)) {
                    currentQubit += mainGateQubits.size();
                    centralMatr = centralMatr.tensorMultiplication(gateMatrix);
                } else if (qubitParams.gateID.equals(QuantumSchemeStepQubitAttributes.IdentityGateID)) {
                    centralMatr = centralMatr.tensorMultiplication(ComplexMatrix.identity(2));
                    currentQubit++;
                } else {
                    throw new Exception("Two non trivial gates at step!");
                }
            }

            //Move control qubit to top if need
            int controlQubitIndex = -1;
            for (Number mainGateQubit : mainGateQubits) {
                int qubitIndex = mainGateQubit.intValue();
                if (algorithmSchemeMatrix[qubitIndex][step].control)
                    controlQubitIndex = qubitIndex;
            }

            List<ComplexMatrix> swapMatrices = new ArrayList<>();

            if (controlQubitIndex != -1) {
                ComplexMatrix swapGateMatrix = Operators.swap();
                ComplexMatrix identityMatrx = ComplexMatrix.identity(2);
                int higherQubitIndex = mainGateQubits.get(0).intValue();
                for (; controlQubitIndex > higherQubitIndex; controlQubitIndex--) {
                    ComplexMatrix currentSwap = ComplexMatrix.identity(1);
                    for (int i = 0; i < qubitsNumber; ) {
                        if (i < qubitsNumber - 1 && i + 1 == controlQubitIndex) {
                            currentSwap = currentSwap.tensorMultiplication(swapGateMatrix);
                            controlQubitIndex = i;
                            i += 2;
                        } else {
                            currentSwap = currentSwap.tensorMultiplication(identityMatrx);
                            i++;
                        }
                    }
                    swapMatrices.add(currentSwap);
                }

                if (swapMatrices.size() > 0) {
                    result = swapMatrices.get(0).copy();
                    for (int i = 1; i < swapMatrices.size(); i++) {
                        result = result.multiply(swapMatrices.get(i));
                    }

                    ComplexMatrix swapConj = result.conjugateTranspose();
                    result = result.multiply(centralMatr);
                    result = result.multiply(swapConj);
                } else {
                    result = centralMatr;
                }
            } else {
                result = centralMatr;
            }

        } else {
            //Group one gate qubits
            //check Type of gravityCenter (maybe needs double)
            int gravityCenter = 0;
            if (count > 0) {
                gravityCenter = mainGateIndexesSum / count;
            }

            int currentUpperQubitIdex = mainGateQubits.get(0).intValue();

            int upperQubit, lowerQubit;
            int levelNumber = mainGateQubits.size() / 2 + mainGateQubits.size() % 2;
            ComplexMatrix centralMatrix = ComplexMatrix.identity(1); //matrix perfomed main gate when all qubits are near
            ComplexMatrix swapGateMatrix = Operators.swap();
            ComplexMatrix identityMatrix = ComplexMatrix.identity(2);

            ComplexMatrix swapMatrix = null;

            boolean[] currentQubitsPositions = new boolean[qubitsNumber];
            for (Number index : mainGateQubits) {
                currentQubitsPositions[index.intValue()] = true;
            }

            for (int level = 0; level <= levelNumber; level++) {
                //find upper and lower qubits. Upper index is less than lower index
                upperQubit = -1;
                lowerQubit = -1; //empty
                int upperPlace = gravityCenter - level;
                int lowerPlace = gravityCenter + level;
                int upperIndex = upperPlace;
                int lowerIndex = lowerPlace;


                int distance;
                for (; upperIndex >= 0; upperIndex--) {
                    if (upperQubit == -1 && currentQubitsPositions[upperIndex]) {
                        upperQubit = upperIndex;
                        currentQubitsPositions[upperIndex] = false;
                        currentQubitsPositions[gravityCenter - level] = true;
                        break;
                    }
                }

                if (level > 0) {
                    for (; lowerIndex < qubitsNumber; lowerIndex++) {
                        if (lowerQubit == -1 && currentQubitsPositions[lowerIndex]) {
                            lowerQubit = lowerIndex;
                            currentQubitsPositions[lowerIndex] = false;
                            currentQubitsPositions[gravityCenter + level] = true;
                            break;
                        }
                    }
                }

                distance = Math.max(upperPlace - upperQubit, lowerQubit - lowerPlace);

                //move qubits to gravity center + level
                for (; distance > 0; distance--) {
                    //form swap matrix
                    ComplexMatrix currentDistanceSwap = ComplexMatrix.identity(1);
                    for (int i = 0; i < qubitsNumber; ) {
                        if ((i == upperQubit && upperQubit == upperPlace - distance) ||
                                (i == lowerQubit - 1 && lowerQubit == lowerPlace + distance)) {
                            //need to swap upper gate

                            if (i == upperQubit) {
                                upperQubit++;
                            }

                            if (i == lowerQubit - 1) {
                                lowerQubit--;
                            }

                            currentDistanceSwap = currentDistanceSwap.tensorMultiplication(swapGateMatrix);
                            i += 2;
                        } else {
                            currentDistanceSwap = currentDistanceSwap.tensorMultiplication(identityMatrix);
                            i++;
                        }
                    }
                    if (swapMatrix == null) {
                        swapMatrix = currentDistanceSwap.copy();
                    } else {
                        swapMatrix = currentDistanceSwap.multiply(swapMatrix);
                    }
                }

                if (upperQubit != -1) {
                    currentUpperQubitIdex = upperPlace;
                }
            }

            //Move control qubit to top if need
            int controlQubitIndex = -1;
            for (int i = 0; i < mainGateQubits.size(); i++) {
                if (algorithmSchemeMatrix[mainGateQubits.get(i).intValue()][step].control)
                    controlQubitIndex = i;
            }
            if (controlQubitIndex != -1) {
                //project to current positions
                controlQubitIndex = currentUpperQubitIdex + controlQubitIndex;
                for (; controlQubitIndex > currentUpperQubitIdex; controlQubitIndex--) {
                    ComplexMatrix currentSwap = ComplexMatrix.identity(1);
                    for (int i = 0; i < qubitsNumber; ) {
                        if (i < qubitsNumber - 1 && i + 1 == controlQubitIndex) {
                            currentSwap = currentSwap.tensorMultiplication(swapGateMatrix);
                            controlQubitIndex = i;
                            i += 2;
                        } else {
                            currentSwap = currentSwap.tensorMultiplication(identityMatrix);
                            i++;
                        }
                    }
                    swapMatrix = currentSwap.multiply(swapGateMatrix);
                }
            }
            //form central matrix after swaps
            for (int i = 0; i < qubitsNumber; ) {
                if (i == gravityCenter - levelNumber / 2) {
                    centralMatrix = centralMatrix.tensorMultiplication(gateMatrix);
                    i += mainGateQubits.size();
                } else {
                    centralMatrix = centralMatrix.tensorMultiplication(identityMatrix);
                    i++;
                }
            }
            ComplexMatrix swapConjugateMatrix = swapMatrix.conjugateTranspose();
            result = centralMatrix.multiply(swapMatrix);
            result = swapConjugateMatrix.multiply(result);
        }
        return result;
    }

    boolean checkAdjustment(ArrayList<Number> listToCheck) {
        for (int i = 1; i < listToCheck.size(); i++) {
            if (listToCheck.get(i).intValue() > listToCheck.get(i - 1).intValue() + 1) {
                return false;
            }
        }
        return true;
    }

    public ComplexMatrix getMatrix() throws Exception {
        ComplexMatrix result = generateStepMatrix(stepsNumber - 1);
        for (int i = stepsNumber - 2; i >= 0; i--) {
            result = result.multiply(generateStepMatrix(i));
        }
        return result;
    }
}