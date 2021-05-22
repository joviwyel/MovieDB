package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.ArrayList;

public class ListViewActivity extends Activity {
    private final String host = "10.0.2.2";
    private final String port = "8080";
    private final String domain = "spring21-FabFlix";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("list view: ", "in list view");
        Bundle bundle = getIntent().getExtras();
        String movieJAString = bundle.getString("movieJAString");
        Log.d("list view : ", movieJAString);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        // TODO: this should be retrieved from the backend server
        // final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONArray movieJA = new JSONArray(movieJAString);
            Log.d("length", ""+movieJA.length());
            Log.d("list: ", "before for loop");
            for (int i = 0; i < 11; i++) {
                String mid = movieJA.getJSONObject(i).getString("movie_id");
                String title = movieJA.getJSONObject(i).getString("movie_name");
                String year = movieJA.getJSONObject(i).getString("movie_year");
                String director = movieJA.getJSONObject(i).getString("movie_dir");
                String genre1 = movieJA.getJSONObject(i).getString("genre_name1");
                String genre2 = movieJA.getJSONObject(i).getString("genre_name2");
                String genre3 = movieJA.getJSONObject(i).getString("genre_name3");
                String star1 = movieJA.getJSONObject(i).getString("star_name1");
                String star2 = movieJA.getJSONObject(i).getString("star_name2");
                String star3 = movieJA.getJSONObject(i).getString("star_name3");

                movies.add(new Movie(mid, title, year, director, genre1, genre2, genre3, star1, star2, star3));

                Log.d("list: (title act)", title);
            }
            MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
            ListView listView = findViewById(R.id.list);
            listView.setAdapter(adapter);

        }catch(Exception e){

        }
/*
protected void onNext() {
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // this becomes a function, and we call it when we next, prev, from search
        final StringRequest movieListRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie?letter=z&mobile=1&pageNum=0",
                response -> { // parsing to new array list
                    // TODO: pass query from search activity for initial search, below is for next
                    try {
                        JSONArray movieJA = new JSONArray(response);
                        Log.d("list: ", "before for loop");
                        for (int i = 0; i < 3; i++) {
                            String mid = movieJA.getJSONObject(i).getString("movie_id");
                            String title = movieJA.getJSONObject(i).getString("movie_name");
                            String year = movieJA.getJSONObject(i).getString("movie_year");
                            String director = movieJA.getJSONObject(i).getString("movie_dir");
                            String genre1 = movieJA.getJSONObject(i).getString("genre_name1");
                            String genre2 = movieJA.getJSONObject(i).getString("genre_name2");
                            String genre3 = movieJA.getJSONObject(i).getString("genre_name3");
                            String star1 = movieJA.getJSONObject(i).getString("star_name1");
                            String star2 = movieJA.getJSONObject(i).getString("star_name2");
                            String star3 = movieJA.getJSONObject(i).getString("star_name3");

                            movies.add(new Movie(title, year));

                            Log.d("list: (title act)", title);
                        }
                        // render movies, add adapter here
                        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
                        ListView listView = findViewById(R.id.list);
                        listView.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.d("list:", "error in catch");
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("list: error", error.toString());
                }
        );
        // queue.add(movieListRequest);

        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = movies.get(position);
            String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
*/
        //queue.add(movieListRequest);
    }
}
