<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="net.ech.anagram.*" %>
<%@ page import="net.ech.apps.anagram.*" %>
<%@ page import="java.util.*" %>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="StyleSheet" type="text/css" href="anagrammara.css">
    <title>Anagrammara</title>
  </head>

  <script type="text/javascript" src="jquery-1.3.2.min.js"></script>

  <body>
    <div class="outer">
    <div class="content">
      <h1>Anagram Finder</h1>
      <form method="post" action="anagram">
        <input name="f" id="f" type="hidden" value="html"/>
        <input name="i" id="i" type="text" width="240"/><input class="submit" type="submit" value="FIND"/>
      </form> 
<% if (request.getAttribute("input") != null) { %>
      <div class="loading">Working...</div>
<% } %>
      <div class="output">
        <pre>
<% if (request.getAttribute("anagram") != null) { %>
<jsp:include page="resulttext.jsp"/>
<% } %>
        </pre>
      </div>
    </div>
    </div>
  </body>

  <script type="text/javascript">
    $(function() {
        $("form").submit(function() {
            if ($("#i").val() == "") return false;
            $("#f").val("ajax");
            return true;
        });
<% if (request.getAttribute("input") != null) { %>
        $.ajax({
            type: 'POST',
            url: 'anagram',
            data: {
                i: '<%= request.getAttribute("input") %>',
                f: 'text'
            },
            success: function(data) {
                $(".loading").hide();
                $("pre").html(data);
            }
        });
<% } %>
    });
  </script>
</html>
