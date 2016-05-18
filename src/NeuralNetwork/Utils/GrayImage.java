package NeuralNetwork.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by charleshamesse on 18/05/16.
 */
public class GrayImage {

    private String path;
    private BufferedImage img;
    private int[][] values;

    public GrayImage(String path) {
        this.path = path;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println(e);
        }
        getArray();
    }


    public int[][] getArray() {
        int[][] arr = new int[img.getHeight()][img.getWidth()];
        int rgb, r, g, b;

        for(int i = 0; i < arr.length; i++)
            for(int j = 0; j < arr[0].length; j++) {
                rgb = img.getRGB(i, j);
                r = (rgb >> 16) & 0xFF;
                g = (rgb >> 8) & 0xFF;
                b = rgb & 0xFF;

                // NTSC conversion
                arr[i][j] = (int) Math.round(0.2989*r + 0.5870*g + 0.1140*b);
            }
        values = arr;
        return arr;
    }

    public BufferedImage getBufferedImage() {
        int xLength = this.values.length;
        int yLength = this.values[0].length;
        BufferedImage b = new BufferedImage(xLength, yLength, 3);
        for(int x = 0; x < xLength; x++) {
            for(int y = 0; y < yLength; y++) {
                Color gray = new Color(values[x][y], values[x][y], values[x][y]);
                b.setRGB(x, y, gray.getRGB());
            }
        }
        return b;

    }

}
