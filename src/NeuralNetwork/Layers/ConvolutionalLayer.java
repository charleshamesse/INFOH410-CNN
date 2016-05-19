package NeuralNetwork.Layers;

import NeuralNetwork.Neuron;
import NeuralNetwork.TransferFunctions.TransferFunction;
import NeuralNetwork.Utils.Matrix;

/**
 * Created by charleshamesse on 16/05/16.
 */
public class ConvolutionalLayer extends Layer {

    private int nix, niy, nox, noy;
    private double[] w, b;
    private TransferFunction tf;
    private Neuron[][] neurons;
    private double[][] kernel;
    private Layer previousLayer;
    private int stride;

    /**
     * FullyConnectedLayer
     * @param ni Number of input neurons
     * @param no Number of output neurons = number of neurons in this layer
     * @param tf Transfer function
     */
    public ConvolutionalLayer(int[] ni, int[] no, TransferFunction tf) {
        super(ni, no, tf);
        // Stride
        this.stride = 1;
        // Kernel with F = 3
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

        /*
        System.out.println("Padding:");
        System.out.println(Matrix.format(input));
        System.out.println("This layer's dimensions: h:" + getHeight() + ", w:" + getWidth());
        System.out.println("This input's dimensions: h:" + input.length + ", w:" + input[0].length);
        */
        // Convolve kernel
        for(i = 0; i < getHeight(); ++i) {
            for(j = 0; j < getWidth(); ++j) {

                // For each this layer's cells
                new_value = 0.0;
                // Weight does not apply if we're on the border
                for(l = 0; l < kernel.length; ++l) {
                    for(m = 0; m < kernel[0].length; ++m) {
                        if(stride*i+l < getHeight() && stride*j+m < getWidth())
                            new_value += neurons[i][j].weights[stride*i+l][stride*j+m] * input[stride*i+l][stride*j+m] * kernel[l][m];
                    }
                }
                /*
                new_value += neurons[i][j].weights[stride*i][stride*j] * input[stride*i][stride*j] * kernel[0][0];
                new_value += neurons[i][j].weights[stride*i][stride*j+1] * input[stride*i][stride*j+1]*kernel[0][1];
                new_value += neurons[i][j].weights[stride*i][stride*j+2] * input[stride*i][stride*j+2]*kernel[0][2];
                new_value += neurons[i][j].weights[stride*i+1][stride*j] * input[stride*i+1][stride*j]*kernel[1][0];
                new_value += neurons[i][j].weights[stride*i+1][stride*j+1] * input[stride*i+1][stride*j+1]*kernel[1][1];
                new_value += neurons[i][j].weights[stride*i+1][stride*j+2] * input[stride*i+1][stride*j+2]*kernel[1][2];
                new_value += neurons[i][j].weights[stride*i+2][stride*j] * input[stride*i+2][stride*j]*kernel[2][0];
                new_value += neurons[i][j].weights[stride*i+2][stride*j+1] * input[stride*i+2][stride*j+1]*kernel[2][1];
                new_value += neurons[i][j].weights[stride*i+2][stride*j+2] * input[stride*i+2][stride*j+2]*kernel[2][2];
                /*
                for(l = 0; l < kernel.length; l++)
                    for(m = 0; m < kernel[0].length; m++)
                        //if(i != 0 && j != 0 && i != input.length-1 && j != input[0].length-1) // No weight defined on borders
                            if(stride*i+l < neurons[i][j].weights.length && stride*j+m < neurons[i][j].weights[0].length)
                                new_value += neurons[i][j].weights[stride*i+l][stride*j+m] * input[stride*i+l][stride*j+m];


        System.out.println("First");
        System.out.println(Matrix.format(previousLayer.getNeurons()));
        System.out.println("Second");
        System.out.println(Matrix.format(neurons));
                */

                new_value += this.getNeuron(i, j).bias;
                neurons[i][j].value = tf.evaluate(new_value);
            }
        }


    }
    private Double[][] applyPadding() {
        int h = previousLayer.getHeight() + 2;
        int w = previousLayer.getWidth() + 2;
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
