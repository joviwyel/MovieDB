import java.util.Objects;

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

    public NewStar(String name, int birthYear){
        this();
        this.name = name;
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
        if(this.id != null && that.getId() != null && !this.id.equals(that.getId()))
            return false;
        if(this.birthYear != -1 && that.getBirthYear() != -1 && this.birthYear != that.getBirthYear())
            return false;

        if(this.movieId != null && that.getMovieId() != null && !this.movieId.equals(that.getMovieId()))
            return false;

        if(this.starId != null && that.getStarId() != null && !this.starId.equals(that.getStarId()))
            return false;
        return true;


    }

    @Override
    public int hashCode(){
        int hash = 9;
        int factor = 32;
        hash = factor * hash + (this.name == null ? 0 : this.name.hashCode());
        hash = factor * hash + (this.id == null ? 0 : this.id.hashCode());
        hash = factor * hash + (this.birthYear == -1 ? 0 : this.birthYear);
        hash = factor * hash + (this.fid == null ? 0 : this.fid.hashCode());
        hash = factor * hash + (this.starId == null ? 0 : Integer.parseInt(this.starId.substring(2)));
        hash = factor * hash + (this.movieId == null ? 0 : Integer.parseInt(this.movieId.substring(2)));
        hash = factor * hash;
        return hash;
    }
}
