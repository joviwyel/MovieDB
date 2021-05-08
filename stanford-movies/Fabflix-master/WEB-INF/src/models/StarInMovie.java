
package models;

public class StarInMovie {
    public int starId;
    public int movieId;

    public StarInMovie() {

    }

    public int getStarId() {
        return starId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}