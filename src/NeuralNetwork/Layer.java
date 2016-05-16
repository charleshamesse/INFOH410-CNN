package NeuralNetwork;

/**
 * Created by charleshamesse on 16/05/16.
 */

public class Layer
{
    public Neuron neurons[];
    public int length;

    /**
     * Layer
     *
     * @param l dimension of this layer
     * @param prev dimension of previous layer
     */
    public Layer(int l, int prev)
    {
        length = l;
        neurons = new Neuron[l];

        for(int j = 0; j < length; j++)
            neurons[j] = new Neuron(prev);
    }
}