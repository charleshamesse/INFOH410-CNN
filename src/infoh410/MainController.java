package infoh410;
import NeuralNetwork.*;
import NeuralNetwork.Layers.FullyConnectedLayer;
import NeuralNetwork.Layers.Layer;
import NeuralNetwork.TransferFunctions.Sigmoid;
import NeuralNetwork.TransferFunctions.TransferFunction;
import NeuralNetwork.Utils.Matrix;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    private TextArea mainTextArea;

    public MainController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void start() {
        mainTextArea.appendText("Starting learn process..\n");
        test();
    }

    private void test() {
        TransferFunction tf = new Sigmoid();
        Layer[] layers = new Layer[]{
                new FullyConnectedLayer(new int[]{0, 0}, new int[]{3, 3}, tf),
                new FullyConnectedLayer(new int[]{3, 3}, new int[]{3, 3}, tf),
                new FullyConnectedLayer(new int[]{3, 3}, new int[]{1, 1}, tf)
        };

        NeuralNetwork net = new NeuralNetwork(layers, 0.6);

		/* Learning */
        for (int i = 0; i < 2000; i++) {
            mainTextArea.appendText("\n\nIteration " + i);
            double[][] inputs = new double[][]{
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())}
            };
            double[][] output = new double[1][1];
            double error;

            mainTextArea.appendText(Matrix.format(inputs));
            if (inputs[0][0] == 1 && inputs[1][1] == 1 && inputs[2][2] == 1)
                output[0][0] = 1;
            else if (inputs[2][0] == 1 && inputs[1][1] == 1 && inputs[0][2] == 1)
                output[0][0] = 1;
            else
                output[0][0] = 0;

            error = net.backPropagate(inputs, output);
            mainTextArea.appendText("\nError at step " + i + " is " + error + "\n");
        }

        mainTextArea.appendText("Learning completed!\n");

		/* Test */
        double[][] inputs = new double[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 1}
        };
        double[][] output = net.execute(inputs);
        mainTextArea.appendText("Test result: " + Math.round(output[0][0]) + " (" + output[0][0] + ")\n");


    }

}
