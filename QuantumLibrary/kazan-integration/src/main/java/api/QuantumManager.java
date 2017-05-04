package api;

import emulator.OneStepAlgorithm;
import emulator.QuantumRegister;
import ru.kpfu.arturvasilov.core.Complex;
import ru.kpfu.arturvasilov.core.ComplexMatrix;
import ru.kpfu.arturvasilov.core.ComplexVector;

import java.util.*;

import static emulator.OneStepAlgorithm.EMPTY_ADDRESS;

/**
 * @author Artur Vasilov
 */
public class QuantumManager {

    protected static final String qubitDestroyedRegisterAddress = "Qubit destroyed";

    // This class must contain quantum registers
    protected final Map<String, RegisterInfo> registers = new HashMap<>();

    //    Qubit creation
    public Qubit initNewQubit() throws Exception {
        return initNewQubit(Complex.one(), Complex.zero());
    }

    public Qubit initNewQubit(Complex alpha, Complex beta) throws Exception {
        QuantumRegister newRegister = new QuantumRegister(1, new ComplexVector(alpha, beta));
        String registerID = Double.toString(new Date().getTime() + new Random().nextDouble());
        Qubit newQubit = new Qubit(registerID, 0);
        List<Qubit> qubits = new ArrayList<>();
        qubits.add(newQubit);
        registers.put(registerID, new RegisterInfo(qubits, newRegister));
        return newQubit;
    }

    //Service functions
    protected RegisterInfo checkAndMergeRegistersIfNeedForQubits(Qubit... qubits) throws Exception {
        List<String> usedRegisterAddresses = new ArrayList<>();
        for (Qubit qubit : qubits) {
            if (!usedRegisterAddresses.contains(qubit.registerAddress)) {
                usedRegisterAddresses.add(qubit.registerAddress);
            }
        }

        if (usedRegisterAddresses.size() == 1) {
            return registers.get(usedRegisterAddresses.get(0));
        }

        //       Create new register merged registers
        ComplexMatrix newRegisterConfiguration = ComplexMatrix.identity(1);
        List<Qubit> newRegisterQubits = new ArrayList<>();
        String newRegisterAddress = Double.toString(new Date().getTime());

        for (String registerAddress : usedRegisterAddresses) {
            RegisterInfo currentRegisterInfo = registers.get(registerAddress);
            newRegisterConfiguration = newRegisterConfiguration.tensorMultiplication(currentRegisterInfo.register.getStateMatrix());
            for (Qubit qubit : currentRegisterInfo.qubits) {
                newRegisterQubits.add(qubit);
                qubit.registerAddress = newRegisterAddress;
                qubit.addressInRegister = newRegisterQubits.size() - 1;
            }
            registers.remove(registerAddress);
        }

        QuantumRegister newRegister = new QuantumRegister(newRegisterQubits.size(), newRegisterConfiguration);

        RegisterInfo newRegisterInfo = new RegisterInfo(newRegisterQubits, newRegister);
        registers.put(newRegisterAddress, newRegisterInfo);

        return newRegisterInfo;
    }

    protected int qubitAddressInRegister(Qubit q) {
        return q.addressInRegister;
    }

    protected void performTransitionForQubits(Qubit controlQubit, ComplexMatrix transitionMatrix,
                                              RegisterInfo mergedRegisterInfo, Qubit... qubits) throws Exception {
        List<Integer> qubitIndexes = new ArrayList<>();
        for (Qubit q : qubits) {
            qubitIndexes.add(q.addressInRegister);
        }

        int controlQubitIndex = EMPTY_ADDRESS;
        if (controlQubit != null) {
            controlQubitIndex = controlQubit.addressInRegister;
        }

        OneStepAlgorithm alg = new OneStepAlgorithm(mergedRegisterInfo.qubits.size(), controlQubitIndex,
                qubitIndexes, transitionMatrix);
        mergedRegisterInfo.register.performAlgorythm(alg);
    }

    public void performTransitionForQubits(Qubit controlQubit, ComplexMatrix transitionMatrix, Qubit... qubits) throws Exception {
        List<Qubit> allQubits = new ArrayList<>();
        Collections.addAll(allQubits, qubits);

        if (controlQubit != null) {
            allQubits.add(controlQubit);
        }

        Qubit[] qubitsArray = new Qubit[allQubits.size()];
        qubitsArray = allQubits.toArray(qubitsArray);
        RegisterInfo info = checkAndMergeRegistersIfNeedForQubits(qubitsArray);
        performTransitionForQubits(controlQubit, transitionMatrix, info, qubits);
    }

    // Operations
    public int measure(Qubit qubit) throws Exception {
        RegisterInfo regInfo = registers.get(qubit.registerAddress);
        int result = regInfo.register.measureQubit(qubit.addressInRegister);
//        int qubitPosition = regInfo.qubits.indexOf(qubit);
//        regInfo.qubits.remove(qubitPosition);
//        for (int i=qubitPosition; i< regInfo.qubits.size(); i++){
//            regInfo.qubits.get(i).addressInRegister --;
//        }
//        qubit.registerAddress = qubitDestroyedRegisterAddress;
//        TODO: remove register if qubits count is 0
        return result;
    }

    public static class Qubit {
        // Можно было сделать его частным случаем регистра, но пока удобнее хранить идентификаторы регистров и их номер в регистре
        String registerAddress;
        int addressInRegister;

        Qubit(String registerAddress, int addressInRegister) {
            this.registerAddress = registerAddress;
            this.addressInRegister = addressInRegister;
        }
    }

    protected static class RegisterInfo {
        public QuantumRegister register;
        public List<Qubit> qubits;

        RegisterInfo(List<Qubit> qubits, QuantumRegister register) {
            this.qubits = qubits;
            this.register = register;
        }
    }
}
