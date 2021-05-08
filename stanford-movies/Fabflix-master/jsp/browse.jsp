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
    }
    </style>

</head>
<body>

    <%@include file="header.jsp"%>

    <div class="container top-pad">
        <div class="row">
            <div class="col-md-6 bottom-pad">
                <h3>Browse by Genre</h3>
                <hr />
            </div>
            <div class="col-md-6 bottom-pad">
                <h3>Browse by Title</h3>
                <hr />
            </div>
        </div>

        <div class="col-md-6">
            <c:forEach var="genre" items="${applicationScope.genresMap}">
                <p><a href="search?genre=${genre.getKey()}">${genre.getValue()}</a></p>
            </c:forEach>
        </div>

        <div class="col-md-6">
            <div class="row">
                <div class="col-md-1">
                    <p><a href="search?firstLetter=0">0</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=1">1</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=2">2</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=3">3</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=4">4</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=5">5</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=6">6</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=7">7</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=8">8</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=9">9</a></p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-1">
                    <p><a href="search?firstLetter=a">A</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=b">B</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=c">C</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=d">D</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=e">E</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=f">F</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=g">G</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=h">H</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=i">I</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=j">J</a></p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-1">
                    <p><a href="search?firstLetter=k">K</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=l">L</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=m">M</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=n">N</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=o">O</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=p">P</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=q">Q</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=r">R</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=s">S</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=t">T</a></p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-1">
                    <p><a href="search?firstLetter=u">U</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=v">V</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=w">W</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=x">X</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=y">Y</a></p>
                </div>
                <div class="col-md-1">
                    <p><a href="search?firstLetter=z">Z</a></p>
                </div>
            </div>
        </div>
    </div>




</body>
</html>
