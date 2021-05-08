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
    <%--<script src="js/jquery.min.js"></script>--%>
    <%--<script src="js/bootstrap.min.js"></script>--%>
    <%--<link rel="stylesheet" href="css/bootstrap.min.css">--%>

    <link href="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.css" rel="stylesheet"/>
    <link href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.2/css/bootstrap.css" rel="stylesheet"/>

    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.2/js/bootstrap.js"></script>

    <script src="js/tooltip.js"></script>

    <style>
    .margin {
        margin-top: 30px;
        margin-bottom: 30px;
    }
    .top-pad {
        padding-top: 70px;
    }
    .bot-pad {
        padding-bottom: 50px;
    }
    hr {
        border-top: 1px solid #333333;
    }
    .img-center {
        display: block;
        margin: auto;
        height: auto;
    }
    .ui-tooltip {
        max-width: 800px;
        width: 800px;
    }
    </style>
</head>
<body>

    <!-- Header file -->
    <%@include file="header.jsp"%>


    <div class="container top-pad">
        <div class="col-md-8 col-md-offset-2">
            <c:choose>
                <c:when test="${not empty movieList}">

                    <h3>Search Results:</h3>



                    <!-- Sorting -->
                    <!-- Build a newSortUrlQueryString -->
                    <c:url var="newSortUrlQueryString" value="">
                    <c:forEach items="${param}" var="entry">
                            <c:if test="${entry.key != 'sort'}">
                                <c:param name="${entry.key}" value="${entry.value}" />
                            </c:if>
                    </c:forEach>
                    </c:url>

                    <p>Sort by:
                        <!-- Sort by title ascending, A-Z -->
                        <a class="btn btn-default" href="search${newSortUrlQueryString}&sort=titleAscending" role="button">
                            Title <span class="glyphicon glyphicon-arrow-up">
                            </span>
                        </a>

                        <!-- Sort by title descending, Z-A -->
                        <a class="btn btn-default" href="search${newSortUrlQueryString}&sort=titleDescending" role="button">
                            Title <span class="glyphicon glyphicon-arrow-down">
                            </span>
                        </a>

                        <!-- Sort by year descending, high-low -->
                        <a class="btn btn-default" href="search${newSortUrlQueryString}&sort=yearDescending" role="button">
                            Year <span class="glyphicon glyphicon-arrow-up">
                            </span>
                        </a>

                        <!-- Sort by year ascending, low-high -->
                        <a class="btn btn-default" href="search${newSortUrlQueryString}&sort=yearAscending" role="button">
                            Year <span class="glyphicon glyphicon-arrow-down">
                            </span>
                        </a>
                    </p>


                    <br />
                        

                    <!-- Display the results -->
                    <c:forEach var="movie" items="${movieList}">
                        <div class="row">
                            <div class="col-md-4 col-md-offset-1">
                                <a href="movie?id=${movie.id}"><img class="img-center" src="${movie.bannerUrl}" alt="No image on file."></a>
                            </div>
                            <div class="col-md-4 col-md-offset-1">
                                <p class="movieCardAnchor" id="${movie.id}"><strong>Title:</strong> <a href="movie?id=${movie.id}">${movie.title}</a></p>
                                <p><strong>ID:</strong> ${movie.id}</p>
                                <p><strong>Year:</strong> ${movie.year}</p>
                                <p><strong>Director:</strong> ${movie.director}</p>
                                <p><strong>Genre:</strong>
                                    <c:forEach var="genreInMovie" items="${movie.getGenres()}" varStatus="status">
                                    <a href="search?genre=${genreInMovie.id}">${genreInMovie.name}</a>
                                    <c:if test="${!status.last}">, </c:if> <!-- Comma separate list -->
                                    </c:forEach>
                                </p>
                                <p><strong>Stars:</strong>
                                    <c:forEach var="starInMovie" items="${movie.getStars()}" varStatus="status">
                                    <a href="star?id=${starInMovie.id}">${starInMovie.getFullName().trim()}</a>
                                    <c:if test="${!status.last}">,</c:if>
                                    </c:forEach>
                                </p>
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
                        <hr />
                        </c:forEach>
                    </div>
        </div>

            <!-- Sorting -->
            <!-- Build a newResultsPerPageQueryString -->
            <c:url var="newResultsPerPageQueryString" value="">
                <c:forEach items="${param}" var="entry">
                    <c:if test="${entry.key != 'resultsPerPage' && entry.key != 'page'}">
                        <c:param name="${entry.key}" value="${entry.value}" />
                    </c:if>
                </c:forEach>
            </c:url>

            <!-- Next and Previous -->
            <c:url var="newPageQueryString" value="">
                <c:forEach items="${param}" var="entry">
                    <c:if test="${entry.key != 'page'}">
                        <c:param name="${entry.key}" value="${entry.value}" />
                    </c:if>
                </c:forEach>
            </c:url>
            <nav>
                <ul class="pager">

                    <li class="active">
                        <a href="search${newPageQueryString}&page=${(requestScope.page != null) ? requestScope.page - 1 : 1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo; Previous</span>
                        </a>
                    </li>

                    <li class="active">
                        <a href="search${newPageQueryString}&page=${requestScope.page != null ? requestScope.page + 1 : 2}" aria-label="Next">
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                    </li>

                    <li>
                        <p> Results per page: <a href="search${newResultsPerPageQueryString}&page=1&resultsPerPage=5"> 5 </a> | <a href="search${newResultsPerPageQueryString}&page=1&resultsPerPage=10"> 10 </a> | <a href="search${newResultsPerPageQueryString}&page=1&resultsPerPage=20">20</a></p>
                    </li>

                </ul>
            </nav>
            <!-- /.ul class="pager -->

            <%--<div class="row margin">--%>
                <%--<div class="col-md-4 col-md-offset-1">--%>
                    <%--<button type="button" class="btn btn-default">Previous</button>--%>
                    <%--<button type="button" class="btn btn-default">Next</button>--%>
                <%--</div>--%>
                <%--<div class="col-md-4 col-md-offset-1 text-right">--%>
                    <%--<p> Results per page: <a href="search${newResultsPerPageQueryString}&resultsPerPage=5"> 5 </a> | <a href="search${newResultsPerPageQueryString}&resultsPerPage=10"> 10 </a> | <a href="search${newResultsPerPageQueryString}&resultsPerPage=20">20</a>--%>
                    <%--</p>--%>
                <%--</div>--%>
            <%--</div>--%>

        <%--</div>--%>

            </c:when>
                <c:otherwise>
                    <br />
                    <div class="alert alert-info">
                        <p>No movies found.</p>
                    </div>
                </c:otherwise>
        </c:choose>
    </div>
    
</body>
</html>
