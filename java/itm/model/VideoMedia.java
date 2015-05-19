package itm.model;

/*******************************************************************************
 * This file is part of the ITM course 2015
 * (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.io.*;

public class VideoMedia extends AbstractMedia {

    // ***************************************************************
    // Fill in your code here!
    // ***************************************************************

	/* video format metadata */
    protected String videoCodec;
    protected String videoCodecID;
    protected String videoFrameRate;
    protected String videoLength;
    protected String videoHeight;
    protected String videoWidth;

	/* audio format metadata */
    protected String audioCodec;
    protected String audioCodecID;
    protected String audioChannels;
    protected String audioSampleRate;
    protected String audioBitRate;

    /**
     * Constructor.
     */
    public VideoMedia() {
        super();
    }

    /**
     * Constructor.
     */
    public VideoMedia(File instance) {
        super(instance);
    }

	/* GET / SET methods */
    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getVideoCodecID() {
        return videoCodecID;
    }

    public void setVideoCodecID(String videoCodecID) {
        this.videoCodecID = videoCodecID;
    }

    public String getVideoFrameRate() {
        return videoFrameRate;
    }

    public void setVideoFrameRate(String videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(String videoHeight) {
        this.videoHeight = videoHeight;
    }

    public String getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(String videoWidth) {
        this.videoWidth = videoWidth;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public String getAudioCodecID() {
        return audioCodecID;
    }

    public void setAudioCodecID(String audioCodecID) {
        this.audioCodecID = audioCodecID;
    }

    public String getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(String audioChannels) {
        this.audioChannels = audioChannels;
    }

    public String getAudioSampleRate() {
        return audioSampleRate;
    }

    public void setAudioSampleRate(String audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public String getAudioBitRate() {
        return audioBitRate;
    }

    public void setAudioBitRate(String audioBitRate) {
        this.audioBitRate = audioBitRate;
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
        out.println("type: video");
        StringBuffer sup = super.serializeObject();
        out.print(sup);

		/* video fields */

        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        if(videoCodec != null) {
            out.println("videoCodec: " + getVideoCodec());
        }
        if(videoCodecID != null) {
            out.println("videoCodecID: " + getVideoCodecID());
        }
        if(videoFrameRate != null) {
            out.println("videoFrameRate: " + getVideoFrameRate());
        }
        if(videoLength != null) {
            out.println("videoLength in seconds: " + getVideoLength());
        }
        if(videoHeight != null) {
            out.println("videoHeight: " + getVideoHeight());
        }
        if(videoWidth != null) {
            out.println("videoWidth: " + getVideoWidth());
        }
        if(audioCodec != null) {
            out.println("audioCodec: " + getAudioCodec());
        }
        if(audioCodecID != null) {
            out.println("audioCodecID: " + getAudioCodecID());
        }
        if(audioChannels != null) {
            out.println("audioChannels: " + getAudioChannels());
        }
        if(audioSampleRate != null) {
            out.println("audioSampleRate: " + getAudioSampleRate());
        }
        if(audioBitRate != null) {
            out.println("audioBitRate: " + getAudioBitRate());
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

			/* video fields */
            // ***************************************************************
            // Fill in your code here!
            // ***************************************************************

            // read and set properties
            if (line.startsWith("videoCodec: "))
                setVideoCodec(line.substring("videoCodec: ".length()));
            if (line.startsWith("videoCodecID: "))
                setVideoCodecID(line.substring("videoCodecID: ".length()));
            if (line.startsWith("videoFrameRate: "))
                setVideoFrameRate(line.substring("videoFrameRate: ".length()));
            if (line.startsWith("videoLength: "))
                setVideoLength(line.substring("videoLength in seconds: ".length()));
            if (line.startsWith("videoHeight: "))
                setVideoHeight(line.substring("videoHeight: ".length()));
            if (line.startsWith("videoWidth: "))
                setVideoWidth(line.substring("videoWidth: ".length()));
            if (line.startsWith("audioCodec: "))
                setAudioCodec(line.substring("audioCodec: ".length()));
            if (line.startsWith("audioCodecID: "))
                setAudioCodecID(line.substring("audioCodecID: ".length()));
            if (line.startsWith("audioChannels: "))
                setAudioChannels(line.substring("audioChannels: ".length()));
            if (line.startsWith("audioSampleRate: "))
                setAudioSampleRate(line.substring("audioSampleRate: ".length()));
            if (line.startsWith("audioBitRate: "))
                setAudioBitRate(line.substring("audioBitRate: ".length()));
        }
    }

}
