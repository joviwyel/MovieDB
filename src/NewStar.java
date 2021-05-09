public class NewStar {

    private String name;
    private String id;
    private int birthYear;
    private String fid;

    private String starId;
    private String movieId;

    public NewStar(){
        this.name = null;
        this.id = null;
        this.birthYear = -1;
        this.starId = null;
        this.movieId = null;
        this.fid = null;
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
    public String getFid(){ return this.fid;}

    public void setMovieId(String movieId){this.movieId = movieId;}
    public void setName(String name){this.name = name;}
    public void setId(String id){this.id = id;}
    public void setStarId(String starId){this.starId = starId;}
    public void setBirthYear(int birthYear){this.birthYear = birthYear;}
    public void setFid(String fid){this.fid = fid;}


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

    @Override
    public boolean equals(Object object) {
        NewStar that = (NewStar) object;
        if (this.name != null && that.getName() != null && !this.name.equals(that.getName()))
            return false;

        if(this.fid != null && that.getFid() != null && !this.fid.equals(that.getFid()))
            return false;
        return true;

    }
}
