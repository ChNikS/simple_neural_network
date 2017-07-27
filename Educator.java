package lab1;

/**
 * Created by ceredniknikita on 01.06.17.
 */
public class Educator {

    public static boolean educateOneApoch(
            Neuron neuron,
            int[] expectedFunction_v16,
            double learningFactor,
            int[] numbersOfTeachingVectors) {

        int stepError = 0;
        int[] inputVector_v4 = new int[4];

        for (int step=0; step<numbersOfTeachingVectors.length; step++) {
            if (!writeBynarySet_v4(numbersOfTeachingVectors[step], inputVector_v4))
                return false;

            stepError = expectedFunction_v16[numbersOfTeachingVectors[step]]
                    - neuron.getRealOutput(inputVector_v4);

            neuron.weightsVector_5v[0] +=
                    learningFactor
                            * stepError
                            * neuron.derivativeOfActivationFunction(
                            neuron.getNetOutput(inputVector_v4));
            for (int numberOfInput=1; numberOfInput<5; numberOfInput++) {
                neuron.weightsVector_5v[numberOfInput] +=
                        learningFactor
                                * stepError
                                * neuron.derivativeOfActivationFunction(
                                neuron.getNetOutput(inputVector_v4))
                                * inputVector_v4[numberOfInput-1];
            }

        }

        return true;
    }

    public static int getQuadraticError(Neuron neuron, int[] expectedFunction_v16) {
        int quadraticError = 0;
        int[] inputVector_v4 = new int[4];
        for (int serialNumberOfInputVector=0; serialNumberOfInputVector<16; serialNumberOfInputVector++) {
            if (! writeBynarySet_v4(serialNumberOfInputVector, inputVector_v4)) return -1;
            quadraticError += Math.abs(expectedFunction_v16[serialNumberOfInputVector]
                    - neuron.getRealOutput(inputVector_v4));
        }
        return quadraticError;
    }

    public static boolean writeBynarySet_v4(int serialNumberOfSet, int[] bynarySet_v4) {
        if (serialNumberOfSet < 0 || serialNumberOfSet > 15) return false;
        if (bynarySet_v4.length < 4) return false;

        for(int i=0; i<=3; i++)
            bynarySet_v4[i] = (serialNumberOfSet >> (3-i)) & 1 ;
        return true;
    }

    public static int[] getRealOutputsFromAllInputs_v16(Neuron neuron) {
        int[] realOutputs_v16 = new int[16];
        int[] inputVector_v4 = new int[4];
        for (int inputNumber=0; inputNumber<16; inputNumber++) {
            if (!writeBynarySet_v4(inputNumber, inputVector_v4)) return null;
            realOutputs_v16[inputNumber] = neuron.getRealOutput(inputVector_v4);
        }
        return realOutputs_v16;
    }
}

