<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FabFlix - Home</title>
    <meta name="viewport" content="width-device-width, initial-scale=1">

    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/" />

    <!-- From cdn -->
    <!--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">-->
    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>-->
    <!--<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>-->

    <!-- From local -->
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap.min.css">

    <style>
    .padding {
        padding: 20px;
    }
    .margin {
        margin: 30px 10px;
    }
    .top-pad {
        padding-top: 130px;
    }
    .bottom-pad {
        padding-bottom: 130px;
    }
    .main {
        background: url("gray.jpg") no-repeat center fixed;
        height: 100%;
        width: 100%;
        position: relative;
        background-size: cover;
    }
    .white-text {
        color: #fff;
    }
    .browse-top-pad {
        padding-bottom: 10px;
    }
    </style>
    
</head>
<body>

    <%@include file="header.jsp"%>

    <div class="container main top-pad bottom-pad text-center">
        <h1>FabFlix</h1>
        <h2 style="padding-bottom:20px">Whatever you need, we got it.</h2>
        <div class="col-md-4 col-md-offset-4">


            <!-- Main search bar -->
            <form name="search_by_title" action="search" method="GET">
                <div class="input-group">
                    <input type="text" name="title" class="form-control" placeholder="Search movie" required>
                    <div class="input-group-btn">
                    <button type="submit" value="search" id="search_button" class="btn btn-default">Search</button>
                    </div>
                </div>
            </form>
            <!-- END Main search bar -->


            <br />
            <p>Need a more refined search? <a href="advanced-search">Advanced Search</a></p>
            <br />
            <p class="text-center">Don't know what you're looking for?</p>
            <a href="browse"class="btn btn-default">Browse</a>
        </div>
    </div>
    
</body>
</html>
