package itm.audio;

/**
 * ****************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 * *****************************************************************************
 */


import com.sun.media.sound.WaveFileWriter;
import javazoom.jl.converter.WaveFile;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class creates acoustic thumbnails from various types of audio files. It
 * can be called with 3 parameters, an input filename/directory and an output
 * directory, and the desired length of the thumbnail in seconds. It will read
 * MP3 or OGG encoded input audio files(s), cut the contained audio data to a
 * given length (in seconds) and saves the acoustic thumbnails to a certain
 * length.
 * <p>
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */

public class AudioThumbGenerator {

    private int thumbNailLength = 10; // 10 sec default

    /**
     * Constructor.
     */
    public AudioThumbGenerator(int thumbNailLength) {
        this.thumbNailLength = thumbNailLength;
    }

    /**
     * Processes the passed input audio file / audio file directory and stores
     * the processed files to the output directory.
     *
     * @param input  a reference to the input audio file / input directory
     * @param output a reference to the output directory
     */
    public ArrayList<File> batchProcessAudioFiles(File input, File output)
            throws IOException {
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

                String ext = f.getName().substring(
                        f.getName().lastIndexOf(".") + 1).toLowerCase();
                if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
                    try {
                        File result = processAudio(f, output);
                        System.out.println("converted " + f + " to " + result);
                        ret.add(result);
                    } catch (Exception e0) {
                        System.err.println("Error converting " + f + " : "
                                + e0.toString());
                    }

                }

            }
        } else {
            String ext = input.getName().substring(
                    input.getName().lastIndexOf(".") + 1).toLowerCase();
            if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
                try {
                    File result = processAudio(input, output);
                    System.out.println("converted " + input + " to " + result);
                    ret.add(result);
                } catch (Exception e0) {
                    System.err.println("Error converting " + input + " : "
                            + e0.toString());
                }

            }

        }
        return ret;
    }

    /**
     * Processes the passed audio file and stores the processed file to the
     * output directory.
     *
     * @param input  a reference to the input audio File
     * @param output a reference to the output directory
     */
    protected File processAudio(File input, File output) throws IOException,
            IllegalArgumentException {
        if (!input.exists())
            throw new IOException("Input file " + input + " was not found!");
        if (input.isDirectory())
            throw new IOException("Input file " + input + " is a directory!");
        if (!output.exists())
            throw new IOException("Output directory " + output + " not found!");
        if (!output.isDirectory())
            throw new IOException(output + " is not a directory!");

        File outputFile = new File(output, input.getName() + ".wav");


        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        // load the input audio file

        // cut the audio data in the stream to a given length

        // save the acoustic thumbnail as WAV file


        // from http://docs.oracle.com/javase/tutorial/sound/converters.html

        // TODO support ogg
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(input);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.out.println(e.getStackTrace());
        }
        AudioFormat format = audioInputStream.getFormat();
        // Try to read numBytes bytes from the file.
        AudioFormat pcm =
                new AudioFormat(format.getSampleRate(), 16,
                        format.getChannels(), true, false);
        // Get a wrapper stream around the input stream that does the
        // transcoaudioInputStreamg for us.
        audioInputStream = AudioSystem.getAudioInputStream(pcm, audioInputStream);
        // Update the format and info variables for the transcoded data
        format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        int totalFramesRead = 0;
        int bytesPerFrame =
                audioInputStream.getFormat().getFrameSize();
        if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
            // some audio formats may have unspecified frame size
            // in that case we may read any amount of bytes
            bytesPerFrame = 1;
        }

        // Set an arbitrary buffer size of 1024 frames.
        int numBytes = 1024 * bytesPerFrame;
        byte[] audioBytes = new byte[numBytes];

        int numBytesRead = 0;
        int numFramesRead = 0;

        //from http://stackoverflow.com/questions/7546010/obtaining-an-audioinputstream-upto-some-x-bytes-from-the-original-cutting-an-au

            //AudioInputStream inputStream = null;
            AudioInputStream shortenedStream = null;
            //AudioFileFormat fileFormat = null;

                //fileFormat = AudioSystem.getAudioFileFormat(input);
                //format = fileFormat.getFormat();
                int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
                //inputStream.skip(startSecond * bytesPerSecond);
                long framesOfAudioToCopy = thumbNailLength * (int) format.getFrameRate();
                shortenedStream = new AudioInputStream(audioInputStream, format, framesOfAudioToCopy);
                //File destinationFile = new File(outputFile);
                //AudioSystem.write(shortenedStream, fileFormat.getType(), outputFile);

                pcm = new AudioFormat(format.getSampleRate(), 16,
                        format.getChannels(), true, false);
                // Get a wrapper stream around the input stream that does the
                // transcoaudioInputStreamg for us.
                shortenedStream = AudioSystem.getAudioInputStream(pcm, shortenedStream);
                // Update the format and info variables for the transcoded data


                WaveFileWriter writer = new WaveFileWriter();
                writer.write(shortenedStream, AudioFileFormat.Type.WAVE, outputFile);




        //FileOutputStream fos = new FileOutputStream(outputFile);
        //fos.write(audioBytes);
        //fos.close();


        return outputFile;
    }

    /**
     * Main method. Parses the commandline parameters and prints usage
     * information if required.
     */
    public static void main(String[] args) throws Exception {

        args = new String[]{"./media/audio", "./test", "10"};

        if (args.length < 3) {
            System.out
                    .println("usage: java itm.audio.AudioThumbGenerator <input-audioFile> <output-directory> <length>");
            System.out
                    .println("usage: java itm.audio.AudioThumbGenerator <input-directory> <output-directory> <length>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        Integer length = new Integer(args[2]);
        AudioThumbGenerator audioThumb = new AudioThumbGenerator(length.intValue());
        audioThumb.batchProcessAudioFiles(fi, fo);
    }

}
