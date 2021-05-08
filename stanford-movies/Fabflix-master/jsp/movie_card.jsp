<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="css/movie_card.css" rel="stylesheet" />
<table>
    <tr>
        <td>
            <img src="${movie.bannerUrl}" width="150" height="200" alt="No Movie Image" id="movie_poster"/>
        </td>
        <td>
            <h3 id="movieCardAnchor">Title: ${movie.title}</h3>
            <p>Year Released: ${movie.year}</p>
            <p>Director: ${movie.director}</p>
            <p>Stars:
                <c:forEach var="star" items="${movie.getStars()}" varStatus="status">
                    <p><a href="star?id=${star.id}">${star.getFullName()}</a></p>
                </c:forEach>
            </p>
        </td>
    </tr>
</table>
