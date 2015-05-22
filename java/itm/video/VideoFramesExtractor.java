package itm.video;

import com.xuggle.xuggler.*;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import itm.util.ImageCompare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VideoFramesExtractor {

    //default of 0
    public static long NANO_SECONDS_BETWEEN_FRAMES = 0;
    private static long mLastPtsWrite = Global.NO_PTS;
    private static IVideoPicture oldPic = null;

    static List<BufferedImage> getFrames(File input, int timespan) {

        ArrayList<BufferedImage> frames = new ArrayList<>();
        NANO_SECONDS_BETWEEN_FRAMES = Global.DEFAULT_PTS_PER_SECOND * timespan;

        // source https://github.com/artclarke/xuggle-xuggler/blob/master/src/com/xuggle/xuggler/demos/DecodeAndCaptureFrames.java
        String filename = input.getAbsolutePath();

        // make sure that we can actually convert video pixel formats
        if (!IVideoResampler.isSupported(
                IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
            throw new RuntimeException("you must install the GPL version of Xuggler (with IVideoResampler support) for this demo to work");

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
        for (int i = 0; i < numStreams; i++) {

            // find the stream object
            IStream stream = container.getStream(i);

            // get the pre-configured decoder that can decode this stream;
            IStreamCoder coder = stream.getStreamCoder();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                videoStreamId = i;
                videoCoder = coder;
                break;
            }
        }

        if (videoStreamId == -1)
            throw new RuntimeException("could not find video stream in container: " + filename);

        // Now we have found the video stream in this file.  Let's open up
        // our decoder so it can do work
        if (videoCoder.open(null, null) < 0)
            throw new RuntimeException(
                    "could not open video decoder for container: " + filename);

        IVideoResampler resampler = null;
        if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
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
        while (container.readNextPacket(packet) >= 0) {

            // Now we have a packet, let's see if it belongs to our video stream
            if (packet.getStreamIndex() == videoStreamId) {

                // We allocate a new picture to get the data out of Xuggle
                IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
                        videoCoder.getWidth(), videoCoder.getHeight());

                int offset = 0;

                while (offset < packet.getSize()) {

                    // Now, we decode the video, checking for any errors.
                    int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                    if (bytesDecoded < 0)
                        throw new RuntimeException("got error decoding video in: " + filename);
                    offset += bytesDecoded;

                    // Some decoders will consume data in a packet, but will not
                    // be able to construct a full video picture yet.  Therefore
                    // you should always check if you got a complete picture from
                    // the decode.
                    if (picture.isComplete()) {
                        IVideoPicture newPic = picture;

                        // If the resampler is not null, it means we didn't get the
                        // video in BGR24 format and need to convert it into BGR24
                        // format.
                        if (resampler != null) {
                            // we must resample
                            newPic = IVideoPicture.make(
                                    resampler.getOutputPixelFormat(), picture.getWidth(),
                                    picture.getHeight());
                            if (resampler.resample(newPic, picture) < 0)
                                throw new RuntimeException("could not resample video from: " + filename);
                        }

                        if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
                            throw new RuntimeException("could not decode video as BGR 24 bit data in: " + filename);

                        // convert the BGR24 to an Java buffered image
                        IConverter converter = ConverterFactory.createConverter(ConverterFactory.XUGGLER_BGR_24, newPic);
                        BufferedImage javaImage = converter.toImage(newPic);

                        if (timespan == 0) {
                            if (oldPic == null) {
                                // add to frame
                                frames.add(javaImage);
                            } else {
                                BufferedImage oldImage = converter.toImage(oldPic);
                                ImageCompare imgCompare = new ImageCompare(oldImage, javaImage);
                                imgCompare.setParameters(5, 5, 2, 10);
                                imgCompare.compare();

                                if (!imgCompare.match()) {
                                    // add to frame
                                    frames.add(javaImage);
                                }
                            }
                            oldPic = newPic;
                        } else {
                            //from http://www.xuggle.com/public/documentation/java/api/com/xuggle/mediatool/IMediaWriter.html
                            if (VideoFramesExtractor.acceptFrame(newPic.getPts())) {
                                frames.add(javaImage);
                            }
                        }
                    }
                }
            }
        }


        // Technically since we're exiting anyway, these will be cleaned up
        // by the garbage collector... but because we're nice people and
        // want to be invited places for Christmas, we're going to show how
        // to clean up.

        if (videoCoder != null) {
            videoCoder.close();
            videoCoder = null;
        }
        if (container != null) {
            container.close();
            container = null;
        }

        //reset our counter
        mLastPtsWrite = Global.NO_PTS;
        oldPic = null;

        return frames;
    }

    static boolean acceptFrame(long pts) {
        try {
            // if uninitialized, backdate mLastPtsWrite so we get the very
            // first frame
            if (mLastPtsWrite == Global.NO_PTS) {
                mLastPtsWrite = pts - NANO_SECONDS_BETWEEN_FRAMES;
            }

            if (pts - mLastPtsWrite >= NANO_SECONDS_BETWEEN_FRAMES) {
                // update last write time
                mLastPtsWrite += NANO_SECONDS_BETWEEN_FRAMES;

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}