public class GenreInMovie {

    // Movie attributes.
    private String title; 
    private int year; 
    private String director;
    private String genre; 
    private String movieIdString;

    // Genre in movie attributes.
    private int genreID; 
    private int movieID; 


    // Constructors.
    public GenreInMovie() {
        this.title = null;
        this.year = -1;
        this.director = null;
        this.genre = null;
        this.genreID = -1;
        this.movieID = -1;
    }
    
    public GenreInMovie(String title, int year, String director, String movieIdString) {
        this();
        this.title = title; 
        this.year = year; 
        this.director = director; 
        this.movieIdString = movieIdString;
    }
    
    public GenreInMovie(String genre) {
        this();
        this.genre = genre;
    }
    
    public GenreInMovie(int genreID, int movieID) {
        this();
        this.genreID = genreID; 
        this.movieID = movieID;
    }

    // Accessors.
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }
    public String getGenre() { return genre; }
    public int getGenreID() { return genreID; }
    public int getMovieID() { return movieID; }
    public String getMovieIdString() { return movieIdString; }


    // Mutators.
    public void setTitle(String title) { this.title = title; }
    public void setYear(int year) { this.year = year; }
    public void setDirector(String director) { this.director = director; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setGenreID(int genreID) { this.genreID = genreID; }
    public void setMovieID(int movieID) { this.movieID = movieID; }
    public void setMovieIdString(String movieIdString) { this.movieIdString = movieIdString; }
    
    @Override 
    public boolean equals(Object thatObj) {
        
        GenreInMovie that = (GenreInMovie) thatObj;

        if (this.title != null && that.getTitle() != null && !this.title.equals(that.getTitle()))
            return false; 
        if (this.year != that.getYear())
            return false; 
        if (this.director != null && that.getDirector() != null && !this.director.equals(that.getDirector()))
            return false; 
        if (this.genre != null && that.getGenre() != null && !this.genre.equals(that.getGenre()))
            return false; 
        if (this.genreID != that.getGenreID())
            return false; 
        if (this.movieID != that.getMovieID())
            return false; 
        if (this.movieIdString != null && that.getMovieIdString() != null && !this.movieIdString.equals(that.getMovieIdString()))
            return false; 
            
        return true; 
        
    }
    
    @Override
    public int hashCode(){
        int hash = 9;
        int factor = 32;
        hash = factor * hash + (this.title == null ? 0 : this.title.hashCode());
        hash = factor * hash + (this.director == null ? 0 : this.director.hashCode());
        hash = factor * hash + (this.genre == null ? 0 : this.genre.hashCode());
        hash = factor * hash + this.year;
        hash = factor * hash + this.genreID;
        hash = factor * hash + this.movieID;
        hash = factor * hash;
        return hash;
    }
}
