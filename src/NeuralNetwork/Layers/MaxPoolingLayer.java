package NeuralNetwork.Layers;

import NeuralNetwork.Neuron;
import NeuralNetwork.TransferFunctions.TransferFunction;

/**
 * Created by charleshamesse on 19/05/16.
 */
public class MaxPoolingLayer extends Layer{

    /**
     * MaxPoolingLayer
     * @param ni Number of input neurons
     * @param no Number of output neurons = number of neurons in this layer
     * @param tf Transfer function
     */
    public MaxPoolingLayer(int[] ni, int[] no, TransferFunction tf) {
        super(ni, no, tf);
    }


}
