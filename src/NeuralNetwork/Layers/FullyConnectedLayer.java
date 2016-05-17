package NeuralNetwork.Layers;

import NeuralNetwork.Neuron;
import NeuralNetwork.TransferFunctions.TransferFunction;

/**
 * Created by charleshamesse on 16/05/16.
 */
public class FullyConnectedLayer implements Layer {

    private int nix, niy, nox, noy;
    private double[] w, b;
    private TransferFunction tf;
    private Neuron[][] neurons;

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
