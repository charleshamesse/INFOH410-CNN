package infoh410;
import NeuralNetwork.*;
import NeuralNetwork.Layers.*;
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
        series1 = new LineChart.Series<>();
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
        inputImage.subSample(50, 50);
        Image image = SwingFXUtils.toFXImage(inputImage.getBufferedImage(), null);
        inputImageView.setImage(image);
        mainTextArea.appendText("Not implemented yet..\n");
    }

    private void run() {
        TransferFunction tf = new Sigmoid();
        Layer topLayer = new FullyConnectedLayer(new int[]{0, 0}, new int[]{20, 20}, tf);
        Layer midLayer = new MaxPoolingLayer(new int[]{20, 20}, new int[]{10, 10}, tf);
        Layer bottomLayer = new FullyConnectedLayer(new int[]{10, 10}, new int[]{1, 1}, tf);
        Layer[] layers = new Layer[]{
                topLayer,
                midLayer,
                bottomLayer
        };

        NeuralNetwork net = new NeuralNetwork(layers, 0.01);

		/* Learning */
        for (int i = 0; i < 2000; i++) {
            double[][] inputs = new double[20][20];
            Matrix.initMat(inputs);
            double[][] output = new double[1][1];
            double error;

            output[0][0] = generateOutput(inputs);

            error = net.backPropagate(inputs, output);
            //System.out.println("\nError at step " + i + " is " + error + "\n");
            if(i % 200 == 0) {
                series1.getData().add(new XYChart.Data<>((double)i, error));
                GrayImage layerImage = new GrayImage(bottomLayer.getNeuronValues());
                layerImage.superSample(200, 200);
                Image image = SwingFXUtils.toFXImage(layerImage.getBufferedImage(), null);
                inputImageView.setImage(image);
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
