function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleResult(resultData) {
    // console.log(resultData[0]["message"]);
    $("#insert_message").text(resultData[0]["message"]);
    // console.log(resultData[1]["message_movie"]);
    $("#insert_movie_message").text(resultData[1]["message_movie"]);

    // console.log("handleResult: populating metadata info from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let metadataTableBodyElement = jQuery("#metadata_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 2; i < resultData.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["table_name"] + "</th>";
        rowHTML += "<th>" + resultData[i]["column_name"] + "</th>";
        rowHTML += "<th>" + resultData[i]["data_type"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        metadataTableBodyElement.append(rowHTML);
    }

}
if(getParameterByName('name') == null && getParameterByName('title') == null){
    // console.log("Dashboard.js: No star or movie inserting");
    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "_dashboard", // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
}
else if (getParameterByName('name') != null){
    let nameURL = getParameterByName('name');
    let birthYearURL = getParameterByName('birthYear');
    // console.log("Dashboard.js: Star inserting");
    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "_dashboard?name=" + nameURL + "&birthYear=" + birthYearURL, // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
}
else{
    let titleURL = getParameterByName('title');
    let yearURL = getParameterByName('year');
    let directorURL = getParameterByName('director');
    let starURL = getParameterByName('star');
    let genreURL = getParameterByName('genre');
    let ratingURL = getParameterByName('rating');
    // console.log("Dashboard.js: Movie inserting");
    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "_dashboard?title=" + titleURL + "&year=" + yearURL + "&director=" +
            directorURL + "&star=" + starURL + "&genre=" + genreURL + "&rating=" + ratingURL,
        success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
}