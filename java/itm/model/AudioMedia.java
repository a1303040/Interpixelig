package itm.model;

/*******************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.io.*;

public class AudioMedia extends AbstractMedia {

    // ***************************************************************
    // Fill in your code here!
    // ***************************************************************


    protected String encoding;
    protected float duration;
    protected String author;
    protected String title;
    protected int date;
    protected String comment;
    protected String album;
    protected String track;
    protected String composer;
    protected String genre;
    protected int frequency;
    protected int bitrate;
    protected int channels;

    /**
     * Constructor.
     */
    public AudioMedia() {
        super();
    }

    /**
     * Constructor.
     */
    public AudioMedia(File instance) {
        super(instance);
    }

    /* GET / SET methods */
    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    // ***************************************************************
    // Fill in your code here!
    // ***************************************************************

	/* (de-)serialization */

    /**
     * Serializes this object to the passed file.
     *
     */
    @Override
    public StringBuffer serializeObject() throws IOException {
        StringWriter data = new StringWriter();
        PrintWriter out = new PrintWriter(data);
        out.println("type: audio");
        StringBuffer sup = super.serializeObject();
        out.print(sup);

        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        out.println("encoding: " + getEncoding());
        out.println("duration in seconds: " + getDuration());
        out.println("author: " + getAuthor());
        out.println("title: " + getTitle());
        out.println("date: " + getDate());
        out.println("comment: " + getComment());
        out.println("album: " + getAlbum());
        out.println("track: " + getTrack());
        out.println("composer: " + getComposer());
        out.println("genre: " + getGenre());
        out.println("frequency: " + getFrequency());
        out.println("bitrate: " + getBitrate());
        out.println("channels: " + getChannels());
        
        return data.getBuffer();
    }

    /**
     * Deserializes this object from the passed string buffer.
     */
    @Override
    public void deserializeObject(String data) throws IOException {
        super.deserializeObject(data);

        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);
        String line = null;
        while ((line = br.readLine()) != null) {

            // ***************************************************************
            // Fill in your code here!
            // ***************************************************************

            // read and set properties
            if (line.startsWith("encoding: "))
                setEncoding(line.substring("encoding: ".length()));
            if (line.startsWith("duration in seconds: "))
                setDuration(Float.parseFloat(line.substring("duration: ".length())));
            if (line.startsWith("author: "))
                setAuthor(line.substring("author: ".length()));
            if (line.startsWith("title: "))
                setTitle(line.substring("title: ".length()));
            if (line.startsWith("date: "))
                setDate(Integer.parseInt(line.substring("date: ".length())));
            if (line.startsWith("comment: "))
                setComment(line.substring("comment: ".length()));
            if (line.startsWith("album: "))
                setAlbum(line.substring("album: ".length()));
            if (line.startsWith("track: "))
                setTrack(line.substring("track: ".length()));
            if (line.startsWith("composer: "))
                setComposer(line.substring("composer: ".length()));
            if (line.startsWith("genre: "))
                setGenre(line.substring("genre: ".length()));
            if (line.startsWith("frequency: "))
                setFrequency(Integer.parseInt(line.substring("frequency: ".length())));
            if (line.startsWith("bitrate: "))
                setBitrate(Integer.parseInt(line.substring("bitrate: ".length())));
            if (line.startsWith("channels: "))
                setChannels(Integer.parseInt(line.substring("channels: ".length())));

        }
    }

}
