package itm.audio;

/**
 * ****************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 * *****************************************************************************
 */


import com.sun.media.sound.WaveFileWriter;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

        ArrayList<File> ret = new ArrayList<>();

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

        // sources
        // http://stackoverflow.com/questions/7546010/obtaining-an-audioinputstream-upto-some-x-bytes-from-the-original-cutting-an-au
        // http://stackoverflow.com/questions/23701339/converting-ogg-to-wav-doesnt-work-although-listed-as-available-format
        // http://www.java-forum.org/allgemeine-java-themen/111606-pcm-wav-schreiben-bzw-ogg-vorbis-wav.html

        // load the input audio file

        AudioInputStream in = null;
        try {
            in = AudioSystem.getAudioInputStream(input);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        AudioFormat format = in.getFormat();
        AudioFormat originalFormat = format;

        // transcode
        AudioFormat pcm = new AudioFormat(format.getSampleRate(), 16, format.getChannels(), true, false);
        AudioInputStream audioInputStream;
        audioInputStream = AudioSystem.getAudioInputStream(pcm, in);
        format = audioInputStream.getFormat();

        // cut the audio data in the stream to a given length

        long framesOfAudioToCopy = thumbNailLength * (int) format.getFrameRate();
        AudioInputStream shortenedStream = new AudioInputStream(audioInputStream, format, framesOfAudioToCopy);

        // save the acoustic thumbnail as WAV file

        WaveFileWriter writer = new WaveFileWriter();
        writer.write(shortenedStream, AudioFileFormat.Type.WAVE, outputFile);

        // only for OGG
        if (originalFormat.getEncoding().toString().toLowerCase().contains("vorbis")) {
            final byte[] buffer = new byte[4096];
            int n;

            //use a temporary file...
            File output_temp = new File(output, input.getName() + ".wav" + "pcm.temp");
            final FileOutputStream fos = new FileOutputStream(output_temp);

            while (-1 != (n = audioInputStream.read(buffer))) {
                fos.write(buffer, 0, n);
            }
            fos.close();

            // cut to length
            final AudioInputStream pcmIn = new AudioInputStream(new FileInputStream(output_temp), pcm, 44100 * thumbNailLength);
            AudioSystem.write(pcmIn, AudioFileFormat.Type.WAVE, outputFile);

            output_temp.delete();
        }

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
