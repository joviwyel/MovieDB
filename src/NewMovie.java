public class NewMovie {

    // Movie attributes
    private String title;
    private int year;
    private String director;
    private String genre;
    private String id;

    // Genre information
    private int genreId;
    private String movieId;

    public NewMovie(){
        this.title = null;
        this.year = 0;
        this.director = null;
        this.genre = null;
        this.genreId = -1;
        this.movieId = null;
    }

    public NewMovie(String title, int year, String director, String movieId){
        this();
        this.title = title;
        this.year = year;
        this.director = director;
        this.movieId = movieId;
    }

    public NewMovie(int genreId, String movieId){
        this();
        this.genreId = genreId;
        this.movieId = movieId;
    }

    public NewMovie(String genre){
        this();
        this.genre = genre;
    }

    public String getTitle(){return this.title;}
    public int getYear(){return this.year;}
    public String getDirector(){return this.director;}
    public String getGenre(){return this.genre;}
    public int getGenreId(){return this.genreId;}
    public String getMovieId(){return this.movieId;}

    public void setTitle(String title) { this.title = title; }
    public void setYear(int year) { this.year = year; }
    public void setDirector(String director) { this.director = director; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setGenreID(int genreId) { this.genreId = genreId; }
    public void setMovieID(String movieId) { this.movieId = movieId; }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", director='" + director + '\'' +
                ", genre='" + genre + '\'' +
                ", genreId='" + genreId + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }


}
