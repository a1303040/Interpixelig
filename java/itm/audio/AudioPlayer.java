package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

/**
 * Plays an audio file using the system's default sound output device
 * 
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
	 * @param input
	 *            the audio file
	 * @throws IOException
	 *             general error when accessing audio file
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
	 * 
	 * Supported encodings: MP3, OGG (requires SPIs to be in the classpath)
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @return a PCM AudioInputStream
	 * @throws UnsupportedAudioFileException
	 *             an audio file's encoding is not supported
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	private AudioInputStream openAudioInputStream(File input)
			throws UnsupportedAudioFileException, IOException {

		AudioInputStream din = null;
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// open audio stream
		
		// get format
		
		// get decoded format
		
		// get decoded audio input stream


        // from http://archive.oreilly.com/onjava/excerpt/jenut3_ch17/examples/PlaySoundStream.java
        din=AudioSystem.getAudioInputStream(input);
        AudioFormat format = din.getFormat( );
        DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);

        // If the format is not supported directly (i.e. if it is not PCM
        // encoded), then try to transcode it to PCM.
        if (!AudioSystem.isLineSupported(info)) {
            // This is the PCM format we want to transcode to.
            // The parameters here are audio format details that you
            // shouldn't need to understand for casual use.
            AudioFormat pcm =
                    new AudioFormat(format.getSampleRate( ), 16,
                            format.getChannels( ), true, false);

            // Get a wrapper stream around the input stream that does the
            // transcoding for us.
            din = AudioSystem.getAudioInputStream(pcm, din);

            // Update the format and info variables for the transcoded data
            format = din.getFormat( );
            info = new DataLine.Info(SourceDataLine.class, format);
        }

        return din;
	}

	/**
	 * Writes audio data from an AudioInputStream to a SourceDataline
	 * 
	 * @param audio
	 *            the audio data
	 * @throws IOException
	 *             error when writing audio data to source data line
	 * @throws LineUnavailableException
	 *             system's default source data line is not available
	 */
	private void rawplay(AudioInputStream audio) throws IOException,
			LineUnavailableException {

		
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// get audio format
		
		// get a source data line
		
		// read samples from audio and write them to the data line 

		// properly close the line!


        // Open the line through which we'll play the streaming audio.
        AudioFormat format = audio.getFormat( );
        DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);

        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);

        line.start();

        //from http://jan.newmarch.name/LinuxSound/Sampled/JavaSound/

        // Create a buffer for moving data from the audio stream to the line.
        int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
        byte [] buffer = new byte[ bufferSize ];

        // Move the data until done or there is an error.
        try {
            int bytesRead = 0;
            while (bytesRead >= 0) {
                bytesRead = audio.read(buffer, 0, buffer.length);
                if (bytesRead >= 0) {
                    // System.out.println("Play.playAudioStream bytes read=" + bytesRead +
                    // ", frame size=" + audioFormat.getFrameSize() + ", frames read=" + bytesRead / audioFormat.getFrameSize());
                    // Odd sized sounds throw an exception if we don't write the same amount.
                    int framesWritten = line.write(buffer, 0, bytesRead);
                }
            } // while
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Play.playAudioStream draining line.");
        // Continues data line I/O until its buffer is drained.
        line.drain();

        System.out.println("Play.playAudioStream closing line.");
        // Closes the data line, freeing any resources such as the audio device.
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
