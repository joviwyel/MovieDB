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
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let genreTableBodyElement = jQuery("#genre_table_body");
    let letterTableBodyElement = jQuery("#letter_table_body");
    let letter_list = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
    'Y', 'Z'];
    for(let j = 0; j < letter_list.length; j++){
        let rowLetter = "";
        if( (j + 1) % 10 == 0){
            rowLetter += "<tr><td></td></tr>";
            letterTableBodyElement.append(rowLetter);
        }

        rowLetter += '<a href = "movie.html?letter=' + letter_list[j] + '">' +' '
            + letter_list[j] + ' ' + '</a>';
        letterTableBodyElement.append(rowLetter);
    }

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject

        let rowHTML = "";
        if( (i+1)%6 == 0){
            rowHTML += "<tr><td></td></tr>";
            genreTableBodyElement.append(rowHTML);
        }
        rowHTML +=

            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="movie.html?genre=' + resultData[i]['genre_name'] + '">' + ' _' +
            resultData[i]['genre_name'] + '_ '+
            '</a>' ;

        // Append the row created to the table body, which will refresh the page
        genreTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/brows", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});