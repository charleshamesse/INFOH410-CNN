package NeuralNetwork.Layers;

import NeuralNetwork.Neuron;
import NeuralNetwork.TransferFunctions.TransferFunction;
import NeuralNetwork.Utils.Matrix;

/**
 * Created by charleshamesse on 16/05/16.
 */
public class FullyConnectedLayer implements Layer {

    private int nix, niy, nox, noy;
    private double[] w, b;
    private TransferFunction tf;
    private Neuron[][] neurons;
    private Layer previousLayer;

    /**
     * FullyConnectedLayer
     * @param ni Number of input neurons
     * @param no Number of output neurons = number of neurons in this layer
     * @param tf Transfer function
     */
    public FullyConnectedLayer(int[] ni, int[] no, TransferFunction tf) {
        this.nix = ni[0];
        this.niy = ni[1];
        this.nox = no[0];
        this.noy = no[1];

        this.neurons = new Neuron[noy][nox];
        this.tf = tf;

        for(int i = 0; i < noy; i++)
            for(int j = 0; j < nox; j++)
                neurons[i][j] = new Neuron(new int[] {nix, niy});

    }

    public void connectPreviousLayer(Layer l) {
        this.previousLayer = l;
    }

    public void execute() {
        int i, j;
        double new_value;
        for(i = 0; i < getHeight(); i++)
        {
            for(j = 0; j < getWidth(); j++)
            {
                new_value = 0.0;
                for(int l = 0; l < previousLayer.getHeight(); l++)
                    for(int m = 0; m < previousLayer.getWidth(); m++)
                        new_value += neurons[i][j].weights[l][m] * previousLayer.getNeuron(l, m).value;

                new_value += this.getNeuron(i, j).bias;

                neurons[i][j].value = tf.evaluate(new_value);
            }
        }
    }

    public int getHeight() {
        return this.noy;
    }
    public int getWidth() {
        return this.nox;
    }

    public Neuron[][] getNeurons() {
        return this.neurons;
    }

    public Neuron getNeuron(int i, int j) {
        return this.neurons[i][j];
    }

    public TransferFunction getTransferFunction() {
        return this.tf;
    }

}
