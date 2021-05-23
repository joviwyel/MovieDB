package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.ArrayList;

public class ListViewActivity extends Activity {
    private Button nextButton;
    private Button prevButton;
    private String movieJAString;
    private final String host = "10.0.2.2";
    private final String port = "8080";
    private final String domain = "spring21-FabFlix";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("list view: ", "in list view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        nextButton = findViewById(R.id.next);
        prevButton = findViewById(R.id.prev);

        Bundle bundle = getIntent().getExtras();
        movieJAString = bundle.getString("movieJAString");
        String movieTitle = bundle.getString("movieTitle");

        Log.d("--MAIN:", movieJAString);
        Log.d("--MAIN:", movieTitle);
        Log.d("--MAIN:", "" + bundle.getInt("pageNum"));

        nextButton.setOnClickListener(view -> next(movieTitle));
        prevButton.setOnClickListener(view -> prev(movieTitle));
        getMovie(movieJAString);
    }

    public void next(String movieTitle) {
        Log.d("list view activity: ", "next");
        Bundle bundle = getIntent().getExtras();
        int pageNum = bundle.getInt("pageNum");
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest nextRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie?year=&director=&star=&title=" + movieTitle + "&pageNum=" + (pageNum+1) + "&mobile=1",
                response -> {
                    Log.d("next: ", response);
                    if (response != null) {
                        Intent listPage = new Intent(ListViewActivity.this, ListViewActivity.class);
                        listPage.putExtra("movieJAString", response);
                        listPage.putExtra("movieTitle", movieTitle);
                        listPage.putExtra("pageNum", pageNum + 1);
                        startActivity(listPage);
                    }
                },
                error -> {
                    // error
                    Log.d("search.error", error.toString());
                });
        // important: queue.add is where the search request is actually sent
        queue.add(nextRequest);
    }

    public void prev(String movieTitle) {
        Log.d("list view activity: ", "prev");
        Bundle bundle = getIntent().getExtras();
        int pageNum = bundle.getInt("pageNum");
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest nextRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie?year=&director=&star=&title=" + movieTitle + "&pageNum=" + (pageNum-1) + "&mobile=1",
                response -> {
                    Log.d("prev: ", response);
                    if (response != null) {
                        Intent listPage = new Intent(ListViewActivity.this, ListViewActivity.class);
                        listPage.putExtra("movieJAString", response);
                        listPage.putExtra("movieTitle", movieTitle);
                        listPage.putExtra("pageNum", pageNum - 1);
                        startActivity(listPage);
                    }
                },
                error -> {
                    // error
                    Log.d("search.error", error.toString());
                });
        // important: queue.add is where the search request is actually sent
        queue.add(nextRequest);
    }

    // Retrieve movie data from the backend server
    public void getMovie(String movieJAString){
        Log.d("list view activity: ", "get movie");
        final ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONArray movieJA = new JSONArray(movieJAString);

            // Check if has next
            if (!movieJA.getJSONObject(movieJA.length() - 1).getBoolean("more")){
                nextButton.setEnabled(false);
            }
            else{
                nextButton.setEnabled(true);
            }

            // Check if has prev
            Bundle bundle = getIntent().getExtras();
            if((bundle.getInt("pageNum")-1) < 0){
                prevButton.setEnabled(false);
            }
            else{
                prevButton.setEnabled(true);
            }

            for (int i = 0; i < movieJA.length() - 1; i++) {
                String mid = movieJA.getJSONObject(i).getString("movie_id");
                String title = movieJA.getJSONObject(i).getString("movie_name");
                String year = movieJA.getJSONObject(i).getString("movie_year");
                String director = movieJA.getJSONObject(i).getString("movie_dir");
                String genre1;
                String genre2;
                String genre3;
                String star1;
                String star2;
                String star3;

                // Check if star_name or genre_name exists
                if (movieJA.getJSONObject(i).has("star_name1")) {
                    star1 = movieJA.getJSONObject(i).getString("star_name1");
                } else{
                    star1 = "N/A";
                }
                if (movieJA.getJSONObject(i).has("star_name2")) {
                    star2 = movieJA.getJSONObject(i).getString("star_name2");
                } else{
                    star2 = "N/A";
                }
                if (movieJA.getJSONObject(i).has("star_name3")) {
                    star3 = movieJA.getJSONObject(i).getString("star_name3");
                } else{
                    star3 = "N/A";
                }

                if (movieJA.getJSONObject(i).has("genre_name1")) {
                    genre1 = movieJA.getJSONObject(i).getString("genre_name1");
                } else{
                    genre1 = "N/A";
                }
                if (movieJA.getJSONObject(i).has("genre_name2")) {
                    genre2 = movieJA.getJSONObject(i).getString("genre_name2");
                } else{
                    genre2 = "N/A";
                }
                if (movieJA.getJSONObject(i).has("genre_name3")) {
                    genre3 = movieJA.getJSONObject(i).getString("genre_name3");
                } else{
                    genre3 = "N/A";
                }

                movies.add(new Movie(mid, title, year, director, genre1, genre2, genre3,
                        star1, star2, star3));
            }
            MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
            ListView listView = findViewById(R.id.list);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void singleMoviePage(View view){
        Log.d("--SINGLE ", "move into single movie");
        Intent singleMovie = new Intent(ListViewActivity.this, SingleMovieViewActivity.class);
        singleMovie.putExtra("movieJAString", movieJAString);
        singleMovie.putExtra("titleClicked", ((TextView)view.findViewById(R.id.title)).getText().toString());
        startActivity(singleMovie);
    }
}

