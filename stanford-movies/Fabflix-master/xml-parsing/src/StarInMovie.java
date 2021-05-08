public class StarInMovie {


    private String firstName;
    private String actorLastName; 
    private String movieTitle; 
    private String filmID; 
    private int starID; 
    private int movieID; 
    
    public StarInMovie() { 
        this.firstName = null;
        this.actorLastName = null;
        this.movieTitle = null;
        this.filmID = null;
        starID = -1; 
        movieID = -1;
    }
    
    public StarInMovie(String firstName, String actorLastName, String movieTitle){
        this();
        this.firstName = firstName;
        this.actorLastName = actorLastName; 
        this.movieTitle = movieTitle;
    }
    
    public StarInMovie(String movieTitle, String filmID, int useless){
        this();
        this.movieTitle = movieTitle; 
        this.filmID = filmID;
    }
    
    public StarInMovie(String firstName, String actorLastName){
        this();
        this.firstName = firstName;
        this.actorLastName = actorLastName;
    }
    
    public StarInMovie(int starID, int movieID) {
        this();
        this.starID = starID; 
        this.movieID = movieID;
    }
    
    public String getFirstName() { return firstName; }
    public String getActorLastName() { return actorLastName; }
    public String getMovieTitle() { return movieTitle; }
    public String getFilmID() { return filmID; }
    public int getStarID() { return starID; }
    public int getMovieID() { return movieID; }
    
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setActorLastName(String actorLastName) { this.actorLastName = actorLastName; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public void setFilmID(String filmID) { this.filmID = filmID; }
    public void setStarID(int starID) { this.starID = starID; }
    public void setMovieID(int movieID) { this.movieID = movieID; }
    
    @Override 
    public boolean equals(Object object) {
        
        StarInMovie that = (StarInMovie) object;
        
        if (this.firstName != null && that.getFirstName() != null && !this.firstName.equals(that.getFirstName()))
            return false; 
        if (this.actorLastName != null && that.getActorLastName() != null && !this.actorLastName.equals(that.getActorLastName()))
            return false; 
        if (this.movieTitle != null && that.getMovieTitle() != null && !this.movieTitle.equals(that.getMovieTitle()))
            return false; 
        if (this.starID != that.getStarID())
            return false; 
        if (this.movieID != that.getMovieID())
            return false; 
        if (this.filmID != null && that.getFilmID() != null && !this.filmID.equals(that.getFilmID()))
            return false; 
            
        return true; 
        
    }
    
    @Override
    public int hashCode() {

        int hash = 9;
        int factor = 32;
        hash = factor * hash + (this.firstName == null ? 0 : this.firstName.hashCode());
        hash = factor * hash + (this.actorLastName == null ? 0 : this.actorLastName.hashCode());
        hash = factor * hash + (this.movieTitle == null ? 0 : this.movieTitle.hashCode());
        hash = factor * hash + (this.filmID == null ? 0 : this.filmID.hashCode());
        hash = factor * hash + this.starID;
        hash = factor * hash + this.movieID;
        hash = factor * hash;
        return hash;
    }

}
