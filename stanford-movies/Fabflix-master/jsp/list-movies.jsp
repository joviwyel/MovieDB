
<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
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
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="js/bootstrap.min.js"></script>
    <script src="js/jquery.min.js"></script>
</head>
<body>
    <div class="container">
        <h2>Movies</h2>
        <!--Movies List-->
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <form action="/employee" method="post" id="employeeForm" role="form">
            <input type="hidden" id="idEmployee" name="idEmployee"> <input
                type="hidden" id="action" name="action">
            <c:choose>
                <c:when test="${not empty employeeList}">
                    <table class="table table-striped">
                        <thead>
                            <tr>

                                <%--id;--%>
                                <%--title (hyperlinked);--%>
                                <%--year;--%>
                                <%--director;--%>
                                <%--list of genres;--%>
                                <%--list of stars (each hyperlinked);--%>
                                <td>#</td>
                                <td>Title</td>
                                <td>Year</td>
                                <td>Director</td>
                                <%--<td>Genres</td>--%>
                                <%--<td>Stars</td>--%>
                            </tr>
                        </thead>
                        <c:forEach var="movie" items="${employeeList}">
                            <c:set var="classSucess" value="" />
                            <c:if test="${idEmployee == employee.id}">
                                <c:set var="classSucess" value="info" />
                            </c:if>
                            <tr class="${classSucess}">
                                <td><a
                                    href="/employee?idEmployee=${employee.id}&searchAction=searchById">${employee.id}</a>
                                </td>
                                <td>${movie.id}</td>
                                <td>${movie.title}</td>
                                <td>${movie.year}</td>
                                <td>${movie.director}</td>
                                <%--<td>${movie.genres}</td>--%>
                                <%--<td>${movie.stars}</td>--%>
                            </tr>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <br>
                    <div class="alert alert-info">No people found matching your
                        search criteria</div>
                </c:otherwise>
            </c:choose>
        </form>

    </div>
</body>
</html>
