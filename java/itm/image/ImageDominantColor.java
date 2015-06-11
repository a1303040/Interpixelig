package itm.image;

import itm.util.Histogram;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by emanuelstadler on 6/6/15.
 */

/**
 * TODO look into k means clustering
 * https://charlesleifer.com/blog/using-python-and-k-means-to-find-the-dominant-colors-in-images/
 */
public class ImageDominantColor {

    // TODO this is really similar to our histogram generator
    public ArrayList<String> getDominantColors(File input) throws IOException, IllegalArgumentException {
        // load the input image
        BufferedImage img = null;
        img = ImageIO.read(input);
        if (img == null)
            throw new IOException("Input image " + input + " is corrupt or not a supported image type!");

        // get the color model of the image and the amount of color components
        ColorModel colorModel = img.getColorModel();
        int numColorComponent = colorModel.getNumColorComponents();

        // counters
        int redcount = 0;
        int greencount = 0;
        int bluecount = 0;

        BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, null);

        int numColorComponents = image.getColorModel().getNumColorComponents();
        int colorSpaceType = image.getColorModel().getColorSpace().getType();

        // in java, the array is default initialized to zero
        // read pixel values
        // see http://stackoverflow.com/questions/10880083/get-rgb-of-a-bufferedimage
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color c = new Color(image.getRGB(j, i));

                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                // white black or gray
                if(red == green && red == blue){
                    //jump to the next iteration
                    continue;
                }
                // if getRed biggest value
                if(red >= green && red >= blue ){
                    redcount++;
                }
                // if getGreen biggest value,
                if(green >= red && green >= blue ) {
                    greencount++;
                }
                // if getBlue biggest value,
                if(blue >= red && blue >= green) {
                    bluecount++;
                }
                // histArray[3][c.getAlpha()]++;
            }
        }

        // now we look at our counts
        // TODO multiple assignments

        ArrayList<String> colors = new ArrayList<>();

        // if getRed biggest value
        // ignore grayscale
        if(redcount != 0 && greencount != 0 && bluecount != 0) {
            if (redcount >= greencount && redcount >= bluecount) {
                colors.add("red");
            }
            // if getGreen biggest value,
            if (greencount >= redcount && greencount >= bluecount) {
                colors.add("green");
            }
            // if getBlue biggest value,
            if (bluecount >= redcount && bluecount >= greencount) {
                colors.add("blue");
            }
        }
        return colors;
    }
}
