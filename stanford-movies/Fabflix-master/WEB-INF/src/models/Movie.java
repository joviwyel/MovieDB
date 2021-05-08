package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Movie {
    public Integer id;
    public String title;
    public Integer year;
    public String director;
    public String bannerUrl;
    public String trailerUrl;

    public List<Star> stars;
    public List<Genre> genres;

    public Movie() {
        stars = new ArrayList<Star>();
        genres = new ArrayList<Genre>();
    }

    public Movie(Integer id, String title) {
        super();
        this.id = id;
        this.title = title;
        stars = new ArrayList<Star>();
        genres = new ArrayList<Genre>();
    }

    public Movie(Integer id, String title, Integer year, String director, String bannerUrl, String trailerUrl) {
        super();
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.bannerUrl = bannerUrl;
        this.trailerUrl = trailerUrl;
        stars = new ArrayList<Star>();
        genres = new ArrayList<Genre>();
    }


    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void addStarIfAbsent(Star star) {
        if (!this.containsStar(star)) {
            this.addStar(star);
        }
    }

    public boolean containsStar(Star star) {
        for (Star previouslyAddedStar : this.getStars()) {
            if (previouslyAddedStar.getId() == star.getId()) {
                return true;
            }
        }
        return false;
    }

    public void addGenreIfAbsent(Genre genre) {
        if (!this.containsGenre(genre)) {
            this.addGenre(genre);
        }
    }

    public boolean containsGenre(Genre genre) {
        for (Genre previouslyAddedGenre : this.getGenres()) {
            if (previouslyAddedGenre.getId() == genre.getId()) {
                return true;
            }
        }
        return false;
    }

    public void addStar(Star star) {
        this.stars.add(star);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public List<Star> getStars() { return stars; }

    public List<Genre> getGenres() { return genres; }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setBannerUrl(String bannerUrl) {
        if (bannerUrl != null && !bannerUrl.toLowerCase().matches("^\\w+://.*")) {  // Add http
            bannerUrl = "http://" + bannerUrl;
        }
        this.bannerUrl = bannerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        if (trailerUrl != null && !trailerUrl.toLowerCase().matches("^\\w+://.*")) {
            trailerUrl = "http://" + trailerUrl;
        }
        this.trailerUrl = trailerUrl;
    }

    public void setStars(List<Star> stars) { this.stars = stars; }

    public void setGenres(List<Genre> genres) { this.genres = genres; }

    @Override
    public String toString() {
        String str = "{\n" + "\tid : " + String.valueOf(id) + "\n" + "\ttitle : " + title + "\n" + "\tyear : "
                + String.valueOf(year) + "\n" + "\tdirector : " + director + "\n" + "\tbanner_url : "
                + (bannerUrl != null && bannerUrl != "" ? bannerUrl : "unknown") + "\n" + "\ttrailer_url : "
                + (trailerUrl != null && trailerUrl != "" ? trailerUrl : "unknown") + "\n" + "}";
        return str;
    }

    public String prettyPrintString() {
        String str = "" + "\t{\n" + "\t\tid : " + String.valueOf(id) + "\n" + "\t\ttitle : " + title + "\n"
                + "\t\tyear : " + String.valueOf(year) + "\n" + "\t\tdirector : " + director + "\n"
                + "\t\tbanner_url : " + (bannerUrl != null && bannerUrl != "" ? bannerUrl : "unknown") + "\n"
                + "\t\ttrailer_url : " + (trailerUrl != null && trailerUrl != "" ? trailerUrl : "unknown") + "\n"
                + "\t}";
        return str;
    }

    public static Movie buildMovieFromRowResult(Map<String, Object> movieMap) {

        // Non-NULL fields
        Integer id = (Integer) movieMap.get("id");
        String title = (String) movieMap.get("title");
        Integer year = (Integer) movieMap.get("year");
        String director = (String) movieMap.get("director");

        // NULL fields
        String bannerUrl = null;
        String trailerUrl = null;

        Object bannerUrlObj = movieMap.get("banner_url");
        if (bannerUrlObj != null) {
            bannerUrl = (String) bannerUrlObj;
        }

        Object trailerUrlObj = movieMap.get("trailer_url");
        if (trailerUrlObj != null) {
            trailerUrl = (String) trailerUrlObj;
        }

        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setYear(year);
        movie.setDirector(director);
        movie.setBannerUrl(bannerUrl);
        movie.setTrailerUrl(trailerUrl);

        return movie;
    }

    public static Movie getMovieById(int id) {
        String sql = "SELECT * FROM movies WHERE id=" + String.valueOf(id) + " LIMIT 1;";
        try {
            return Movie.buildMovieFromRowResult(QueryProcessor.processReadOp(sql).get(0));
        } catch (Exception e) {
            return null;
        }
    }

    public static Movie buildMovieFromStringParameters(Map<String, String[]> parameterMap) {

        try {

            // Get the parameters
            String[] titleArray = parameterMap.get("movieTitle");
            String[] yearArray = parameterMap.get("movieYear");
            String[] directorArray = parameterMap.get("movieDirector");
            String[] bannerUrlArray = parameterMap.get("movieBannerUrl");
            String[] trailerUrlArray = parameterMap.get("movieTrailerUrl");

            // Parse title, year, director all NOT NULL
            String title = titleArray[0];
            Integer year = Integer.parseInt(yearArray[0]);
            String director = directorArray[0];


            // Parse bannerUrl, can be null.
            String bannerUrl = null;
            try {
                if (!bannerUrlArray[0].isEmpty()) {
                    bannerUrl = bannerUrlArray[0];
                }
            } catch (Exception e) {
                // pass
            }

            // Parse trailerUrl, can be null.
            String trailerUrl = null;
            try {
                if (!trailerUrl.isEmpty()) {
                    trailerUrl = trailerUrlArray[0];
                }
            } catch (Exception e) {
                // pass
            }

            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setYear(year);
            movie.setDirector(director);
            movie.setBannerUrl(bannerUrl);
            movie.setTrailerUrl(trailerUrl);

            return movie;

        } catch (Exception e) {
            return null;
        }
    }
}