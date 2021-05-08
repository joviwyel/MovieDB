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
                <c:when test="${not empty movie}">

                    <h3>Movie Info:</h3>
                    <br /> <!-- Line break -->
                    <div class="row">
                        <div class="col-md-4 col-md-offset-1">
                            <img src="${movie.bannerUrl}" alt="No image on file." width="200">
                        </div>
                        <div class="col-md-4 col-md-offset-1">
                            <p><strong>ID:</strong> ${movie.id}</p>
                            <p><strong>Title:</strong> ${movie.title}</p>
                            <p><strong>Year:</strong> ${movie.year}</p>
                            <p><strong>Director:</strong> ${movie.director}</p>
                            <p><strong>Genre:</strong>
                                <c:forEach var="genre" items="${movie.getGenres()}" varStatus="status">
                                    ${genre.name}
                                    <c:if test="${!status.last}">, </c:if> <!-- Comma separate list -->
                            </c:forEach>
                            </p>
                            <p><strong>Stars:</strong>
                            <ul>
                                <c:forEach var="star" items="${movie.getStars()}" varStatus="status">
                                    <li><a href="star?id=${star.id}">${star.getFullName()}</a></li>
                                </c:forEach>
                            </ul>
                            </p>
                            <!-- Alternative bootstrap list -->
                                <!--<div class="list-group">-->
                                <!--<c:forEach var="movie" items="${star.getMovies()}" varStatus="status">-->
                                <!--<a href="movie?id=${movie.id}" class="list-group-item">${movie.title}</a></a>-->
                                <!--&lt;%&ndash;<c:if test="${!status.last}">,</c:if>&ndash;%&gt; -->
                                <!--</c:forEach>-->
                                <!--</div>-->
                                <!--</p>-->
                            <form action="cart" method="POST">
                                <div class="col-md-2">
                                    <input type="hidden" name="movieId" value="${movie.id}">
                                    <input type="hidden" name="quantity" value="1">
                                    <input type="hidden" name="addToCart" value="true">
                                    <input type="hidden" name="movieTitle" value="${movie.title}">
                                    <button type="submit" class="btn btn-default">Add to Cart</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <br>
                    <div class="alert alert-info">
                        No movie found matching the id.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    
</body>
</html>
