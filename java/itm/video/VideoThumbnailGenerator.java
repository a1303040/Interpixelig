package itm.video;

/*******************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 *******************************************************************************/

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IRational;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class creates Thumbnails from video files
 * <p>
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class VideoThumbnailGenerator {

    /**
     * Constructor.
     */
    public VideoThumbnailGenerator() {
    }

    /**
     * Main method. Parses the commandline parameters and prints usage
     * information if required.
     */
    public static void main(String[] args) throws Exception {

        if (args.length < 3) {
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-video> <output-directory> <timespan>");
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-directory> <output-directory> <timespan>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        int timespan = 5;
        if (args.length == 3)
            timespan = Integer.parseInt(args[2]);

        VideoThumbnailGenerator videoMd = new VideoThumbnailGenerator();
        videoMd.batchProcessVideoFiles(fi, fo, true, timespan);
    }

    /**
     * Processes a video file directory in a batch process.
     *
     * @param input     a reference to the video file directory
     * @param output    a reference to the output directory
     * @param overwrite indicates whether existing output files should be overwritten
     *                  or not
     * @return a list of the created media objects (videos)
     */
    public ArrayList<File> batchProcessVideoFiles(File input, File output, boolean overwrite, int timespan) throws IOException {
        if (!input.exists())
            throw new IOException("Input file " + input + " was not found!");
        if (!output.exists())
            throw new IOException("Output directory " + output + " not found!");
        if (!output.isDirectory())
            throw new IOException(output + " is not a directory!");

        ArrayList<File> ret = new ArrayList<File>();

        if (input.isDirectory()) {
            File[] files = input.listFiles();
            for (File f : files) {

                String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
                if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
                        || ext.equals("mp4"))
                    try {
                        File result = processVideo(f, output, overwrite, timespan);
                        System.out.println("processed file " + f + " to " + output);
                        ret.add(result);
                    } catch (Exception e0) {
                        System.err.println("Error processing file " + input + " : " + e0.toString());

                        StringWriter errors = new StringWriter();
                        e0.printStackTrace(new PrintWriter(errors));
                        System.out.println(errors.toString());
                    }
            }
        } else {

            String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
            if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4"))
                try {
                    File result = processVideo(input, output, overwrite, timespan);
                    System.out.println("processed " + input + " to " + result);
                    ret.add(result);
                } catch (Exception e0) {
                    System.err.println("Error when creating processing file " + input + " : " + e0.toString());
                }

        }
        return ret;
    }

    /**
     * Processes the passed input video file and stores a thumbnail of it to the
     * output directory.
     *
     * @param input     a reference to the input video file
     * @param output    a reference to the output directory
     * @param overwrite indicates whether existing files should be overwritten or not
     * @return the created video media object
     */
    protected File processVideo(File input, File output, boolean overwrite, int timespan) throws Exception {
        if (!input.exists())
            throw new IOException("Input file " + input + " was not found!");
        if (input.isDirectory())
            throw new IOException("Input file " + input + " is a directory!");
        if (!output.exists())
            throw new IOException("Output directory " + output + " not found!");
        if (!output.isDirectory())
            throw new IOException(output + " is not a directory!");

        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        File outputFile = new File(output, input.getName() + "_thumb.swf");
        if (outputFile.exists() && !overwrite) {
            return null;
        }

        // extract frames from input video
        // if timespan is set to zero, compare the frames to use and add
        // only frames with significant changes to the final video
        List<BufferedImage> frames = VideoFramesExtractor.getFrames(input, timespan);

        if (!frames.isEmpty()) {
            // create a video writer
            IMediaWriter writer = ToolFactory.makeWriter(outputFile.getAbsolutePath());

            // add a stream with the proper width, height and frame rate
            final int videoStreamIndex = 0;
            final int ourVideoStreamId = 0;
            // 1 frame per second
            final IRational fps = IRational.make(1, 1);
            writer.addVideoStream(videoStreamIndex, ourVideoStreamId, fps, frames.get(0).getWidth(), frames.get(0).getHeight());

            // loop: get the frame image, encode the image to the video stream
            int curFrame = 0;
            for (BufferedImage image : frames) {
                writer.encodeVideo(0, image, curFrame++, TimeUnit.SECONDS);
            }

            // Close the writer
            if (writer.isOpen()) {
                writer.flush();
                writer.close();
            }
        }
        return output;
    }
}