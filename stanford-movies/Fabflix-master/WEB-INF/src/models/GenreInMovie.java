
package models;

public class GenreInMovie {
    private int genreId;
    private int movieId;

    public GenreInMovie() {

    }

    public int getGenreId() {
        return genreId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}