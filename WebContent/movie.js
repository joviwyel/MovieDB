/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */
/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 * */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    //url = decodeURI(url);
    // url = encodeURI(encodeURI(url));
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);

    console.log(results);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    //return decodeURIComponent(results[2].replace(/\+/g, " "));
    return results[2];
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let starTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_name"] +     // display star_name for the link text
            '</a>' +
            "</th>";

        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_dir"] + "</th>";

        rowHTML += "<th>"
        if (resultData[i]["genre_name1"] != "N/A") {
            rowHTML +=
                '<a href="movie.html?genre=' + resultData[i]['genre_name1'] + '">'
                + resultData[i]["genre_name1"] +
                '</a>';
        }
        if (resultData[i]["genre_name2"] != "N/A") {
            rowHTML += ",  " +
                '<a href="movie.html?genre=' + resultData[i]['genre_name2'] + '">'
                + resultData[i]["genre_name2"] +
                '</a>';
        }
        if (resultData[i]["genre_name3"] != "N/A") {
            rowHTML += ",  " +
                '<a href="movie.html?genre=' + resultData[i]['genre_name3'] + '">'
                + resultData[i]["genre_name3"] +
                '</a>';
        }
        rowHTML += "</th>"

        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-star.html?id=' + resultData[i]['star_id1'] + '">'
            + resultData[i]["star_name1"] +     // display star_name for the link text
            '</a>' + ",  "  +
            '<a href="single-star.html?id=' + resultData[i]['star_id2'] + '">'
            + resultData[i]["star_name2"] +     // display star_name for the link text
            '</a>' + ",  "  +
            '<a href="single-star.html?id=' + resultData[i]['star_id3'] + '">'
            + resultData[i]["star_name3"] +     // display star_name for the link text
            '</a>' + "  "  +
            "</th>";

        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
if(getParameterByName('sortby1') == null){
    if(getParameterByName('genre') != null) {
        let genreURL = getParameterByName('genre');
            // Makes the HTTP GET request and registers on success callback function handleStarResult
        jQuery.ajax({
            dataType: "json", // Setting return data type
            method: "GET", // Setting request method
            url: "api/movie?genre=" + genreURL, // Setting request url, which is mapped by StarsServlet in Stars.java
            success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
        });
    }
    else if(getParameterByName('letter') != null){
        let letterURL = getParameterByName('letter');
        // Makes the HTTP GET request and registers on success callback function handleStarResult
        jQuery.ajax({
            dataType: "json", // Setting return data type
            method: "GET", // Setting request method
            url: "api/movie?letter=" + letterURL, // Setting request url, which is mapped by StarsServlet in Stars.java
            success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
        });

    }
    else {
        let titleURL = getParameterByName('title');
        let yearURL = getParameterByName('year');
        let dirURL = getParameterByName('director');
        let starURL = getParameterByName('star');
        // Makes the HTTP GET request and registers on success callback function handleStarResult
        jQuery.ajax({
            dataType: "json", // Setting return data type
            method: "GET", // Setting request method
            url: "api/movie?title=" + titleURL + "&year=" + yearURL + "&director=" + dirURL + "&star=" + starURL,
            success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
        });
    }
}
else{
    if(getParameterByName('genre') != "") {
        let genreURL = getParameterByName('genre');
        let sortby1URL = getParameterByName('sortby1');
        let order1URL = getParameterByName('order1');
        let sortby2URL = getParameterByName('sortby2');
        let order2URL = getParameterByName('order2');
        // Makes the HTTP GET request and registers on success callback function handleStarResult
        jQuery.ajax({
            dataType: "json", // Setting return data type
            method: "GET", // Setting request method
            url: "api/movie?genre=" + genreURL + "&sortby1=" + sortby1URL + "&order1=" + order1URL + "&sortby2=" + sortby2URL + "&order2=" + order2URL,
            success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
        });
    }
    else if(getParameterByName('letter') != ""){
        let letterURL = getParameterByName('letter');
        // Makes the HTTP GET request and registers on success callback function handleStarResult
        let sortby1URL = getParameterByName('sortby1');
        let order1URL = getParameterByName('order1');
        let sortby2URL = getParameterByName('sortby2');
        let order2URL = getParameterByName('order2');
        jQuery.ajax({
            dataType: "json", // Setting return data type
            method: "GET", // Setting request method
            url: "api/movie?letter=" + letterURL + "&sortby1=" + sortby1URL + "&order1=" +
                order1URL + "&sortby2=" + sortby2URL + "&order2=" + order2URL,
            success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
        });

    }
    else {
        let titleURL = getParameterByName('title');
        let yearURL = getParameterByName('year');
        let dirURL = getParameterByName('director');
        let starURL = getParameterByName('star');
        let sortby1URL = getParameterByName('sortby1');
        let order1URL = getParameterByName('order1');
        let sortby2URL = getParameterByName('sortby2');
        let order2URL = getParameterByName('order2');
        // Makes the HTTP GET request and registers on success callback function handleStarResult
        jQuery.ajax({
            dataType: "json", // Setting return data type
            method: "GET", // Setting request method
            url: "api/movie?title=" + titleURL + "&year=" + yearURL + "&director=" + dirURL + "&star=" + starURL +
                "&sortby1=" + sortby1URL + "&order1=" + order1URL + "&sortby2=" + sortby2URL + "&order2=" + order2URL,
            success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
        });
    }

}
