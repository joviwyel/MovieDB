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

    console.log("handleResult: populating metadata info from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let metadataTableBodyElement = jQuery("#metadata_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < resultData.length; i++) {
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

// Makes the HTTP GET request and registers on success callback function handleResult
console.log("Dashboard.js: Before ajax");
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "_dashboard", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});