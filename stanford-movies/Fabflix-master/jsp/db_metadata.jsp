<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FabFlix Employee Dashboard</title>
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
        padding-bottom: 50px;
    }
    </style>
</head>
<body>

    <div class="container top-pad">
        <div class="row">
            <div class="col-md-6 bottom-pad">
                <h3>Database Metadata</h3>
            </div>
        </div>
        <hr />

        <c:choose>
            <c:when test="${not empty metaData}">


                <c:forEach var="table" items="${metaData.entrySet()}">

                    <h4>${table.getKey()}</h4>
                    <c:forEach var="columnMap" items="${table.getValue()}">
                        <c:forEach var="columnEntry" items="${columnMap.entrySet()}">
                            <p>${columnEntry.getKey()}: ${columnEntry.getValue()}</p>
                        </c:forEach>
                    </c:forEach>

                    <br />
                    <%--<p>${table.getValue().toString()}</p>--%>
                </c:forEach>


            </c:when>
        </c:choose>
        
    </div>

</body>
</html>