<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="itm.model.*" %>
<!--
/*******************************************************************************
This file is part of the WM.II.ITM course 2014
(c) University of Vienna 2009-2014
*******************************************************************************/
-->
<%

%>
<html>
<head>
</head>
<body>


fill in your code here :)
<%

    String tag = null;

    // ***************************************************************
    //  Fill in your code here!
    // ***************************************************************

    // get "tag" parameter

    tag = request.getParameter("tag");

    // if no param was passed, forward to index.jsp (using jsp:forward)

%>


<h1>Media that is tagged with <%= tag %>
</h1>
<a href="index.jsp">back</a>

<%

    // ***************************************************************
    //  Fill in your code here!
    // ***************************************************************

    // get all media objects that are tagged with the passed tag

    // iterate over all available media objects and display them

%>
<% // TODO this is not good practice, we should NOT be using scriptlets, but expression language, JSTL, etc... %>
<jsp:include page="index.jsp">
    <jsp:param name="tagname" value="<%= tag %>"/>
</jsp:include>


</body>
</html>
