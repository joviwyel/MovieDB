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
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleMovieViewActivity extends Activity {
    private TextView movieTitleV;
    private TextView movieYear;
    private TextView movieDirector;
    private TextView movieGenre;
    private TextView movieStar;

    private final String host = "ec2-18-222-143-158.us-east-2.compute.amazonaws.com";
    private final String port = "8443";
    private final String domain = "cs122b-spring21-team-10-p1";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("--SINGLE: ", "in single view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlemovieview);
        movieTitleV = findViewById(R.id.movieTitle);
        movieYear = findViewById(R.id.movieYear);
        movieDirector = findViewById(R.id.movieDirector);
        movieGenre = findViewById(R.id.movieGenre);
        movieStar = findViewById(R.id.movieStar);

        Bundle bundle = getIntent().getExtras();
        String movieJAString = bundle.getString("movieJAString");
        String movieTitle = bundle.getString("titleClicked");

        Log.d("--SINGLE:", movieJAString);
        Log.d("--SINGLE:", movieTitle);

        // Search for matching movie title in movieJAString
        final ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONArray movieJA = new JSONArray(movieJAString);
            for (int i = 0; i < movieJA.length() - 1; i++) {
                if (movieJA.getJSONObject(i).getString("movie_name").equals(movieTitle)){
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

                    String genreStr = "";
                    if(!genre1.equals("N/A")){
                        genreStr += genre1;
                    }
                    if(!genre2.equals("N/A")){
                        genreStr += ", " + genre2;
                    }
                    if(!genre3.equals("N/A")){
                        genreStr += ", " + genre3;
                    }
                    movies.add(new Movie(mid, title, year, director, genre1, genre2, genre3,
                            star1, star2, star3));

                    movieTitleV.setText("Title: " + title);
                    movieYear.setText("Year: " + year);
                    movieDirector.setText("Director: " + director);
                    movieGenre.setText("Genre: " + genreStr);

                    // Get all stars with mid
                    final RequestQueue queue = NetworkManager.sharedManager(this).queue;
                    final StringRequest starRequest = new StringRequest(
                            Request.Method.GET,
                            baseURL + "/api/single-movie?id=" + mid + "&mobile=1",
                            response -> {
                                try{
                                    Log.d("--SINGLE: ", response);
                                    JSONArray starJA = new JSONArray(response);
                                    JSONArray starArray = starJA.getJSONObject(0).getJSONArray("starName");
                                    String starStr = "";
                                    if (starArray != null) {
                                        for (int n = 0; n < starArray.length() - 1; n++){
                                            Log.d("--SINGLE star name: ", starArray.getString(n));
                                            starStr += starArray.getString(n) + ", ";
                                        }
                                        starStr += starArray.getString(starArray.length() - 1);
                                        movieStar.setText("Star: " + starStr);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            },
                            error -> {
                                // error
                                error.printStackTrace();
                                Log.d("star.error", error.toString());
                            });
                    queue.add(starRequest);

                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
