package NeuralNetwork;


import java.io.*;

/**
 * Created by charleshamesse on 16/05/16.
 */

public class NeuralNetwork implements Serializable {
    protected double learningRate = 0.6;
    protected Layer[] layers;
    protected TransferFunction transferFunction;


    /**
     * Constructor
     *
     * @param l array of numbers of neurons per layer
     * @param lr learning rate
     * @param tf transfer function
     */
    public NeuralNetwork(int[] l, double lr, TransferFunction tf)
    {
        learningRate = lr;
        transferFunction = tf;

        layers = new Layer[l.length];

        for(int i = 0; i < layers.length; i++)
        {
            if(i != 0)
            {
                layers[i] = new Layer(l[i], l[i - 1]);
            }
            else
            {
                layers[i] = new Layer(l[i], 0);
            }
        }
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

        double output[] = new double[layers[layers.length - 1].length];

        // Put input
        for(i = 0; i < layers[0].length; i++)
        {
            layers[0].neurons[i].value = input[i];
        }

        // Execute - hiddens + output
        for(k = 1; k < layers.length; k++)
        {
            for(i = 0; i < layers[k].length; i++)
            {
                new_value = 0.0;
                for(j = 0; j < layers[k - 1].length; j++)
                    new_value += layers[k].neurons[i].weights[j] * layers[k - 1].neurons[j].value;

                new_value += layers[k].neurons[i].bias;

                layers[k].neurons[i].value = transferFunction.evaluate(new_value);
            }
        }


        // Get output
        for(i = 0; i < layers[layers.length - 1].length; i++)
        {
            output[i] = layers[layers.length - 1].neurons[i].value;
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

        for(i = 0; i < layers[layers.length - 1].length; i++)
        {
            error = output[i] - new_output[i];
            layers[layers.length - 1].neurons[i].delta = error * transferFunction.evaluateDerivate(new_output[i]);
        }


        for(k = layers.length - 2; k >= 0; k--)
        {
            // Compute layer error and delta
            for(i = 0; i < layers[k].length; i++)
            {
                error = 0.0;
                for(j = 0; j < layers[k + 1].length; j++)
                    error += layers[k + 1].neurons[j].delta * layers[k + 1].neurons[j].weights[i];

                layers[k].neurons[i].delta = error * transferFunction.evaluateDerivate(layers[k].neurons[i].value);
            }

            // Propagate
            for(i = 0; i < layers[k + 1].length; i++)
            {
                for(j = 0; j < layers[k].length; j++)
                    layers[k + 1].neurons[i].weights[j] += learningRate * layers[k + 1].neurons[i].delta *
                            layers[k].neurons[j].value;
                layers[k + 1].neurons[i].bias += learningRate * layers[k + 1].neurons[i].delta;
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
     * Carica una rete MLP da file
     * @param path Path dal quale caricare la rete MLP
     * @return Rete MLP caricata dal file o null
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



    /**
     * @return Costante di apprendimento
     */
    public double getLearningRate()
    {
        return learningRate;
    }


    /**
     *
     * @param rate
     */
    public void	setLearningRate(double rate)
    {
        learningRate = rate;
    }


    /**
     * Imposta una nuova funzione di trasferimento
     *
     * @param fun Funzione di trasferimento
     */
    public void setTransferFunction(TransferFunction fun)
    {
        transferFunction = fun;
    }



    /**
     * @return Dimensione layer di input
     */
    public int getInputLayerSize()
    {
        return layers[0].length;
    }


    /**
     * @return Dimensione layer di output
     */
    public int getOutputLayerSize()
    {
        return layers[layers.length - 1].length;
    }
}
