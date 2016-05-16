package NeuralNetwork.Layers;

import NeuralNetwork.Neuron;
import NeuralNetwork.TransferFunctions.TransferFunction;

/**
 * Created by charleshamesse on 16/05/16.
 */
public class FullyConnectedLayer implements Layer {

    private int ni, no;
    private double[] w, b;
    private TransferFunction tf;
    private Neuron[] neurons;

    /**
     * FullyConnectedLayer
     * @param ni Number of input neurons
     * @param no Number of output neurons = number of neurons in this layer
     * @param tf Transfer function
     */
    public FullyConnectedLayer(int ni, int no, TransferFunction tf) {
        this.no = no;
        this.neurons = new Neuron[no];
        this.tf = tf;

        for(int j = 0; j < no; j++)
            neurons[j] = new Neuron(ni);

    }

    public int getHeight() {
        return this.no;
    }

    public Neuron[] getNeurons() {
        return this.neurons;
    }

    public Neuron getNeuron(int i) {
        return this.neurons[i];
    }
    public TransferFunction getTransferFunction() {
        return this.tf;
    }

}
