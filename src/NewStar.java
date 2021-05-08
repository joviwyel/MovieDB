public class NewStar {

    private String name;
    private String id;
    private int birthYear;

    private String starId;
    private String movieId;

    public NewStar(){
        this.name = null;
        this.id = null;
        this.birthYear = -1;
        this.starId = null;
        this.movieId = null;
    }

    public NewStar(String id, String name, int birthYear){
        this();
        this.name = name;
        this.id = id;
        this.birthYear = birthYear;
    }

    public NewStar(String starId, String movieId){
        this();
        this.starId = starId;
        this.movieId = movieId;
    }

    public String getMovieId(){return this.movieId;}
    public String getStarId(){return this.starId;}
    public String getName(){return this.name;}
    public String getId(){return this.id;}
    public int getBirthYear(){return this.birthYear;}

    public void setMovieId(String movieId){this.movieId = movieId;}
    public void setName(String name){this.name = name;}
    public void setId(String id){this.id = id;}
    public void setStarId(String starId){this.starId = starId;}
    public void setBirthYear(int birthYear){this.birthYear = birthYear;}


    @Override
    public String toString() {
        return "NewStar{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", birthYear=" + birthYear +
                ", starId='" + starId + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }
}
