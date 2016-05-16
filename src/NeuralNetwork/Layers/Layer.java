package NeuralNetwork.Layers;

import NeuralNetwork.Neuron;
import NeuralNetwork.TransferFunctions.TransferFunction;

/**
 * Created by charleshamesse on 16/05/16.
 */
public interface Layer {
    public int getHeight();
    public Neuron[] getNeurons();
    public Neuron getNeuron(int i);
    public TransferFunction getTransferFunction();
}