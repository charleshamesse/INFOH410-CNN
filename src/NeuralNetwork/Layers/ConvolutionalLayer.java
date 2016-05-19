package NeuralNetwork.Layers;

import NeuralNetwork.Neuron;
import NeuralNetwork.TransferFunctions.TransferFunction;
import NeuralNetwork.Utils.Matrix;

/**
 * Created by charleshamesse on 16/05/16.
 */
public class ConvolutionalLayer implements Layer {

    private int nix, niy, nox, noy;
    private double[] w, b;
    private TransferFunction tf;
    private Neuron[][] neurons;
    private double[][] kernel;
    private Layer previousLayer;

    /**
     * FullyConnectedLayer
     * @param ni Number of input neurons
     * @param no Number of output neurons = number of neurons in this layer
     * @param tf Transfer function
     */
    public ConvolutionalLayer(int[] ni, int[] no, TransferFunction tf) {
        this.nix = ni[0];
        this.niy = ni[1];
        this.nox = no[0];
        this.noy = no[1];
        this.tf = tf;

        this.neurons = new Neuron[noy][nox];

        for(int i = 0; i < noy; i++)
            for(int j = 0; j < nox; j++)
                neurons[i][j] = new Neuron(new int[] {nix, niy});

        this.kernel = new double[][]{
                {0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0}
        };

    }

    public void connectPreviousLayer(Layer l) {
        this.previousLayer = l;
    }

    public void execute() {
        // TO-DO
        int i, j, l, m;
        double new_value;
        // Pad input
        Double[][] input = applyPadding();
        // Convolve kernel
        for(i = 0; i < input.length - (kernel.length - 2); i += 2)
        {
            for(j = 0; j < input[0].length - (kernel[0].length - 2); j += 2)
            {
                new_value = 0.0;
                for(l = 0; l < kernel.length; ++l) {
                    for(m = 0; m < kernel[0].length; ++m) {
                        new_value += input[i+l][j+m] * neurons[l][m].weights[l][m] * kernel[l][m];
                    }
                }
                /*
                new_value = 0.0;
                for(int l = 0; l < input.length - (kernel.length-1); l++)
                    for(int m = 0; m < input[0].length - (kernel[0].length-1); m++)
                        new_value += neurons[i][j].weights[l][m] * previousLayer.getNeuron(l, m).value;

                new_value += this.getNeuron(i, j).bias;
                */
                neurons[i/2][j/2].value = tf.evaluate(new_value);
            }
        }
    }
    private Double[][] applyPadding() {
        int h = previousLayer.getHeight() + 1;
        int w = previousLayer.getWidth() + 1;
        int i, j;
        Double[][] new_input = new Double[h][w];
        for(i = 0; i < h; ++i) {
            for(j = 0; j < w; ++j) {
                if(i == 0 || j == 0 || i == h-1 || j == w-1) new_input[i][j] = 0.0;
                else new_input[i][j] = previousLayer.getNeuron(i-1,j-1).value;
            }
        }
        return new_input;
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
