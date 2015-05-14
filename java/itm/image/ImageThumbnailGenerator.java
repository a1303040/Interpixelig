package itm.image;

/**
 * ****************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 * *****************************************************************************
 */

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class converts images of various formats to PNG thumbnails files.
 * It can be called with 3 parameters, an input filename/directory, an output directory and a compression quality parameter.
 * It will read the input image(s), grayscale and scale it/them and convert it/them to a PNG file(s) that is/are written to the output directory.
 * <p>
 * If the input file or the output directory do not exist, an exception is thrown.
 */
public class ImageThumbnailGenerator {

    /**
     * Constructor.
     */
    public ImageThumbnailGenerator() {
    }

    /**
     * Processes an image directory in a batch process.
     *
     * @param input     a reference to the input image file
     * @param output    a reference to the output directory
     * @param rotation
     * @param overwrite indicates whether existing thumbnails should be overwritten or not
     * @return a list of the created files
     */
    public ArrayList<File> batchProcessImages(File input, File output, double rotation, boolean overwrite) throws IOException {
        if (!input.exists()) {
            throw new IOException("Input file " + input + " was not found!");
        }
        if (!output.exists()) {
            throw new IOException("Output directory " + output + " not found!");
        }
        if (!output.isDirectory()) {
            throw new IOException(output + " is not a directory!");
        }

        ArrayList<File> ret = new ArrayList<File>();

        if (input.isDirectory()) {
            File[] files = input.listFiles();
            for (File f : files) {
                try {
                    File result = processImage(f, output, rotation, overwrite);
                    System.out.println("converted " + f + " to " + result);
                    ret.add(result);
                } catch (Exception e0) {
                    System.err.println("Error converting " + input + " : " + e0.toString());
                }
            }
        } else {
            try {
                File result = processImage(input, output, rotation, overwrite);
                System.out.println("converted " + input + " to " + result);
                ret.add(result);
            } catch (Exception e0) {
                System.err.println("Error converting " + input + " : " + e0.toString());
            }
        }
        return ret;
    }

    /**
     * Processes the passed input image and stores it to the output directory.
     * This function should not do anything if the outputfile already exists and if the overwrite flag is set to false.
     *
     * @param input     a reference to the input image file
     * @param output    a reference to the output directory
     * @param dimx      the width of the resulting thumbnail
     * @param dimy      the height of the resulting thumbnail
     * @param overwrite indicates whether existing thumbnails should be overwritten or not
     */
    protected File processImage(File input, File output, double rotation, boolean overwrite) throws IOException, IllegalArgumentException {
        if (!input.exists()) {
            throw new IOException("Input file " + input + " was not found!");
        }
        if (input.isDirectory()) {
            throw new IOException("Input file " + input + " is a directory!");
        }
        if (!output.exists()) {
            throw new IOException("Output directory " + output + " not found!");
        }
        if (!output.isDirectory()) {
            throw new IOException(output + " is not a directory!");
        }

        // create outputfilename and check whether thumb already exists
        File outputFile = new File(output, input.getName() + ".thumb.png");
        if (outputFile.exists()) {
            if (!overwrite) {
                return outputFile;
            }
        }

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // load the input image
        BufferedImage img = ImageIO.read(input);
        if (img == null)
            throw new IOException("Input image " + input + " is corrupt or not a supported image type!");

        // rotate by the given parameter the image - do not crop image parts!
        if (img.getWidth() < img.getHeight()) {
            AffineTransform affineTransform = new AffineTransform();

            affineTransform.translate(img.getHeight() / 2, img.getWidth() / 2);
            affineTransform.rotate(Math.PI / 2);
            affineTransform.translate(-img.getWidth() / 2, -img.getHeight() / 2);

            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);

            BufferedImage tmpImage = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB);
            affineTransformOp.filter(img, tmpImage);

            img = tmpImage;
        }

        // scale the image to a maximum width of 200 pixels - do not distort!
        if (img.getWidth() > 200) {
            double scalingRatio = 200 / (double) img.getWidth();
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(scalingRatio, scalingRatio);
            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
            img = affineTransformOp.filter(img, null);
        }

        // if the image is smaller than [ 200 w X 100 h ] - print it on a [ dim X dim ] canvas!
        if (img.getWidth() < 200 || img.getHeight() < 100) {

            BufferedImage tmpIMG = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
            tmpIMG.getGraphics().drawImage(img, (200 - img.getWidth()) / 2, (100 - img.getHeight()) / 2, null);

            img = tmpIMG;
        }

        // rotate your image by the given rotation parameter
        BufferedImage newIMG = img;
        AffineTransform affineTransform = new AffineTransform();

        affineTransform.setToRotation(Math.toRadians(rotation), img.getWidth() / 2, img.getHeight() / 2);

        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage = new BufferedImage(newIMG.getWidth(), newIMG.getHeight(), newIMG.getType());
        affineTransformOp.filter(newIMG, newImage);

        // save as extra file - say: don't return as output file
        File outputFileRotated = new File(output, input.getName() + ".thumb.rotated.png");
        ImageIO.write(newImage, "png", outputFileRotated);

        // encode and save the image
        ImageIO.write(img, "png", outputFile);

        return outputFile;

        /**
         ./ant.sh ImageThumbnailGenerator -Dinput=media/img/ -Doutput=test/ -Drotation=90
         */
    }

    /**
     * Main method. Parses the commandline parameters and prints usage information if required.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("usage: java itm.image.ImageThumbnailGenerator <input-image> <output-directory> <rotation degree>");
            System.out.println("usage: java itm.image.ImageThumbnailGenerator <input-directory> <output-directory> <rotation degree>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        double rotation = Double.parseDouble(args[2]);

        ImageThumbnailGenerator itg = new ImageThumbnailGenerator();
        itg.batchProcessImages(fi, fo, rotation, true);
    }
}