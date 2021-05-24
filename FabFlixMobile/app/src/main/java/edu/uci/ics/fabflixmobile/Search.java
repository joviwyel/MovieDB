package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Search extends ActionBarActivity {

    private EditText searchTitle;
    private Button searchButton;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    private final String host = "ec2-18-222-143-158.us-east-2.compute.amazonaws.com";
    private final String port = "8443";
    private final String domain = "cs122b-spring21-team-10-p1";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("search", "in search");
        super.onCreate(savedInstanceState);
        // upon creation, inflate and initialize the layout
        setContentView(R.layout.search);
        searchTitle = findViewById(R.id.searchTitle);
        searchButton = findViewById(R.id.searchButton);

        //assign a listener to call a function to handle the user request when clicking a button
        searchButton.setOnClickListener(view -> search());
    }

    public void search() {

        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is GET
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie?year=&director=&star=&title=" + searchTitle.getText().toString() + "&mobile=1",
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value
                    Log.d("search: ", response);
                    if (response != null) {
                        // initialize the activity(page)/destination
                        Intent listPage = new Intent(Search.this, ListViewActivity.class);
                        // Pass json array to list page
                        listPage.putExtra("movieJAString", response);
                        listPage.putExtra("movieTitle", searchTitle.getText().toString());
                        listPage.putExtra("pageNum", 0);
                        // activate the list page
                        startActivity(listPage);
                    }

                },
                error -> {
                    // error
                    Log.d("search.error", error.toString());
                });
        // important: queue.add is where the search request is actually sent
        queue.add(searchRequest);
    }
}
