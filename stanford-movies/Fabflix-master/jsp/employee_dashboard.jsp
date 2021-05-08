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
        padding-top: 50px;
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
                <h3>Employee Dashboard</h3>
            </div>
        </div>
        <hr />


        <!-- insert new star form -->       
        <h4>Insert a new star:</h4>
        <br />
        <form action="_dashboard" method="POST" role="form">

            <!-- Hidden field, used to specify insert new star -->
            <input type="hidden" name="postAction" value="insertStar">

            <!-- name -->
            <div class="form-group row">
                <label for="starFullName" class="col-md-2 form-control-label">Name</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="starFullName" id="starFullName" required placeholder="Star Full Name (required)">
                </div>
            </div>

            <!-- dob -->
            <div class="form-group row">
                <label for="starDob" class="col-md-2">Date of Birth</label>
                <div class="col-md-4">
                    <input type="date" class="form-control" name="starDob" id="starDob" placeholder="Dob (yyyy-MM-dd)">
                </div>
            </div>

            <!-- photo url -->
            <div class="form-group row">
                <label for="starPhotoUrl" class="col-md-2 form-control-label">Photo URL</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="starPhotoUrl" id="starPhotoUrl" placeholder="Photo URL">
                </div>
            </div>

            <!-- Submit -->
            <div class="form-group row">
                <div class="col-md-offset-2 col-md-6">
                <button type="submit" value="insertStar" class="btn btn-default">Add New Star</button>
                </div>
            </div>
        </form>
        <hr />

        <!-- add info to movie form -->
        <h4>Add information to a movie:</h4>
        <br />
        <form action="_dashboard" method="POST" role="form">

            <!-- Hidden field, used to specify insert new star -->
            <input type="hidden" name="postAction" value="updateMovie">

            <!-- movie title -->
            <div class="form-group row">
                <label for="movieTitle" class="col-md-2 form-control-label">Movie Title</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="movieTitle" id="movieTitle" placeholder="Movie Title" required>
                </div>
            </div>

            <!-- year -->
            <div class="form-group row">
                <label for="movieYear" class="col-md-2">Year</label>
                <div class="col-md-4">
                    <input type="number" class="form-control" name="movieYear" id="movieYear" placeholder="Year" required>
                </div>
            </div>

            <!-- director -->
            <div class="form-group row">
                <label for="movieDirector" class="col-md-2 form-control-label">Director</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="movieDirector" id="movieDirector" placeholder="Director" required>
                </div>
            </div>

            <!-- banner url -->
            <div class="form-group row">
                <label for="movieBannerUrl" class="col-md-2 form-control-label">Banner URL</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="movieBannerUrl" id="movieBannerUrl" placeholder="Banner URL">
                </div>
            </div>

            <!-- trailer url -->
            <div class="form-group row">
                <label for="movieTrailerUrl" class="col-md-2 form-control-label">Trailer URL</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="movieTrailerUrl" id="movieTrailerUrl" placeholder="Trailer URL">
                </div>
            </div>

            <!-- star name -->
            <div class="form-group row">
                <label for="starFullName" class="col-md-2 form-control-label">Star Name</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="starFullName" id="starFullName" placeholder="Star Full Name" required>
                </div>
            </div>

            <!-- dob -->
            <div class="form-group row">
                <label for="starDob" class="col-md-2">Star Date of Birth</label>
                <div class="col-md-4">
                    <input type="date" class="form-control" name="starDob" id="starDob" placeholder="Dob (yyyy-MM-dd)">
                </div>
            </div>

            <!-- photo url -->
            <div class="form-group row">
                <label for="starPhotoUrl" class="col-md-2 form-control-label">Star Photo URL</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="starPhotoUrl" id="starPhotoUrl" placeholder="Photo URL">
                </div>
            </div>

            <!-- genre -->
            <div class="form-group row">
                <label for="movieGenre" class="col-md-2 form-control-label">Genre</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="movieGenre" id="movieGenre" placeholder="Genre" required>
                </div>
            </div>

            <!-- Submit -->
            <div class="form-group row">
                <div class="col-md-offset-2 col-md-6">
                <button type="submit" value="updateMovie" class="btn btn-default">Update Movie</button>
                </div>
            </div>

        </form>
        <hr />

        <!-- get database metadata -->
        <h4>Get database metadata:</h4>
        <a href="_dashboard/db_metadata" class="btn btn-default">Get Metadata</a>
        <div class="pad-bottom"></div>

    </div>

</body>
</html>