
function handleResult(resultData) {

    console.log("handleResult: populating star info from resultData");
    let InfoTableBodyElement = jQuery("#info_table_body");
    let InfoTableElement = jQuery("#info_table");
    InfoTableElement.append("<p>Customer Id: " + resultData[0]["customerId"] + "</p>");
    for (let i = 0; i < resultData.length; i++) {
        let rowHTML = "";

        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["id"] + "</th>" ;
        rowHTML += "<th>" + resultData[i]["title"] + "</th>" ;
        rowHTML += "<th>" + resultData[i]["qty"] + "</th>" ;
        rowHTML += "</tr>";
        InfoTableBodyElement.append(rowHTML);
    }
    let total = "<th>$" + resultData[0]["Total"] + "</th>" ;
    InfoTableBodyElement.append(total);
}

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/confirmation", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
