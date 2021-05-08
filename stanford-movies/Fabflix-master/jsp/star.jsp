
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
    body {
        background: url("gray.jpg") no-repeat center fixed;
        height: 100%;
        width: 100%;
        position: relative;
        background-size: cover;
    }
    </style>
</head>
<body>

    <%@include file="header.jsp"%>

    <div class="container top-pad">
        <div class="col-md-8 col-md-offset-2">
            <c:choose>
            <c:when test="${not empty star}">
                <h3>Star Info:</h3>
                <br /> <!-- Line break -->
                    <div class="row">
                        <div class="col-md-4">
                            <img src="${star.photoUrl}" alt="No image on file.">
                        </div>
                    <div class="col-md-6 col-md-offset-2">
                        <p><strong>ID:</strong> ${star.id}</p>
                        <p><strong>Name:</strong> ${star.getFullName()}</p>
                        <c:set var="dob" value="${star.getDob() != null ? star.getDob() : \"Unknown\" }" />
                        <p><strong>Date of Birth:</strong>
                            <c:out value="${dob}" /></p>
                            <%--<fmt:formatDate pattern="MM-dd-yyyy" value="${star.getDob()}" /></p>--%>
                        <p><strong>Films:</strong>
                            <ul>
                                <c:forEach var="movie" items="${star.getMovies()}" varStatus="status">
                                    <li><a href="movie?id=${movie.id}">${movie.title}</a></li>
                                    <%--<c:if test="${!status.last}">,</c:if>--%> <!-- Comma separate list (if not using <ul>)-->
                                </c:forEach>
                            </ul>
                            <!-- Alternative bootstrap list -->
                            <%--<div class="list-group">--%>
                                <%--<c:forEach var="movie" items="${star.getMovies()}" varStatus="status">--%>
                                    <%--<a href="movie?id=${movie.id}" class="list-group-item">${movie.title}</a></a>--%>
                                    <%--&lt;%&ndash;<c:if test="${!status.last}">,</c:if>&ndash;%&gt; <!-- Comma separate list -->--%>
                                <%--</c:forEach>--%>
                            <%--</div>--%>
                        </p>
                </div>
            </div>
            </c:when>
            </c:choose>
        </div>
    </div>
    
    
</body>
</html>
