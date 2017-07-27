package lab1;

/**
 * Created by ceredniknikita on 01.06.17.
 */
public abstract class Neuron {
    public double weightsVector_5v[];

    public Neuron(double[] initialWeightsVector_5v) {
        weightsVector_5v = new double[5];
        for (int i=0; i<5; i++)
            weightsVector_5v[i] = initialWeightsVector_5v[i];
    }

    public double getNetOutput(int[] inputVector_4v) {  //net
        double result = weightsVector_5v[0];
        for (int i=1; i<5; i++)
            result += inputVector_4v[i-1]*weightsVector_5v[i];
        return result;
    }

    abstract public double activationFunction(double netInput);  // f
    abstract public double derivativeOfActivationFunction(double netInput);  //  d f(net)/d net
    abstract public int getRealOutput(int[] inputVector_4v);  //Y
}

