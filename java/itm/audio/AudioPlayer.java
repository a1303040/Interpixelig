package itm.audio;

/*******************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 *******************************************************************************/

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Plays an audio file using the system's default sound output device
 */
public class AudioPlayer {

    /**
     * Constructor
     */
    public AudioPlayer() {

    }

    /**
     * Plays audio data from a given input file to the system's default sound
     * output device
     *
     * @param input the audio file
     * @throws IOException general error when accessing audio file
     */
    protected void playAudio(File input) throws IOException {

        if (!input.exists())
            throw new IOException("Input file " + input + " was not found!");

        AudioInputStream audio = null;
        try {
            audio = openAudioInputStream(input);
        } catch (UnsupportedAudioFileException e) {
            throw new IOException("could not open audio file " + input
                    + ". Encoding / file format not supported");
        }

        try {
            rawplay(audio);
        } catch (LineUnavailableException e) {
            throw new IOException("Error when playing sound from file "
                    + input.getName() + ". Sound output device unavailable");
        }

        audio.close();

    }

    /**
     * Decodes an encoded audio file and returns a PCM input stream
     * <p>
     * Supported encodings: MP3, OGG (requires SPIs to be in the classpath)
     *
     * @param input a reference to the input audio file
     * @return a PCM AudioInputStream
     * @throws UnsupportedAudioFileException an audio file's encoding is not supported
     * @throws IOException                   general error when accessing audio file
     */
    private AudioInputStream openAudioInputStream(File input)
            throws UnsupportedAudioFileException, IOException {

        AudioInputStream din = null;

        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        // from http://archive.oreilly.com/onjava/excerpt/jenut3_ch17/examples/PlaySoundStream.java

        // open audio stream
        din = AudioSystem.getAudioInputStream(input);

        // get format
        AudioFormat format = din.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        // If format not supported directly
        if (!AudioSystem.isLineSupported(info)) {

            // get decoded format
            AudioFormat pcm = new AudioFormat(format.getSampleRate(), 16, format.getChannels(), true, false);

            // get decoded audio input stream
            din = AudioSystem.getAudioInputStream(pcm, din);
        }

        return din;
    }

    /**
     * Writes audio data from an AudioInputStream to a SourceDataline
     *
     * @param audio the audio data
     * @throws IOException              error when writing audio data to source data line
     * @throws LineUnavailableException system's default source data line is not available
     */
    private void rawplay(AudioInputStream audio) throws IOException,
            LineUnavailableException {


        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        // from http://jan.newmarch.name/LinuxSound/Sampled/JavaSound/

        // get audio format
        AudioFormat format = audio.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        // get a source data line
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(format);
        line.start();

        // read samples from audio and write them to the data line
        int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
        byte[] buffer = new byte[bufferSize];

        // Move the data until done or there is an error.
        try {
            int bytesRead;
            while ((bytesRead = audio.read(buffer, 0, buffer.length)) >= 0) {
                line.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // properly close the line!
        line.drain();
        line.close();
    }

    /**
     * Main method. Parses the commandline parameters and prints usage
     * information if required.
     */
    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out
                    .println("usage: java itm.audio.AudioPlayer <input-audioFile>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        AudioPlayer player = new AudioPlayer();
        player.playAudio(fi);
        System.exit(0);

    }

}
