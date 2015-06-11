<%@ page import="itm.model.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Enumeration" %>

<!--
/*******************************************************************************
This file is part of the WM.II.ITM course 2014
(c) University of Vienna 2009-2014
*******************************************************************************/
-->

<graphml xmlns="http://graphml.graphdrawing.org/xmlns"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
    <graph id="G" edgedefault="undirected">

        <key id="name" for="node" attr.name="name" attr.type="string"/>
        <key id="url" for="node" attr.name="url" attr.type="string"/>
        <key id="type" for="node" attr.name="type" attr.type="string"/>

        <node id="n0">
            <data key="type">concept</data>
            <data key="name">Tags</data>
            <data key="url">http://localhost:8080/itm/</data>
        </node>

        <%
            // get all media objects
            ArrayList<AbstractMedia> media = MediaFactory.getMedia();

            // List for storing tag-nodes
            Hashtable<String, String> tagNodes = new Hashtable<String, String>();

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************

            // iterate over all available media objects
            int c = 2;
            for (AbstractMedia medium : media) {
                c++;
                if (medium instanceof ImageMedia) {
                } else if (medium instanceof AudioMedia) {
                } else if (medium instanceof VideoMedia) {
                }

                // create tag nodes (and respective edges) if not existing
                for(String tag : medium.getTags()){
                    tagNodes.putIfAbsent(tag, medium.getName());
                }
            }
        %>

        <%
        Enumeration<String> tags = tagNodes.keys();
        while(tags.hasMoreElements()){
            String tag = tags.nextElement();
        %>
        <node id="<%=tag%>">
            <data key="type">concept</data>
            <data key="name"><%=tag%></data>
            <data key="url">http://localhost:8080/itm/tags.jsp?tag=<%=tag%></data>
        </node>
        <edge id="edge_n0_<%=tag%>" directed="false" source="n0" target="<%=tag%>"/>
        <%}%>

        <%
            for (AbstractMedia medium : media) {

        %>
        <node id="<%=medium.getName()%>">
            <data key="type">node</data>
            <data key="name"><%=medium.getName()%></data>
            <data key="url">http://localhost:8080/<%= medium.getInstance() %></data>
        </node>
        <%
            ArrayList<String> mediumtags = medium.getTags();
            for(String mediumtag : mediumtags){
        %>
        <edge id="edge_<%=medium.getName()%>_<%=mediumtag%>" directed="false" source="<%=medium.getName()%>" target="<%=mediumtag%>"/>
        <%}%>
        <%}%>

    </graph>
</graphml>