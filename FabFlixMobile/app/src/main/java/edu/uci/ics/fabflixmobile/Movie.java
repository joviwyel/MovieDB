package edu.uci.ics.fabflixmobile;

import android.util.Log;

import java.lang.reflect.Array;

public class Movie {
    private final String id;
    private final String title;
    private final String year;
    private final String director;
    private final String genre1;
    private final String star1;
    private final String genre2;
    private final String star2;
    private final String genre3;
    private final String star3;

    public Movie(String id, String title, String year, String director,
                 String genre1, String genre2, String genre3,
                 String star1, String star2, String star3) {

        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.genre1 = genre1;
        this.star1 = star1;
        this.genre2 = genre2;
        this.star2 = star2;
        this.genre3 = genre3;
        this.star3 = star3;

        Log.d("list: (movies)", title);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getGenre1() {
        return genre1;
    }

    public String getStar1() {
        return star1;
    }

    public String getGenre2() {
        return genre2;
    }

    public String getStar2() {
        return star2;
    }

    public String getGenre3() {
        return genre3;
    }

    public String getStar3() {
        return star3;
    }
}