
<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FabFlix</title>
    <meta name="viewport" content="width-device-width, initial-scale=1">

    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/" />

    <!-- From cdn -->
    <!--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">-->
    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>-->
    <!--<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>-->

    <!-- From local -->
    <%--<script src="js/jquery.min.js"></script>--%>
    <%--<script src="js/bootstrap.min.js"></script>--%>
    <%--<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.js"></script>--%>
    <%--<link rel="stylesheet" href="css/bootstrap.min.css">--%>


    <!-- Using jquery-ui default styilng -->
    <!-- Works ok -->
    <link href="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.css" rel="stylesheet"/>
    <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.2/css/bootstrap.css" rel="stylesheet"/>

    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.2/js/bootstrap.js"></script>

    <!-- Using jqury-ui-bootstrap styling -->
    <!-- Doesnt work very well -->
    <%--<link href="http://jquery-ui-bootstrap.github.io/jquery-ui-bootstrap/css/custom-theme/jquery-ui-1.10.3.custom.css" rel="stylesheet"/>--%>
    <%--<link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.2/css/bootstrap.css" rel="stylesheet"/>--%>

    <%--<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.js"></script>--%>
    <%--<script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.js"></script>--%>
    <%--<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.2/js/bootstrap.js"></script>--%>

    <script>
    $(document).ready(function() {
        SearchAutoComplete();
    });
    function SearchAutoComplete() {
        $(".autocomplete").autocomplete({
            source: function (request, response) {
                $.ajax({
                    dataType: "text",
                    type: "Get",
                    url: "searchAutoComplete",
                    data: {
                        title: document.getElementById('searchBarText').value
                    },
                    success: function (data) {
                        console.log(data);
                        response(JSON.parse(data));
                    },
                    error: function (data) {
                        console.error(data);
                    }
                });
            }
        });
    }
    </script>

</head>
<body>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#fabflix-nav">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="home">FabFlix</a>
            </div>
            <div class="collapse navbar-collapse" id="fabflix-nav">
                <a href="browse" class="navbar-left btn btn-default">Browse</a>

                <!-- Search Form -->
                <form class="navbar-form navbar-left" id="searchBarForm" action="search" method="GET" >
                    <div class="form-group" id="searchBarInput">
                        <input name="title" type="text" id="searchBarText" class="form-control autocomplete" placeholder="Search">
                    </div>

                    <div class="form-group" id="searchBarSubmitBtn">
                        <button type="submit" value="search" class="btn btn-default">Search</button>
                    </div>
                </form>

                <ul class="nav navbar-nav navbar-right">
                    <li><a href="cart"><span class="glyphicon glyphicon-shopping-cart"></span></a></li>
                </ul>
            </div>
        </div>
    </nav>
</body>
</html>
