public class JumpSession {

    private String title;
    private String year;
    private String director;
    private String star;
    private String genre;
    private String letter;
    private String sortby1;
    private String order1;
    private String sortby2;
    private String order2;

    public JumpSession(String title, String director, String year, String star, String genre,
                        String letter, String sortby1, String order1, String sortby2, String order2) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.star = star;
        this.genre = genre;
        this.letter = letter;
        this.sortby1 = sortby1;
        this.order1 = order1;
        this.sortby2 = sortby2;
        this.order2 = order2;
    }

    public JumpSession(){
        this.title = null;
        this.director = null;
        this.year = null;
        this.star = null;
        this.genre = null;
        this.letter = null;
        this.sortby1 = null;
        this.order1 = null;
        this.sortby2 = null;
        this.order2 = null;
    }

    public void setTitle(String a){
        this.title = a;
    }
    public void setYear(String a){
        this.year = a;
    }
    public void setDirector(String a){
        this.director = a;
    }
    public void setStar(String a){
        this.star = a;
    }
    public void setGenre(String a){
        this.genre = a;
    }
    public void setLetter(String a){
        this.letter = a;
    }
    public void setSortby1(String a){
        this.sortby1 = a;
    }
    public void setOrder1(String a){
        this.order1 = a;
    }
    public void setSortby2(String a){
        this.sortby2 = a;
    }
    public void setOrder2(String a){
        this.order2 = a;
    }
    public String getTitle(){
        return this.title;
    }
    public String getYear(){
        return this.year;
    }
    public String getDirector(){
        return this.director;
    }
    public String getStar(){
        return this.star;
    }
    public String getGenre(){
        return this.genre;
    }
    public String getLetter(){
        return this.letter;
    }
    public String getSortby1(){
        return this.sortby1;
    }
    public String getOrder1(){
        return this.order1;
    }
    public String getSortby2(){
        return this.sortby2;
    }
    public String getOrder2(){
        return this.order2;
    }

    @Override
    public String toString() {
        return "JumpSession{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", director='" + director + '\'' +
                ", star='" + star + '\'' +
                ", genre='" + genre + '\'' +
                ", letter='" + letter + '\'' +
                ", sortby1='" + sortby1 + '\'' +
                ", order1='" + order1 + '\'' +
                ", sortby2='" + sortby2 + '\'' +
                ", order2='" + order2 + '\'' +
                '}';
    }
}
