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
    .bottom-pad {
        padding-bottom: 10px;
    }
    .left {
        float: left;
    }
    .right {
        float: right;
    }
    </style>
</head>
<body>

    <%@include file="header.jsp"%>

    <div class="container top-pad">
        <div class="row">
            <div class="col-md-6 bottom-pad">
                <h3>Advanced Search</h3>
                <hr />
            </div>
        </div>


        <!-- search form -->
        <form action="search" method="GET" role="form">

            <!-- title -->
            <div class="form-group row">
                <label for="title" class="col-md-2 form-control-label">Title</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="title" placeholder="Movie Title">
                </div>
            </div>

                <!-- year -->
                <div class="form-group row">
                <label for="year" class="col-md-2">Year</label>
                <div class="col-md-4">
                    <select class="form-control" name="year" id="year">
                        <option disabled selected>-select-</option>
                        <option>2016</option>
                        <option>2015</option>
                        <option>2014</option>
                        <option>2013</option>
                        <option>2012</option>
                        <option>2011</option>
                        <option>2010</option>
                        <option>2009</option>
                        <option>2008</option>
                        <option>2007</option>
                        <option>2006</option>
                        <option>2005</option>
                        <option>2004</option>
                        <option>2003</option>
                        <option>2002</option>
                        <option>2001</option>
                        <option>2000</option>
                        <option>1999</option>
                        <option>1998</option>
                        <option>1997</option>
                        <option>1996</option>
                        <option>1995</option>
                        <option>1994</option>
                        <option>1993</option>
                        <option>1992</option>
                        <option>1991</option>
                        <option>1990</option>
                        <option>1989</option>
                        <option>1988</option>
                        <option>1987</option>
                        <option>1986</option>
                        <option>1985</option>
                        <option>1984</option>
                        <option>1983</option>
                        <option>1982</option>
                        <option>1981</option>
                        <option>1980</option>
                        <option>1979</option>
                        <option>1978</option>
                        <option>1977</option>
                        <option>1976</option>
                        <option>1975</option>
                        <option>1974</option>
                        <option>1973</option>
                        <option>1972</option>
                        <option>1971</option>
                        <option>1970</option>
                        <option>1969</option>
                        <option>1968</option>
                        <option>1967</option>
                        <option>1966</option>
                        <option>1965</option>
                        <option>1964</option>
                        <option>1963</option>
                        <option>1962</option>
                        <option>1961</option>
                        <option>1960</option>
                    </select>
                    </div>
                </div>

            <!-- director -->
            <div class="form-group row">
                <label for="director" class="col-md-2 form-control-label">Director</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="director" id="director" placeholder="Director">
                </div>
        </div>

            <!-- starFirstName -->
            <div class="form-group row">
                <label for="starFirstName" class="col-md-2 form-control-label">Star First Name</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="starFirstName" id="starFirstName" placeholder="First Name">
                </div>
            </div>

            <!-- starLastName -->
            <div class="form-group row">
                <label for="starLastName" class="col-md-2 form-control-label">Star Last Name</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="starLastName" id="starLastName" placeholder="Last Name">
                </div>
            </div>


            <!-- Hidden inputs required to always have all params in the query string -->
            <input type="hidden" name="resultsPerPage" value="5">
            <input type="hidden" name="sort" value="titleAscending">

            <!-- Submit -->
            <div class="form-group row">
                <div class="col-md-offset-2 col-md-6">
                <button type="submit" value="search" class="btn btn-default">Search</button>
                </div>
            </div>
        </form>
    </div>

</body>
</html>
