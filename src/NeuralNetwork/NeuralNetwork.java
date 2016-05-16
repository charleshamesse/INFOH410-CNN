package NeuralNetwork;


import NeuralNetwork.Layers.Layer;
import NeuralNetwork.TransferFunctions.TransferFunction;

import java.io.*;

/**
 * Created by charleshamesse on 16/05/16.
 */

public class NeuralNetwork implements Serializable {
    protected double learningRate = 0.2;
    protected Layer[] layers;
    protected TransferFunction transferFunction;


    /**
     * Constructor
     *
     * @param layers Array of layers for this network
     * @param lr learning rate
     */
    public NeuralNetwork(Layer[] layers, double lr)
    {
        learningRate = lr;
        this.layers = layers;

    }


    /**
     * 
     *
     * @param input 
     * @return 
     */
    public double[] execute(double[] input)
    {
        int i;
        int j;
        int k;
        double new_value;

        double output[] = new double[layers[layers.length - 1].getHeight()];

        // Put input
        for(i = 0; i < layers[0].getHeight(); i++)
        {
            layers[0].getNeuron(i).value = input[i];
        }

        // Execute - hiddens + output
        for(k = 1; k < layers.length; k++)
        {
            for(i = 0; i < layers[k].getHeight(); i++)
            {
                new_value = 0.0;
                for(j = 0; j < layers[k - 1].getHeight(); j++)
                    new_value += layers[k].getNeuron(i).weights[j] * layers[k - 1].getNeuron(j).value;

                new_value += layers[k].getNeuron(i).bias;

                layers[k].getNeuron(i).value = layers[k].getTransferFunction().evaluate(new_value);
            }
        }


        // Get output
        for(i = 0; i < layers[layers.length - 1].getHeight(); i++)
        {
            output[i] = layers[layers.length - 1].getNeuron(i).value;
        }

        return output;
    }




    /**
     * Backprop algorithm
     *
     * @param input
     * @param output
     * @return
     */
    public double backPropagate(double[] input, double[] output)
    {
        double new_output[] = execute(input);
        double error;
        int i, j, k;

        for(i = 0; i < layers[layers.length - 1].getHeight(); i++)
        {
            error = output[i] - new_output[i];
            layers[layers.length - 1].getNeuron(i).delta = error * layers[layers.length - 1].getTransferFunction().evaluateDerivate(new_output[i]);
        }


        for(k = layers.length - 2; k >= 0; k--)
        {
            // Compute layer error and delta
            for(i = 0; i < layers[k].getHeight(); i++)
            {
                error = 0.0;
                for(j = 0; j < layers[k + 1].getHeight(); j++)
                    error += layers[k + 1].getNeuron(j).delta * layers[k + 1].getNeuron(j).weights[i];

                layers[k].getNeuron(i).delta = error * layers[k].getTransferFunction().evaluateDerivate(layers[k].getNeuron(i).value);
            }

            // Propagate
            for(i = 0; i < layers[k + 1].getHeight(); i++)
            {
                for(j = 0; j < layers[k].getHeight(); j++)
                    layers[k + 1].getNeuron(i).weights[j] += learningRate * layers[k + 1].getNeuron(i).delta *
                            layers[k].getNeuron(j).value;
                layers[k + 1].getNeuron(i).bias += learningRate * layers[k + 1].getNeuron(i).delta;
            }
        }

        // Compute error
        error = 0.0;

        for(i = 0; i < output.length; i++)
        {
            error += Math.abs(new_output[i] - output[i]);

            //System.out.println(output[i]+" "+new_output[i]);
        }

        error = error / output.length;
        return error;
    }


    /**
     * Save trained network
     *
     * @param path Path nel quale salvare la rete MLP
     * @return true se salvata correttamente
     */
    public boolean save(String path)
    {
        try
        {
            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }


    /**
     * Load an existing network
     * @param path
     * @return
     */
    public static NeuralNetwork load(String path)
    {
        try
        {
            NeuralNetwork net;

            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream oos = new ObjectInputStream(fin);
            net = (NeuralNetwork) oos.readObject();
            oos.close();

            return net;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public double getLearningRate()
    {
        return learningRate;
    }
    public void	setLearningRate(double rate)
    {
        learningRate = rate;
    }

}
