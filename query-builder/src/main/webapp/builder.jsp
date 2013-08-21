<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Query Builder</title>

    <%
        String neonServerUrl = getServletContext().getInitParameter("neon.url");
        String owfServerUrl = getServletContext().getInitParameter("owf.url");
    %>

    <link rel="stylesheet" type="text/css" href="css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/builder.css"/>
    <link rel="stylesheet" type="text/css" href="css/slickgrid/slick.grid.css"/>
    <link rel="stylesheet" type="text/css" href="css/smoothness/jquery-ui-1.8.16.custom.css"/>
    <link rel="stylesheet" type="text/css" href="<%=neonServerUrl%>/css/neon.css"/>

    <script src="<%=owfServerUrl%>/js/owf-widget.js"></script>
    <script src="<%=neonServerUrl%>/js/neon.js"></script>

    <!-- build:js js/query-builder.js -->
    <script src="tables.js"></script>
    <script src="jquery/jquery-1.10.1.min.js"></script>
    <script src="jquery-resize/jquery.ba-resize-1.1.min.js"></script>
    <script src="jquery-resize/jquery.autosize-min.js"></script>
    <script src="builder.js"></script>
    <!-- endbuild -->

    <script>
        OWF.relayFile = 'js/eventing/rpc_relay.uncompressed.html';
        neon.query.SERVER_URL = '<%=neonServerUrl%>';
        neon.util.AjaxUtils.useDefaultStartStopCallbacks();
    </script>

</head>
<body>
<div id="query">
    <h4>
        Enter a Query
    </h4>
    <textarea id="queryText" cols="80" rows="3"></textarea>
    <script type="text/javascript">
        $('#queryText').autosize();
        $('#queryText').val('').trigger('autosize.resize');
    </script>
    <br/><br/>
    <button id="submit" class="btn" onclick="submitter();">Submit</button>
    <br/><br/>
</div>

<div id="results"></div>

<script>
    var table = new tables.Table('#results', {data: []});
    function submitter() {
        var query = $('#queryText').val();

        neon.util.AjaxUtils.doPost(neon.query.SERVER_URL + "/services/languageservice/query",
                {
                    data: { text: query },
                    success: function (data) {
                        $('#results').empty();
                        table = new tables.Table('#results', {data: data.data}).draw();
                    }
                });
    }
</script>


</body>
</html>