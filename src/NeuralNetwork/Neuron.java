package NeuralNetwork;

/**
 * Created by charleshamesse on 16/05/16.
 */
public class Neuron
{
    public double value;
    public double[]		weights;
    public double		bias;
    public double		delta;

    public Neuron(int prevLayerSize)
    {
        weights = new double[prevLayerSize];
        bias = Math.random() / 10000000000000.0;
        delta = Math.random() / 10000000000000.0;
        value = Math.random() / 10000000000000.0;

        for(int i = 0; i < weights.length; i++)
            weights[i] = Math.random() / 10000000000000.0;
    }
}