<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FabFlix Employee Log In</title>
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
    .pad-top {
        padding-top: 130px;
    }
    .pad-bottom {
        padding-bottom: 30px;
    }
    body {
        background: url("gray.jpg") no-repeat fixed center;
    }
    </style>
</head>
<body>

    <!-- Form -->
    <div class="container">
        <div class="col-md-6 col-md-offset-3 pad-top">
            <h1 class="text-center pad-bottom">FabFlix Employee Log In</h1>
        </div>
        <div class="col-md-2 col-md-offset-5">
            <form name="login_form" action="_dashboard/login" method="POST">
                <div class="form-group">
                    <input class="form-control" name="email" type="email" placeholder="Email" required="required" />
                </div>
                <div class="form-group">
                    <input class="form-control" name="password" type="password" placeholder="Password" required="required" />
                </div>
                <button type="submit" value="_dashboard" id="login_button" class="btn btn-default btn-block">LOG IN</button>
            </form>

            <h4>${invalid_login}</h4>
            <h4>${error}</h4>
        </div>
    </div>
    
</body>
</html>