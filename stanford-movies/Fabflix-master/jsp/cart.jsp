<%@ page language="java"
        contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="models.SessionCart" %>
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
            <h3>Cart:</h3>
            <br />
            <div class="row">
                <div class="col-md-4">
                    <p><strong>Movie Title</strong></p>
                </div>
                <div class="col-md-2">
                    <p><strong>Price</strong></p>
                </div>
                <div class="col-md-2">
                    <p><strong>Qty</strong></p>
                </div>
                <div class="col-md-2">
                    <p><strong>Update</strong></p>
                </div>
                <div class="col-md-2">
                    <p><strong>Remove</strong></p>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty sessionScope.cart.getCartItems()}">
                    <c:forEach var="cartItem" items="${sessionScope.cart.getCartItems()}" >
                        <div class="row">
                            <div class="col-md-4">
                                <p>${cartItem.movie.title}</p>
                            </div>
                            <div class="col-md-2">
                                <p>13.71</p>
                            </div>

                            <form action="cart" method="POST">
                                <div class="col-md-2">
                                    <input hidden name="movieId" value="${cartItem.movie.id}">
                                    <input required class="form-control input-small" name="quantity" type="number" placeholder="${cartItem.quantity}">
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-default btn-xs">Update</button>
                                </div>
                            </form>

                            <form action="cart" method="POST">
                                <div class="col-md-2">
                                    <input hidden name="movieId" value="${cartItem.movie.id}">
                                    <input hidden name="quantity" value="0">
                                    <button type="submit" class="btn btn-default btn-xs">Remove</button>
                                </div>
                            </form>
                        </div>

                        <br />
                    </c:forEach>
                        <a href="checkout" class="btn btn-default">Check Out</a>
                </c:when>
            </c:choose>
        </div>
    </div>


</body>
</html>
