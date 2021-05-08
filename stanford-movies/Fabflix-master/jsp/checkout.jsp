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
    .top-pad {
        padding-top: 70px;
        padding-bottom: 70px;
    }

    </style>

    <!-- Formats the date picker to be mm-dd-yy -->
    <!--<script>-->
        <!--$(document).ready(function() {-->
            <!--$('.datepicker')-->
                    <!--.datepicker({ format: 'yyyy-mm-dd' })-->
                    <!--.on('changeDate', function(ev) {-->
                        <!--$(this).datepicker('hide');-->
                    <!--});-->
        <!--});-->
    <!--</script>-->

</head>
<body>

    <%@include file="header.jsp"%>

    <div class="container top-pad">
        <div class="row">
            <div class="col-md-6 bottom-pad">
                <h3>Check Out</h3>
                <hr />
            </div>
        </div>
        <form action="checkout" method="POST" role="form">
            <div class="form-group row">
                <label for="firstName" class="col-md-2 form-control-label">First Name</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="firstName" id="firstName" placeholder="First Name" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="lastName" class="col-md-2">Last Name</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="lastName" id="lastName" placeholder="Last Name" required>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label for="ccId" class="col-md-2 form-control-label">Card Number</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="ccId" id="ccId" placeholder="Card Number" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="ccExpiration" class="col-md-2 form-control-label">Expiration Date</label>
                <div class="col-md-4">
                    <input type="date" class="form-control" name="ccExpiration" id="ccExpiration" placeholder="Expiration Date (yyyy-MM-dd)" required>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-offset-2 col-md-6">
                    <button type="submit" value="checkout" class="btn btn-default">Check Out</button>
                </div>
            </div>
        </form>

        <h3>${error}</h3>
    </div>
    
    
</body>
</html>
