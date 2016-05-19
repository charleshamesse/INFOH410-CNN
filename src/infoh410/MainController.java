package infoh410;
import NeuralNetwork.*;
import NeuralNetwork.Layers.ConvolutionalLayer;
import NeuralNetwork.Layers.FullyConnectedLayer;
import NeuralNetwork.Layers.Layer;
import NeuralNetwork.Layers.MaxPoolingLayer;
import NeuralNetwork.TransferFunctions.Sigmoid;
import NeuralNetwork.TransferFunctions.TransferFunction;
import NeuralNetwork.Utils.GrayImage;
import NeuralNetwork.Utils.Matrix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextArea mainTextArea;

    @FXML
    private LineChart<Double, Double> graph;

    @FXML
    private ScatterChart<Number, Number> matrixGraph;

    @FXML
    private ImageView inputImageView;

    ObservableList<XYChart.Series<Double, Double>> lineChartData;
    LineChart.Series<Double, Double> series1;

    GrayImage inputImage;


    public MainController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
        // Plots
        lineChartData = FXCollections.observableArrayList();
        graph.setData(lineChartData);
        graph.setTitle("Training instances");
        graph.createSymbolsProperty();
        series1 = new LineChart.Series<Double, Double>();
        series1.setName("Training set");
        lineChartData.add(series1);

        // Images
        inputImage  = new GrayImage("res/plane.jpeg");
    }
    @FXML
    private void train() {
        mainTextArea.appendText("Starting learn process..\n");
        run();
    }


    @FXML
    private void test() {
        Image image = SwingFXUtils.toFXImage(inputImage.getBufferedImage(), null);
        inputImageView.setImage(image);
        mainTextArea.appendText("Not implemented yet..\n");
        TransferFunction tf = new Sigmoid();
        MaxPoolingLayer MPLayer = new MaxPoolingLayer(new int[]{8, 8}, new int[]{4, 4}, tf);
    }

    private void run() {
        TransferFunction tf = new Sigmoid();
        Layer[] layers = new Layer[]{
                new FullyConnectedLayer(new int[]{0, 0}, new int[]{6, 6}, tf),
                new FullyConnectedLayer(new int[]{6, 6}, new int[]{6, 6}, tf),
                new MaxPoolingLayer(new int[]{6, 6}, new int[]{3, 3}, tf),
                new FullyConnectedLayer(new int[]{3, 3}, new int[]{1, 1}, tf)
        };

        NeuralNetwork net = new NeuralNetwork(layers, 0.01);

		/* Learning */
        for (int i = 0; i < 100000; i++) {
            //mainTextArea.appendText("\nIteration " + i);
            if(i % 5000 == 0) {
                System.out.println("Iteration " + i);
            }
            double[][] inputs = new double[][]{
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())}
            };
            double[][] _inputs = new double[][]{
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())},
                    {Math.round(Math.random()), Math.round(Math.random()), Math.round(Math.random())}
            };
            double[][] output = new double[1][1];
            double error;

            output[0][0] = generateOutput(inputs);

            error = net.backPropagate(inputs, output);
            //System.out.println("\nError at step " + i + " is " + error + "\n");
            if(i % 200 == 0) {
                series1.getData().add(new XYChart.Data<Double, Double>(Double.valueOf(i), error));
                if(error > .9) {
                    //System.out.println(Matrix.format(inputs));
                    //System.out.println("Output ("+i+"): " + output[0][0] + ", error: " + error);
                }
            }
        }

        mainTextArea.appendText("Learning completed!\n");

		/* Test */
        /*
        double[][] inputs = new double[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 1}
        };
        double[][] output = net.execute(inputs);
        mainTextArea.appendText("Test result: " + Math.round(output[0][0]) + " (" + output[0][0] + ")\n");

        */
    }

    private double generateOutput(double[][] input) {
        int count = 0;
        int current = 0;
        for(int i = 0; i < input.length / 2; ++i) {
            for(int j = 0; j < input.length; ++j) {
                ++count;
                if(input[i][j] == 1)
                    ++current;
            }
        }
        return (current / count > 0.9) ? 1.0 : 0.0;
    }
}
