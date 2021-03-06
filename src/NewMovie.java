import java.util.Objects;

public class NewMovie {

    // Movie attributes
    private String title;
    private int year;
    private String director;
    private String genre;
    private String id;
    private String fid;


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
        this.id = null;
        this.fid = null;
    }

    public NewMovie(String id, String title, int year, String director){
        this();
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
    }
    public NewMovie(String fid, String movieId){
        this();
        this.fid = fid;
        this.movieId = movieId;
    }

    public NewMovie(String title, int year, String director){
        this();
        this.title = title;
        this.year = year;
        this.director = director;
    }
    public NewMovie(String movieId, int genreId){
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
    public String getId(){return this.id;}
    public String getFid(){return this.fid;}

    public void setTitle(String title) { this.title = title; }
    public void setYear(int year) { this.year = year; }
    public void setDirector(String director) { this.director = director; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setGenreID(int genreId) { this.genreId = genreId; }
    public void setMovieID(String movieId) { this.movieId = movieId; }
    public void setId(String id){ this.id = id;}
    public void setFid(String fid){ this.fid = fid;}

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", director='" + director + '\'' +
                ", genre='" + genre + '\'' +
                ", genreId='" + genreId + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object thatObj) {

        NewMovie that = (NewMovie) thatObj;
        if (this.director != null && that.getDirector() != null && !this.director.equals(that.getDirector()))
            return false;
        if (this.year != that.getYear())
            return false;
        if (this.title != null && that.getTitle() != null && !this.title.equals(that.getTitle()))
            return false;
        if (this.genre != null && that.getGenre() != null && !this.genre.equals(that.getGenre()))
            return false;
        if (this.id != null && that.getId() != null && !this.id.equals(that.getId()))
            return false;
        if (this.genreId != -1 && that.getGenreId() != -1 && this.genreId != that.getGenreId())
            return false;
        if (this.movieId != null && that.getMovieId() != null && !this.movieId.equals(that.getMovieId()))
            return false;

        return true;

    }


    @Override
    public int hashCode(){
        return Objects.hash(getTitle(), getDirector(), getYear());
    }


}
