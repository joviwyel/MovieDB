/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

/** DROP DOWN BUTTON
*  When the user clicks on the button,
*  toggle between hiding and showing the dropdown content
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
//
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
        let thisId = resultData[i]['movie_id'];
        console.log(thisId);
        rowHTML += "<th>" + '<button onclick = addToCart(\''+thisId + '\')' + ">" + "Add to Cart" + '</button>' + "</th>";
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
function addToCart(thisId){
    alert("Added to Shopping Cart!");
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/addto?addId=" + thisId, // Setting request url, which is mapped by StarsServlet in Stars.java
    });
}
function getPageSize(){
    let e = document.getElementById("pageSize");
    return e.options[e.selectedIndex].text;
}
function getOrder1(){
    let e = document.getElementById("order1");
    return e.options[e.selectedIndex].text;
}
function getOrder2(){
    let e = document.getElementById("order2");
    return e.options[e.selectedIndex].text;
}
function getSortby1(){
    let e = document.getElementById("sortby1");
    return e.options[e.selectedIndex].text;
}
function getSortby2(){
    let e = document.getElementById("sortby2");
    return e.options[e.selectedIndex].text;
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
if(getParameterByName('sortby1') == null){
    if(getParameterByName('genre') != null) {
        let genreURL = getParameterByName('genre');
        let pageNumURL = 0;
        if(getParameterByName('pageNum') != null){
            pageNumURL = getParameterByName('pageNum');
        }
            // Makes the HTTP GET request and registers on success callback function handleStarResult
            jQuery.ajax({
                dataType: "json", // Setting return data type
                method: "GET", // Setting request method
                url: "api/movie?genre=" + genreURL + "&pageNum=" + pageNumURL, // Setting request url, which is mapped by StarsServlet in Stars.java
                success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
            });

    }
    else if(getParameterByName('letter') != null){
        let letterURL = getParameterByName('letter');
        let pageNumURL = 0;
        if(getParameterByName('pageNum') != null){
            pageNumURL = getParameterByName('pageNum');
        }

            // Makes the HTTP GET request and registers on success callback function handleStarResult
            jQuery.ajax({
                dataType: "json", // Setting return data type
                method: "GET", // Setting request method
                url: "api/movie?letter=" + letterURL + "&pageNum=" + pageNumURL, // Setting request url, which is mapped by StarsServlet in Stars.java
                success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
            });


    }
    else {
        let titleURL = getParameterByName('title');
        let yearURL = getParameterByName('year');
        let dirURL = getParameterByName('director');
        let starURL = getParameterByName('star');
        let pageNumURL = 0;
        if(getParameterByName('pageNum') != null){
            pageNumURL = getParameterByName('pageNum');
        }

            // Makes the HTTP GET request and registers on success callback function handleStarResult
            jQuery.ajax({
                dataType: "json", // Setting return data type
                method: "GET", // Setting request method
                url: "api/movie?title=" + titleURL + "&year=" + yearURL + "&director=" + dirURL + "&star=" + starURL + "&pageNum=" + pageNumURL,
                success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
            });

    }
}
else{
    if(getParameterByName('genre') != "") {
        let genreURL = getParameterByName('genre');
        let sortby1URL = getSortby1();
        let order1URL = getOrder1();
        let sortby2URL = getSortby2();
        let order2URL = getOrder2();
        let pageSizeURL = getPageSize();
        let pageNumURL = 0;
        if(getParameterByName('pageNum') != null){
            pageNumURL = getParameterByName('pageNum');
        }
            // Makes the HTTP GET request and registers on success callback function handleStarResult
            jQuery.ajax({
                dataType: "json", // Setting return data type
                method: "GET", // Setting request method
                url: "api/movie?genre=" + genreURL + "&sortby1=" + sortby1URL + "&order1=" +
                    order1URL + "&sortby2=" + sortby2URL + "&order2=" + order2URL
                    + "&pageNum=" + pageNumURL + "&pageSize=" + pageSizeURL,
                success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
            });

    }
    else if(getParameterByName('letter') != ""){
        let letterURL = getParameterByName('letter');
        // Makes the HTTP GET request and registers on success callback function handleStarResult
        let sortby1URL = getSortby1();
        let order1URL = getOrder1();
        let sortby2URL = getSortby2();
        let order2URL = getOrder2();
        let pageSizeURL = getPageSize();
        let pageNumURL = 0;
        if(getParameterByName('pageNum') != null){
            pageNumURL = getParameterByName('pageNum');
        }
            jQuery.ajax({
                dataType: "json", // Setting return data type
                method: "GET", // Setting request method
                url: "api/movie?letter=" + letterURL + "&sortby1=" + sortby1URL + "&order1=" +
                    order1URL + "&sortby2=" + sortby2URL + "&order2=" + order2URL + "&pageNum=" + pageNumURL
                + "&pageSize=" + pageSizeURL,
                success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
            });


    }
    else {
        let titleURL = getParameterByName('title');
        let yearURL = getParameterByName('year');
        let dirURL = getParameterByName('director');
        let starURL = getParameterByName('star');
        let sortby1URL = getSortby1();
        let order1URL = getOrder1();
        let sortby2URL = getSortby2();
        let order2URL = getOrder2();
        let pageSizeURL = getPageSize();
        let pageNumURL = 0;
        if(getParameterByName('pageNum') != null){
            pageNumURL = getParameterByName('pageNum');
        }

            // Makes the HTTP GET request and registers on success callback function handleStarResult
            jQuery.ajax({
                dataType: "json", // Setting return data type
                method: "GET", // Setting request method
                url: "api/movie?title=" + titleURL + "&year=" + yearURL + "&director=" + dirURL + "&star=" + starURL +
                    "&sortby1=" + sortby1URL + "&order1=" + order1URL + "&sortby2=" + sortby2URL + "&order2=" + order2URL +
                    "&pageNum=" + pageNumURL + "&pageSizeURL" + pageSizeURL,
                success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
            });
        }


}
