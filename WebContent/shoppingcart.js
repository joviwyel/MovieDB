
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
    console.log(results);
    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    // Find the empty table body by id "star_table_body"
    let CartTableBodyElement = jQuery("#cart_table_body");
    for (let i = 0; i < resultData.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";

        let thisId = i+1;
        let del = "delete";

        rowHTML += "<th>" + (i+1) + "</th>";
        rowHTML += "<th>" + resultData[i]["title"] + "</th>";
        rowHTML += "<th>" + resultData[i]["qty"] + "</th>" ;

        rowHTML += "<th>" + '<a href="shoppingcart.html?index=' + thisId +
            "&qty=0" + '">'+ del +'</a>'+"</th>";
        rowHTML += "<th>$ " + resultData[i]["price"] + "</th>";


        rowHTML += "</tr>";

        console.log(rowHTML);
        CartTableBodyElement.append(rowHTML);
    }
    let totalTableBodyElement = jQuery("#total_table_body");
    let totalHTML = "";
    totalHTML += "<th>$ " + resultData[0]["total"] + "</th>";

    totalTableBodyElement.append(totalHTML);

}


if(getParameterByName('index') == null){
    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "api/shoppingcart", // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
}
else{
    let indexURL = getParameterByName('index');
    let qtyURL = getParameterByName('qty');
    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "api/shoppingcart?index=" + indexURL + "&qty=" + qtyURL, // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
}
