package lab1;

/**
 * Created by ceredniknikita on 01.06.17.
 */
import java.io.FileWriter;
import java.io.IOException;

public class MainClass {
    private static final String OUTPUT_FILE_NAME = "out.txt";
    private static final double[] INITIAL_WEIGHTS_V5 = {0,0,0,0,0};
    private static final int[] EXPECTED_FUNCTION_V16
            = {0,0,0,0,  1,1,1,1,  0,0,0,0,  0,1,1,1};
    private static final int MAX_APOCHES = 1000;
    private static final double LEANRNING_FACTOR_FIRST_STAGE = 0.1;
    private static final double LEANRNING_FACTOR_SECOND_STAGE = 0.3;
    private static final double LEANRNING_FACTOR_THIRD_STAGE = 0.1;
    private static final double LEANRNING_FACTOR_FOURTH_STAGE = 0.3;
    private static final boolean DEBUG_INFO = true;
    private static final int[] NUMBER_OF_TEACHING_VECTORS_FIRST_STAGE =
            {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private static final int[] NUMBER_OF_TEACHING_VECTORS_SECOND_STAGE =
            {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

    private static FileWriter outStream = null;

    public static void main(String[] args) {
        try {

            if (!openOutputFile()) return;

            doFirstStage();
            doSecondStage();
            doThirdStage();
            doFourthStage();

            closeOutputFile();

        } catch(Exception exception) {
            System.out.println("Error proceeding the program:\n"+exception.toString());
        }
    }

    private static boolean openOutputFile() {
        try {
            outStream = new FileWriter(OUTPUT_FILE_NAME);
            if (DEBUG_INFO) System.out.println("Out-stream is open.");
            return true;
        } catch (IOException exception) {
            System.out.println("Error opening output-file:\n"+exception.toString());
            return false;
        }
    }

    private static boolean closeOutputFile() {
        try {
            outStream.close();
            if (DEBUG_INFO) System.out.println("Out-stream is closed.");
            return true;
        } catch (IOException exception) {
            System.out.println("Error closing output-file:\n"+exception.toString());
            return false;
        }
    }

    private static void doFirstStage() throws Exception {
        outStream.write("\n\n");
        outStream.write("  |   FIRST_STAGE   | \n");


        Neuron neuron = createNeuronForFirstStage();
        int numberOfApoch = -1;
        int[] realOutputs_v16 = null;
        int quadraticError = 0;
        while ( (numberOfApoch++) < MAX_APOCHES
                && (quadraticError
                = Educator.getQuadraticError(neuron, EXPECTED_FUNCTION_V16)) > 0) {
            realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
            writeResultForApoch(numberOfApoch, realOutputs_v16, quadraticError,
                    neuron.weightsVector_5v);
            Educator.educateOneApoch(neuron,
                    EXPECTED_FUNCTION_V16,
                    LEANRNING_FACTOR_FIRST_STAGE,
                    NUMBER_OF_TEACHING_VECTORS_FIRST_STAGE);
        }
        realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
        writeResultForApoch(numberOfApoch, realOutputs_v16, quadraticError,
                neuron.weightsVector_5v);
    }

    private static void doSecondStage() throws Exception {
        outStream.write("\n\n");
        outStream.write("  -------------------- \n");
        outStream.write("  |   SECOND_STAGE   | \n");
        outStream.write("  -------------------- \n\n");

        Neuron neuron = createNeuronForSecondStage();
        int numberOfApoch = -1;
        int[] realOutputs_v16 = null;
        int quadraticError = 0;
        while ( (numberOfApoch++) < MAX_APOCHES
                && (quadraticError
                = Educator.getQuadraticError(neuron, EXPECTED_FUNCTION_V16)) > 0) {
            realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
            writeResultForApoch(numberOfApoch, realOutputs_v16, quadraticError,
                    neuron.weightsVector_5v);
            Educator.educateOneApoch(neuron,
                    EXPECTED_FUNCTION_V16,
                    LEANRNING_FACTOR_SECOND_STAGE,
                    NUMBER_OF_TEACHING_VECTORS_SECOND_STAGE);
        }
        realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
        writeResultForApoch(numberOfApoch, realOutputs_v16, quadraticError,
                neuron.weightsVector_5v);

    }

    private static void doThirdStage() throws Exception {
        outStream.write("\n\n");
        outStream.write("  |    THIRD_STAGE   | \n");

        int numberOfEpoch = -1;
        Neuron neuron = null;
        int[] realOutputs_v16 = null;
        int quadraticError = 0;

        TeachingVectorsGenerator generator = null;
        int[] numbersOfTeachingVectors = null;

        // Начинаем увеличивать вес обучающей выборки, пока не обучится
        for(int amountOfSets=1; amountOfSets<17; amountOfSets++) {
            generator = new TeachingVectorsGenerator(amountOfSets);
            // Перебор всех возможных обучающие выборок веса 'amountOfSets'
            while ( (numbersOfTeachingVectors=generator.getNext()) != null) {
                // возврат НС в исходное состояние
                neuron = createNeuronForThirdStage();
                numberOfEpoch = -1;

                //outStream.write("Обучающая выборка:");
                for (int i=0; i<numbersOfTeachingVectors.length; i++)
                    //outStream.write(" "+numbersOfTeachingVectors[i]);

                // Обучение до успешного обучения, либо до превышения числа эпох
                while ( (numberOfEpoch++) < MAX_APOCHES
                        && (quadraticError
                        = Educator.getQuadraticError(neuron, EXPECTED_FUNCTION_V16)) > 0) {
                    //realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
                    //writeResultForApoch(numberOfEpoch, realOutputs_v16, quadraticError,
                    //        neuron.weightsVector_5v);
                    Educator.educateOneApoch(neuron,
                            EXPECTED_FUNCTION_V16,
                            LEANRNING_FACTOR_THIRD_STAGE,
                            numbersOfTeachingVectors);
                }
                if (numberOfEpoch >= MAX_APOCHES) {  // не обучилась
                    //outStream.write("     Результат: ПРОВАЛ\n");
                    continue;
                }
                else { // ОБУЧИЛАСЬ !!!
                    outStream.write("     Результат: УСПЕХ !!!\n\n");

                    // Выводим информацию по процессу обучения и завершаемся

                    // возврат НС в исходное состояние
                    neuron = createNeuronForThirdStage();
                    numberOfEpoch = -1;
                    while ( (numberOfEpoch++) < MAX_APOCHES
                            && (quadraticError
                            = Educator.getQuadraticError(neuron, EXPECTED_FUNCTION_V16)) > 0) {
                        realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
                        writeResultForApoch(numberOfEpoch, realOutputs_v16, quadraticError,
                                neuron.weightsVector_5v);
                        Educator.educateOneApoch(neuron,
                                EXPECTED_FUNCTION_V16,
                                LEANRNING_FACTOR_THIRD_STAGE,
                                numbersOfTeachingVectors);
                    }
                    realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
                    writeResultForApoch(numberOfEpoch, realOutputs_v16, quadraticError,
                            neuron.weightsVector_5v);

                    outStream.write("\n\nОбучилась на выборке:\n");
                    for (int i=0; i<numbersOfTeachingVectors.length; i++) {
                        outStream.write("   "+numbersOfTeachingVectors[i]+" = ( ");
                        int[] vector = new int[4];
                        Educator.writeBynarySet_v4(numbersOfTeachingVectors[i], vector);
                        for(int j=0; j<vector.length; j++)
                            outStream.write(vector[j]+" ");
                        outStream.write(")\n");
                    }
                    outStream.write("\nВес минимальной выборки для обучения:\n");
                    outStream.write("   "+numbersOfTeachingVectors.length);
                    return;
                }
            }
        }

    }


    private static void doFourthStage() throws Exception {
        outStream.write("\n\n");
        outStream.write("  |   FOURTH_STAGE   | \n");

        int numberOfEpoch = -1;
        Neuron neuron = null;
        int[] realOutputs_v16 = null;
        int quadraticError = 0;

        TeachingVectorsGenerator generator = null;
        int[] numbersOfTeachingVectors = null;

        // Начинаем увеличивать вес обучающей выборки, пока не обучится
        for(int amountOfSets=1; amountOfSets<17; amountOfSets++) {
            generator = new TeachingVectorsGenerator(amountOfSets);
            // Перебор всех возможных обучающие выборок веса 'amountOfSets'
            while ( (numbersOfTeachingVectors=generator.getNext()) != null) {
                // возврат НС в исходное состояние
                neuron = createNeuronForFourthStage();
                numberOfEpoch = -1;

                //outStream.write("Обучающая выборка:");
                for (int i=0; i<numbersOfTeachingVectors.length; i++)
                    //outStream.write(" "+numbersOfTeachingVectors[i]);

                // Обучение до успешного обучения, либо до превышения числа эпох
                while ( (numberOfEpoch++) < MAX_APOCHES
                        && (quadraticError
                        = Educator.getQuadraticError(neuron, EXPECTED_FUNCTION_V16)) > 0) {
                    //realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
                    //writeResultForApoch(numberOfEpoch, realOutputs_v16, quadraticError,
                    //        neuron.weightsVector_5v);
                    Educator.educateOneApoch(neuron,
                            EXPECTED_FUNCTION_V16,
                            LEANRNING_FACTOR_FOURTH_STAGE,
                            numbersOfTeachingVectors);
                }
                if (numberOfEpoch >= MAX_APOCHES) {  // не обучилась
                    //outStream.write("     Результат: ПРОВАЛ\n");
                    continue;
                }
                else { // ОБУЧИЛАСЬ !!!
                    outStream.write("     Результат: УСПЕХ !!!\n\n");
                    // Выводим информацию по процессу обучения и завершаемся

                    // возврат НС в исходное состояние
                    neuron = createNeuronForFourthStage();
                    numberOfEpoch = -1;
                    while ( (numberOfEpoch++) < MAX_APOCHES
                            && (quadraticError
                            = Educator.getQuadraticError(neuron, EXPECTED_FUNCTION_V16)) > 0) {
                        realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
                        writeResultForApoch(numberOfEpoch, realOutputs_v16, quadraticError,
                                neuron.weightsVector_5v);
                        Educator.educateOneApoch(neuron,
                                EXPECTED_FUNCTION_V16,
                                LEANRNING_FACTOR_FOURTH_STAGE,
                                numbersOfTeachingVectors);
                    }
                    realOutputs_v16 = Educator.getRealOutputsFromAllInputs_v16(neuron);
                    writeResultForApoch(numberOfEpoch, realOutputs_v16, quadraticError,
                            neuron.weightsVector_5v);

                    outStream.write("\n\nОбучилась на выборке:\n");
                    for (int i=0; i<numbersOfTeachingVectors.length; i++) {
                        outStream.write("   "+numbersOfTeachingVectors[i]+" = ( ");
                        int[] vector = new int[4];
                        Educator.writeBynarySet_v4(numbersOfTeachingVectors[i], vector);
                        for(int j=0; j<vector.length; j++)
                            outStream.write(vector[j]+" ");
                        outStream.write(")\n");
                    }
                    outStream.write("\nВес минимальной выборки для обучения:\n");
                    outStream.write("   "+numbersOfTeachingVectors.length);
                    return;
                }
            }
        }

    }

    private static Neuron createNeuronForFirstStage() {
        Neuron neuron = new Neuron(INITIAL_WEIGHTS_V5) {
            @Override
            public double activationFunction(double netInput) {
                return netInput;
            }

            @Override
            public double derivativeOfActivationFunction(double netInput) {
                return 1;
            }

            @Override
            public int getRealOutput(int[] inputVector_4v) {
                return getNetOutput(inputVector_4v) < 0 ? 0 : 1;
            }
        };
        return neuron;
    }

    private static Neuron createNeuronForSecondStage() {
        Neuron neuron = new Neuron(INITIAL_WEIGHTS_V5) {
            @Override
            public double activationFunction(double netInput) {
                return 0.5+0.5*netInput/(1+Math.abs(netInput));
            }

            @Override
            public double derivativeOfActivationFunction(double netInput) {
                return 0.5*Math.pow((1-Math.abs(activationFunction(netInput))),2);
            }

            @Override
            public int getRealOutput(int[] inputVector_4v) {
                double netInput = getNetOutput(inputVector_4v);
                return activationFunction(netInput) < 0.5 ? 0 : 1;
            }
        };
        return neuron;
    }

    private static Neuron createNeuronForThirdStage() {
        return createNeuronForFirstStage();
    }

    private static Neuron createNeuronForFourthStage() {
        return createNeuronForSecondStage();
    }

    private static void writeResultForApoch(
            int numberOfApoch,
            int[] realOutputs_v16,
            int quadraticError,
            double[] neuronWeights_v5) {
        try {

            outStream.write("\nk = "+numberOfApoch);
            outStream.write("\nY = (");
            for (int i=0; i<realOutputs_v16.length; i++)
                outStream.write(" "+realOutputs_v16[i]);
            outStream.write(" )\nW = (");
            for (int i=0; i<neuronWeights_v5.length; i++)
                //outStream.write(" "+neuronWeights_v5[i]);
                outStream.write(" "+String.format( "%.5f", neuronWeights_v5[i] ));

            //System.out.println( String.format( "%.2f", dub ) );

            outStream.write(" )\nE = "+quadraticError);
            outStream.write("\n=============================================");

        } catch (Exception exception) {
            System.out.println("[Educator->writeResultForApoch]:"+exception.toString());
        }
    }
}

