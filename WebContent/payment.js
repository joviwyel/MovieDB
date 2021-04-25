/**
 * Get total price from Cart Servlet
 */
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
    let totalTableBodyElement = jQuery("#total_table_body");
    let totalHTML = "";
    totalHTML += "<th>$ " + resultData[0]["total"] + "</th>";
    totalTableBodyElement.append(totalHTML);
}

jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/shoppingcart", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});

/**
 * Send form data to Credit Card Servlet
 * Receive messages from Credit Card Servlet
 */
let credit_card_form = $("#credit_card_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleFormResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle payment response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If payment succeeds, it will redirect the user to confirmation.html
    if (resultDataJson["status"] === "success") {
        window.location.replace("confirmation.html");
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "payment_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#payment_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitCreditForm(formSubmitEvent) {
    console.log("submit credit card form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/creditCard", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: credit_card_form.serialize(),
            success: handleFormResult
        }
    );
}

// Bind the submit action of the form to a handler function
credit_card_form.submit(submitCreditForm);
