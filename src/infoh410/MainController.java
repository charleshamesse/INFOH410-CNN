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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
    private HBox imageViewBox;

    @FXML
    private TextField imageViewCaption;

    ObservableList<XYChart.Series<Double, Double>> lineChartData;
    LineChart.Series<Double, Double> series1;
    GrayImage inputImage;
    NeuralNetwork net;


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

        // NeuralNetwork

        TransferFunction tf = new Sigmoid();

        Layer[] layers = new Layer[]{
                new FullyConnectedLayer(new int[]{0, 0}, new int[]{20, 20}, tf),
                new ConvolutionalLayer(new int[]{20, 20}, new int[]{20, 20}, tf),
                new MaxPoolingLayer(new int[]{20, 20}, new int[]{10, 10}, tf),
                new FullyConnectedLayer(new int[]{10, 10}, new int[]{1, 1}, tf)
        };

        net = new NeuralNetwork(layers, 0.01);
    }
    @FXML
    private void train() {
        mainTextArea.appendText("Starting learn process..\n");
        trainNetwork(net);
    }


    @FXML
    private void test() {
        mainTextArea.appendText("Starting test process..\n");
        testNetwork(net);
    }

    private void trainNetwork(NeuralNetwork net) {

		/* Learning */
        for (int i = 0; i < 5000; i++) {
            double[][] inputs = new double[20][20];
            Matrix.initMat(inputs);
            double[][] output = new double[1][1];
            double error;

            output[0][0] = generateOutput(inputs);

            error = net.backPropagate(inputs, output);
            if(i % 200 == 0) {
                series1.getData().add(new XYChart.Data<>((double)i, error));
            }
            if(i % 1000 == 0) {
                System.out.println("Iteration " + i);
            }
        }

        mainTextArea.appendText("Learning completed!\n");

    }

    private void testNetwork(NeuralNetwork net) {

        double[][] inputs = new double[20][20];
        Matrix.initMat(inputs);

        double[][] output = net.execute(inputs);

        mainTextArea.appendText("Test result: " + output[0][0] + "\n");
        imageViewBox.getChildren().clear();

        imageViewCaption.setText("Layers: ");

        // Plot layer neurons
        for(Layer l : net.getLayers()) {
            GrayImage layerImage = new GrayImage(l.getNeuronValues());
            layerImage.superSample(200, 200);
            Image image = SwingFXUtils.toFXImage(layerImage.getBufferedImage(), null);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageViewBox.getChildren().add(imageView);
            imageViewCaption.appendText(l.getClass().getName() + " ");
        }
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
