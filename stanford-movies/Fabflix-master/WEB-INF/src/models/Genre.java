
package models;

import java.util.Map;

public class Genre {

    public Integer id;
    public String name;
    
    public Genre() { }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(String name) {
        this.name = name;
    }
    
    public Integer getId() {
        return id; 
    }
    
    public String getName() {
        return name;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public static Genre buildGenreFromStringParameters(Map<String, String[]> parametersMap) {

        try {
            String[] nameArray = parametersMap.get("movieGenre");
            if (!nameArray[0].isEmpty()) {
                return new Genre(nameArray[0]);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
