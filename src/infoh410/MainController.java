package infoh410;
import NeuralNetwork.*;
import NeuralNetwork.TransferFunctions.Sigmoid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    private TextArea mainTextArea;

    public MainController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
        mainTextArea.appendText("Starting learn process..");
        test();
    }

    private void test() {
        int[] layers = new int[]{ 2, 2, 1 };

        NeuralNetwork net = new NeuralNetwork(layers, 0.6, new Sigmoid());

		/* Learning */
        for(int i = 0; i < 1000; i++)
        {
            double[] inputs = new double[]{Math.round(Math.random()), Math.round(Math.random())};
            double[] output = new double[1];
            double error;


            if((inputs[0] == inputs[1]) && (inputs[0] == 1))
                output[0] = 1.0;
            else
                output[0] = 0.0;

            mainTextArea.appendText(inputs[0]+" and "+inputs[1]+" = "+output[0] + "\n");

            error = net.backPropagate(inputs, output);
            mainTextArea.appendText("Error at step " + i + " is " + error + "\n");
            System.out.print("Iteration\n");
        }

        mainTextArea.appendText("Learning completed!\n");

		/* Test */
        double[] inputs = new double[]{1.0, 0.0};
        double[] output = net.execute(inputs);

        mainTextArea.appendText(inputs[0]+" and "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")\n");


    }


}
