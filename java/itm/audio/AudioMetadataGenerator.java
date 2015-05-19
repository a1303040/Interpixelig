package itm.audio;

/*******************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 *******************************************************************************/

import itm.model.AudioMedia;
import itm.model.MediaFactory;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class reads audio files of various formats and stores some basic audio
 * metadata to text files. It can be called with 3 parameters, an input
 * filename/directory, an output directory and an "overwrite" flag. It will read
 * the input audio file(s), retrieve some metadata and write it to a text file
 * in the output directory. The overwrite flag indicates whether the resulting
 * output file should be overwritten or not.
 * <p>
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class AudioMetadataGenerator {

    /**
     * Constructor.
     */
    public AudioMetadataGenerator() {
    }

    /**
     * Processes an audio file directory in a batch process.
     *
     * @param input     a reference to the audio file directory
     * @param output    a reference to the output directory
     * @param overwrite indicates whether existing metadata files should be
     *                  overwritten or not
     * @return a list of the created media objects (images)
     */
    public ArrayList<AudioMedia> batchProcessAudio(File input, File output,
                                                   boolean overwrite) throws IOException {
        if (!input.exists())
            throw new IOException("Input file " + input + " was not found!");
        if (!output.exists())
            throw new IOException("Output directory " + output + " not found!");
        if (!output.isDirectory())
            throw new IOException(output + " is not a directory!");

        ArrayList<AudioMedia> ret = new ArrayList<AudioMedia>();

        if (input.isDirectory()) {
            File[] files = input.listFiles();
            for (File f : files) {

                String ext = f.getName().substring(
                        f.getName().lastIndexOf(".") + 1).toLowerCase();
                if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
                    try {
                        AudioMedia result = processAudio(f, output, overwrite);
                        System.out.println("created metadata for file " + f
                                + " in " + output);
                        ret.add(result);
                    } catch (Exception e0) {
                        System.err
                                .println("Error when creating metadata from file "
                                        + input + " : " + e0.toString());
                    }

                }

            }
        } else {

            String ext = input.getName().substring(
                    input.getName().lastIndexOf(".") + 1).toLowerCase();
            if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
                try {
                    AudioMedia result = processAudio(input, output, overwrite);
                    System.out.println("created metadata for file " + input
                            + " in " + output);
                    ret.add(result);
                } catch (Exception e0) {
                    System.err
                            .println("Error when creating metadata from file "
                                    + input + " : " + e0.toString());
                }

            }

        }
        return ret;
    }

    /**
     * Processes the passed input audio file and stores the extracted metadata
     * to a textfile in the output directory.
     *
     * @param input     a reference to the input audio file
     * @param output    a reference to the output directory
     * @param overwrite indicates whether existing metadata files should be
     *                  overwritten or not
     * @return the created image media object
     */
    protected AudioMedia processAudio(File input, File output, boolean overwrite)
            throws IOException, IllegalArgumentException {
        if (!input.exists())
            throw new IOException("Input file " + input + " was not found!");
        if (input.isDirectory())
            throw new IOException("Input file " + input + " is a directory!");
        if (!output.exists())
            throw new IOException("Output directory " + output + " not found!");
        if (!output.isDirectory())
            throw new IOException(output + " is not a directory!");

        // create outputfilename and check whether thumb already exists. All
        // image metadata files have to start with "aud_" - this is used by the
        // mediafactory!
        File outputFile = new File(output, "aud_" + input.getName() + ".txt");
        if (outputFile.exists())
            if (!overwrite) {
                // load from file
                AudioMedia media = new AudioMedia();
                media.readFromFile(outputFile);
                return media;
            }


        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        String name = input.getName();
        String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

        // create an audio metadata object
        AudioMedia media = (AudioMedia) MediaFactory.createMedia(input);

        AudioInputStream din = null;
        // load the input audio file, do not decode
        try {
            din = AudioSystem.getAudioInputStream(input);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Error when creating metadata from file " + input + " : " + e.toString());
        }

        // read AudioFormat properties
        AudioFormat format = din.getFormat();


        // read file-type specific properties

        // encoding
        media.setEncoding(format.getEncoding().toString());

        float length = 0;
        try {

            AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(input);

            // mp3 and ogg
            if (baseFileFormat instanceof TAudioFileFormat) {
                Map properties = ((TAudioFileFormat) baseFileFormat).properties();

                // duration in seconds
                if (properties.containsKey("duration")) {
                    length = ((Number) properties.get("duration")).floatValue() / 1000000;
                }

                // author
                if (properties.containsKey("author")) {
                    media.setAuthor(properties.get("author").toString());
                }

                // title
                if (properties.containsKey("title")) {
                    media.setTitle(properties.get("title").toString());
                }

                // date
                if (properties.containsKey("date")) {
                    media.setDate(properties.get("date").toString());
                }

                // comment
                if (properties.containsKey("comment")) {
                    media.setComment(properties.get("comment").toString());
                }

                // album
                if (properties.containsKey("album")) {
                    media.setAlbum(properties.get("album").toString());
                }

                // track
                if (properties.containsKey("ogg.comment.track")) {
                    media.setTrack(properties.get("ogg.comment.track").toString());
                }
                if (properties.containsKey("mp3.id3tag.track")) {
                    media.setTrack(properties.get("mp3.id3tag.track").toString());
                }

                // genre
                if (properties.containsKey("ogg.comment.genre")) {
                    media.setGenre(properties.get("ogg.comment.genre").toString());
                }
                if (properties.containsKey("mp3.id3tag.genre")) {
                    media.setGenre(properties.get("mp3.id3tag.genre").toString());
                }

                // frequency
                if (properties.containsKey("mp3.frequency.hz")) {
                    media.setFrequency(properties.get("mp3.frequency.hz").toString());
                }
                if (properties.containsKey("ogg.frequency.hz")) {
                    media.setFrequency(properties.get("ogg.frequency.hz").toString());
                }

                // bitrate
                if (properties.containsKey("mp3.bitrate.nominal.bps")) {
                    media.setBitrate(properties.get("mp3.bitrate.nominal.bps").toString());
                }
                if (properties.containsKey("ogg.bitrate.nominal.bps")) {
                    media.setBitrate(properties.get("ogg.bitrate.nominal.bps").toString());
                }

                // channels
                if (properties.containsKey("ogg.channels")) {
                    media.setChannels(properties.get("ogg.channels").toString());
                }
                if (properties.containsKey("mp3.channels")) {
                    media.setChannels(properties.get("mp3.channels").toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error when creating metadata from file " + input + " : " + e.toString());
        }

        // if .wav
        if (ext.equals("wav")) {
            // duration in seconds
            length = din.getFrameLength() / din.getFormat().getFrameRate();
        }
        media.setDuration(String.valueOf(length));

        // you might have to distinguish what properties are available for what audio format

        // add a "audio" tag
        media.addTag("audio");

        // close the audio and write the md file.
        din.close();
        media.writeToFile(outputFile);

        return media;
    }

    /**
     * Main method. Parses the commandline parameters and prints usage
     * information if required.
     */

    public static void main(String[] args) throws Exception {

        //args = new String[] { "./media/audio", "./media/md" };

        if (args.length < 2) {
            System.out
                    .println("usage: java itm.image.AudioMetadataGenerator <input-image> <output-directory>");
            System.out
                    .println("usage: java itm.image.AudioMetadataGenerator <input-directory> <output-directory>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        AudioMetadataGenerator audioMd = new AudioMetadataGenerator();
        audioMd.batchProcessAudio(fi, fo, true);
    }
}
