package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private final ArrayList<Movie> movies;

    public MovieListViewAdapter(ArrayList<Movie> movies, Context context) {
        super(context, R.layout.row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Movie movie = movies.get(position);

        /* TextView: A UI element that displays text to the user
         *  findViewById(): Looks through view hierarchy and returns reference to a
         *                  view requested viewId
         */
        Log.d("adapter: ", "in adapter");
        TextView titleView = view.findViewById(R.id.title);
        TextView yearView = view.findViewById(R.id.year);
        TextView directorView = view.findViewById(R.id.director);
        TextView starView = view.findViewById(R.id.star);
        TextView genreView = view.findViewById(R.id.genre);
        Log.d("adapter: ", movie.getTitle().toString());

        titleView.setText(movie.getTitle());
        yearView.setText("Year: " + movie.getYear());
        directorView.setText("Director: " + movie.getDirector());

        String starList = "";
        if (!movie.getStar1().equals("N/A")){
            starList += "Star: " + movie.getStar1();
        }
        if (!movie.getStar2().equals("N/A")){
            starList += ", " + movie.getStar2();
        }
        if (!movie.getStar3().equals("N/A")) {
            starList += ", " + movie.getStar3();
        }
        starView.setText(starList);

        String genreList = "";
        if (!movie.getGenre1().equals("N/A")){
            genreList += "Genre: " + movie.getGenre1();
        }
        if (!movie.getGenre2().equals("N/A")){
            genreList += ", " + movie.getGenre2();
        }
        if (!movie.getGenre3().equals("N/A")) {
            genreList += ", " + movie.getGenre3();
        }
        genreView.setText(genreList);

        return view;
    }
}