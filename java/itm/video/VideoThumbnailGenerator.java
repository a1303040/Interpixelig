package itm.video;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import com.xuggle.xuggler.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class creates Thumbnails from video files
 *
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
	 * Processes a video file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the video file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing output files should be overwritten
	 *            or not
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




    //TODO default of 1
    public static final double SECONDS_BETWEEN_FRAMES = 1;
    public static final long NANO_SECONDS_BETWEEN_FRAMES =
            (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
    private static long mLastPtsWrite = Global.NO_PTS;




	/**
	 * Processes the passed input video file and stores a thumbnail of it to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input video file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing files should be overwritten or not
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

		// create output file and check whether it already exists.
		File outputFile = new File(output, input.getName() + "_thumb.swf");

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// extract frames from input video

		// create a video writer

		// add a stream with the proper width, height and frame rate

		// if timespan is set to zero, compare the frames to use and add
		// only frames with significant changes to the final video

		// loop: get the frame image, encode the image to the video stream

		// Close the writer


        /**
         * !!!
         * !!!
         * source https://github.com/artclarke/xuggle-xuggler/blob/master/src/com/xuggle/xuggler/demos/DecodeAndCaptureFrames.java
         * !!!
         * !!!
         */
        String filename = input.getAbsolutePath();

            // make sure that we can actually convert video pixel formats

            if (!IVideoResampler.isSupported(
                    IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
                throw new RuntimeException(
                        "you must install the GPL version of Xuggler (with IVideoResampler" +
                                " support) for this demo to work");

            // create a Xuggler container object

            IContainer container = IContainer.make();

            // open up the container

            if (container.open(filename, IContainer.Type.READ, null) < 0)
                throw new IllegalArgumentException("could not open file: " + filename);

            // query how many streams the call to open found

            int numStreams = container.getNumStreams();

            // and iterate through the streams to find the first video stream

            int videoStreamId = -1;
            IStreamCoder videoCoder = null;
            for(int i = 0; i < numStreams; i++)
            {
                // find the stream object

                IStream stream = container.getStream(i);

                // get the pre-configured decoder that can decode this stream;

                IStreamCoder coder = stream.getStreamCoder();

                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
                {
                    videoStreamId = i;
                    videoCoder = coder;
                    break;
                }
            }

            if (videoStreamId == -1)
                throw new RuntimeException("could not find video stream in container: "+filename);

            // Now we have found the video stream in this file.  Let's open up
            // our decoder so it can do work

            if (videoCoder.open() < 0)
                throw new RuntimeException(
                        "could not open video decoder for container: " + filename);

            IVideoResampler resampler = null;
            if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
            {
                // if this stream is not in BGR24, we're going to need to
                // convert it.  The VideoResampler does that for us.

                resampler = IVideoResampler.make(
                        videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24,
                        videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
                if (resampler == null)
                    throw new RuntimeException(
                            "could not create color space resampler for: " + filename);
            }

            // Now, we start walking through the container looking at each packet.

            IPacket packet = IPacket.make();
            while(container.readNextPacket(packet) >= 0)
            {

                // Now we have a packet, let's see if it belongs to our video strea

                if (packet.getStreamIndex() == videoStreamId)
                {
                    // We allocate a new picture to get the data out of Xuggle

                    IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
                            videoCoder.getWidth(), videoCoder.getHeight());

                    int offset = 0;
                    while(offset < packet.getSize())
                    {
                        // Now, we decode the video, checking for any errors.

                        int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                        if (bytesDecoded < 0)
                            throw new RuntimeException("got error decoding video in: " + filename);
                        offset += bytesDecoded;

                        // Some decoders will consume data in a packet, but will not
                        // be able to construct a full video picture yet.  Therefore
                        // you should always check if you got a complete picture from
                        // the decode.

                        if (picture.isComplete())
                        {
                            IVideoPicture newPic = picture;

                            // If the resampler is not null, it means we didn't get the
                            // video in BGR24 format and need to convert it into BGR24
                            // format.

                            if (resampler != null)
                            {
                                // we must resample
                                newPic = IVideoPicture.make(
                                        resampler.getOutputPixelFormat(), picture.getWidth(),
                                        picture.getHeight());
                                if (resampler.resample(newPic, picture) < 0)
                                    throw new RuntimeException(
                                            "could not resample video from: " + filename);
                            }

                            if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
                                throw new RuntimeException(
                                        "could not decode video as BGR 24 bit data in: " + filename);

                            // convert the BGR24 to an Java buffered image

                            BufferedImage javaImage = Utils.videoPictureToImage(newPic);

                            // process the video frame

                            processFrame(newPic, javaImage, input, output);
                        }
                    }
                }
                else
                {
                    // This packet isn't part of our video stream, so we just
                    // silently drop it.
                    do {} while(false);
                }
            }

            // Technically since we're exiting anyway, these will be cleaned up
            // by the garbage collector... but because we're nice people and
            // want to be invited places for Christmas, we're going to show how
            // to clean up.

            if (videoCoder != null)
            {
                videoCoder.close();
                videoCoder = null;
            }
            if (container !=null)
            {
                container.close();
                container = null;
            }

            //reset our counter
            mLastPtsWrite = Global.NO_PTS;

		return outputFile;
	}


    private static void processFrame(IVideoPicture picture, BufferedImage image, File input, File output)
    {
        try
        {
            // if uninitialized, backdate mLastPtsWrite so we get the very
            // first frame

            if (mLastPtsWrite == Global.NO_PTS)
                mLastPtsWrite = picture.getPts() - NANO_SECONDS_BETWEEN_FRAMES;

            // if it's time to write the next frame

            if (picture.getPts() - mLastPtsWrite >= NANO_SECONDS_BETWEEN_FRAMES)
            {
                // Make a temorary file name
                double seconds = ((double)picture.getPts()) / Global.DEFAULT_PTS_PER_SECOND;
                String secondsstring = Double.toString(seconds);
                if(seconds < 10){
                    secondsstring = "0" + secondsstring;
                }
                File file = new File(output, input.getName() + secondsstring + "_thumb.png");

                // write out PNG

                ImageIO.write(image, "png", file);

                // indicate file written

                System.out.printf("at elapsed time of %6.3f seconds wrote: %s\n",
                        seconds, file);

                // update last write time

                mLastPtsWrite += NANO_SECONDS_BETWEEN_FRAMES;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        if(args.length == 3)
            timespan = Integer.parseInt(args[2]);
        
        VideoThumbnailGenerator videoMd = new VideoThumbnailGenerator();
        videoMd.batchProcessVideoFiles(fi, fo, true, timespan);
	}
}