<%@ page import="itm.model.*" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="itm.image.ImageDominantColor" %>
<!--
/*******************************************************************************
This file is part of the WM.II.ITM course 2014
(c) University of Vienna 2009-2014
*******************************************************************************/
-->
<html>
<head>
    <LINK href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>Welcome to the ITM media library</h1>
<a href="infovis.jsp">infovis</a>

<%=
// TODO for debugging only…
request.getParameter("tagname")
%>

<%
    // get the file paths - this is NOT good style (resources should be loaded via inputstreams...)
    // we use it here for the sake of simplicity.
    String basePath = "webapps/itm/media/";
    if (basePath == null)
        throw new NullPointerException("could not determine base path of media directory! please set manually in JSP file!");
    File base = new File(basePath);
    File imageDir = new File(basePath, "img");
    File audioDir = new File(basePath, "audio");
    File videoDir = new File(basePath, "video");
    File metadataDir = new File(basePath, "md");
    MediaFactory.init(imageDir, audioDir, videoDir, metadataDir);

    String tag = request.getParameter("tagname");
    // get all media objects
    ArrayList<AbstractMedia> media = MediaFactory.getMedia(tag);

    int c = 0; // counter for rowbreak after 3 thumbnails.
    // iterate over all available media objects
    for (AbstractMedia medium : media) {
        c++;
%>
<div style="width:300px;padding:10px;float:left;">
    <%

        // handle images
        if (medium instanceof ImageMedia) {
            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************

            // show the histogram of the image on mouse-over

            // display image thumbnail and metadata
            ImageMedia img = (ImageMedia) medium;
    %>

    <!-- get the histograms - this is NOT good style -->
    <!-- we use it here for the sake of simplicity. -->
    <div style="width:200px;height:200px;padding:10px" ;>
        <div class="imagethumb">
            <a href="media/img/<%= img.getInstance().getName()%>">
                <img src="media/md/<%= img.getInstance().getName() %>.thumb.png" border="0"/>
                <img src="media/md/<%= img.getInstance().getName() %>.hist.png" border="0"/>
            </a>
        </div>
        </a>
    </div>
    <div>
        Name: <%= img.getName() %><br/>
        Dimensions: <%= img.getWidth() %>x<%= img.getHeight() %>px<br/>
        Tags: <% for (String t : img.getTags()) { %><a href="tags.jsp?tag=<%= t %>"><%= t %>

    </a> <% } %><br/>
        Meta Data:
        <span class="metadata" ><%= img.formatTags(img.serializeObject()) %></span>
    </div>
    <%
    } else if (medium instanceof AudioMedia) {
        // display audio thumbnail and metadata
        AudioMedia audio = (AudioMedia) medium;
    %>
    <div style="width:200px;height:200px;padding:10px;">
        <br/><br/><br/><br/>
        <embed src="media/md/<%= audio.getInstance().getName() %>.wav" autostart="false" width="150" height="30"/>
        <br/>
        <a href="media/audio/<%= audio.getInstance().getName()%>">
            Download <%= audio.getInstance().getName()%>
        </a>
    </div>
    <div>
        Name: <%= audio.getName() %><br/>
        Duration: <%= audio.getDuration() %><br/>
        Tags: <% for (String t : audio.getTags()) { %><a href="tags.jsp?tag=<%= t %>"><%= t %>
    </a> <% } %><br/>
        Meta Data:
        <span class="metadata" ><%= audio.formatTags(audio.serializeObject()) %></span>
    </div>
    <%
    } else if (medium instanceof VideoMedia) {
        // handle videos thumbnail and metadata...
        VideoMedia video = (VideoMedia) medium;
    %>
    <div style="width:200px;height:200px;padding:10px;">
        <a href="media/video/<%= video.getInstance().getName()%>">

            <object width="200" height="200">
                <param name="movie" value="media/md/<%= video.getInstance().getName() %>_thumb.swf">
                <embed src="media/md/<%= video.getInstance().getName() %>_thumb.swf" width="200" height="200">
                </embed>
            </object>

        </a>
    </div>
    <div>
        Name: <a href="media/video/<%= video.getInstance().getName()%>"><%= video.getName() %>
    </a><br/>
        Tags: <% for (String t : video.getTags()) { %><a href="tags.jsp?tag=<%= t %>"><%= t %>
    </a> <% } %><br/>
        Meta Data:
        <span class="metadata" ><%= video.formatTags(video.serializeObject()) %></span>
    </div>
    <%
        } else {
        }

    %>
</div>
<%
    if (c % 3 == 0) {
%>
<div style="clear:left"/>
<%
        }

    } // for

%>

</body>
</html>
