
package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Star {
    
    public int id;
    public String firstName;
    public String lastName;
    public Date dob;
    public String photoUrl;
    public List<Movie> movies;

    public Star() {
        this.id = -1; // Default value.
        this.movies = new ArrayList<Movie>();
    }

    public Star(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Star(Integer id, String firstName, String lastName, Date dob, String photoUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.photoUrl = photoUrl;
        this.movies = new ArrayList<Movie>();
    }

    public Star(String firstName, String lastName, Date dob, String photoUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public List<Movie> getMovies() {
        return movies;
    }
    
    public String getFullName() {
        if (firstName.isEmpty()) {
            return lastName;
        } else {
            return firstName + " " + lastName;
        }
    }
    
    public String getFullNameAndId() {
        return getFullName() + ", id = " + id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = Utils.normalizeUrl(photoUrl);
    }

    public void setMovies(List<Movie> movies) { this.movies = movies; }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }
    
    @Override 
    public String toString() {
        String str = "Star(";
        str += "id=" + String.valueOf(id) + 
               ", first_name=" + firstName + 
               ", last_name=" + lastName + 
               ", dob=" + (dob != null ? dob.toString() : "null") + 
               ", photo_url=" + (photoUrl != null ? photoUrl.toString() : "null") +
               ")";
        return str;
    }

    public static Star buildStarFromRowResult(Map<String, Object> starMap) {
        
        Integer starId = (Integer) starMap.get("id");
        String firstName = (String) starMap.get("first_name");
        String lastName = (String) starMap.get("last_name");

        Date dob = null;
        try {
            dob = (Date) starMap.get("dob");
        } catch (Exception e) {
            // pass
        }

        String photoUrl = null;
        try {
            photoUrl = (String) starMap.get("photo_url");
        } catch (Exception e) {
            // pass
        }
        
        Star star = new Star();
        star.setId(starId);
        star.setFirstName(firstName);
        star.setLastName(lastName);
        star.setDob(dob);
        star.setPhotoUrl(photoUrl);
        
        return star;
        
    }

    public static Star buildStarFromStringParameters(Map<String, String[]> parameterMap) {

        try {

            // Get the parameters
            String[] starFullNameArray = parameterMap.get("starFullName");
            String[] starDobArray = parameterMap.get("starDob");
            String[] photoUrlArray = parameterMap.get("starPhotoUrl");

            // Parse full name
            Utils.FullName starFullName = new Utils.FullName();
            starFullName.parse(starFullNameArray[0]);

            // Parse dob, can be null.
            Date starDob = null;
            try {
                starDob = Date.valueOf(starDobArray[0]);
            } catch (Exception e) {
                //pass
            }

            /*
            //TODO remove me!
            Date starDob = (starDobArray != null &&
                            starDobArray.length != 0 &&
                            !starDobArray[0].equals("") ? Date.valueOf(starDobArray[0]) : null);
            */

            // Parse photo url, can be null.
            String photoUrl = null;
            try {
                if (!photoUrlArray[0].isEmpty()) {
                    photoUrl = photoUrlArray[0];
                }
            } catch (Exception e) {
                // pass
            }

            /*
            String photoUrl = (photoUrlArray != null && photoUrlArray.length != 0 && ? photoUrlArray[0] : null);
            //TODO Delete ME!
            */

            return new Star(starFullName.firstName, starFullName.lastName, starDob, photoUrl);

        } catch (Exception e) {
            return null;
        }
    }
}
