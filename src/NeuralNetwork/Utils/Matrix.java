package NeuralNetwork.Utils;

import NeuralNetwork.Neuron;

/**
 * Created by charleshamesse on 17/05/16.
 */
public class Matrix {
    public Matrix() {

    }
    public static String format(Double[][] a) {
        int row, column;
        String aString = "";
        for (row = 0; row < a.length; row++) {
            aString = aString + "\n";
            if (a[row] != null && a[row].length > 0) {
                aString = aString + a[row][0];

                for (column = 1; column < a[row].length; column++) {
                    aString = aString + " " + a[row][column];
                }
            }
        }
        return aString;
    }
    public static String format(double[][] a) {
        int row, column;
        String aString = "";
        for (row = 0; row < a.length; row++) {
            aString = aString + "\n";
            if (a[row] != null && a[row].length > 0) {
                aString = aString + a[row][0];

                for (column = 1; column < a[row].length; column++) {
                    aString = aString + " " + a[row][column];
                }
            }
        }
        return aString;
    }
    public static String format(Neuron[][] a) {
        int row, column;
        String aString = "";
        for (row = 0; row < a.length; row++) {
            aString = aString + "\n";
            if (a[row] != null && a[row].length > 0) {
                aString = aString + a[row][0].value;

                for (column = 1; column < a[row].length; column++) {
                    aString = aString + " " + a[row][column].value;
                }
            }
        }
        return aString;
    }
}
