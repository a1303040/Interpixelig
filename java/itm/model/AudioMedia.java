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
    protected String duration;
    protected String author;
    protected String title;
    protected String date;
    protected String comment;
    protected String album;
    protected String track;
    protected String composer;
    protected String genre;
    protected String frequency;
    protected String bitrate;
    protected String channels;

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
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
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

        if(encoding != null) {
            out.println("encoding: " + getEncoding());
        }
        if(duration != null) {
            out.println("duration in seconds: " + getDuration());
        }
        if(author != null) {
            out.println("author: " + getAuthor());
        }
        if(title != null) {
            out.println("title: " + getTitle());
        }
        if(date != null) {
            out.println("date: " + getDate());
        }
        if(comment != null) {
            out.println("comment: " + getComment());
        }
        if(album != null) {
            out.println("album: " + getAlbum());
        }
        if(track != null) {
            out.println("track: " + getTrack());
        }
        if(composer != null) {
            out.println("composer: " + getComposer());
        }
        if(genre != null) {
            out.println("genre: " + getGenre());
        }
        if(frequency != null) {
            out.println("frequency: " + getFrequency());
        }
        if(bitrate != null) {
            out.println("bitrate: " + getBitrate());
        }
        if(channels != null) {
            out.println("channels: " + getChannels());
        }
        
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
                setDuration(line.substring("duration: ".length()));
            if (line.startsWith("author: "))
                setAuthor(line.substring("author: ".length()));
            if (line.startsWith("title: "))
                setTitle(line.substring("title: ".length()));
            if (line.startsWith("date: "))
                setDate(line.substring("date: ".length()));
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
                setFrequency(line.substring("frequency: ".length()));
            if (line.startsWith("bitrate: "))
                setBitrate(line.substring("bitrate: ".length()));
            if (line.startsWith("channels: "))
                setChannels(line.substring("channels: ".length()));

        }
    }

    @Override
    public String formatTags(StringBuffer buf){
        return super.formatTags(buf);
    }
}
