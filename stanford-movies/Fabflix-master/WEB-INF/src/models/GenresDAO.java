
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.LinkedHashMap;


public class GenresDAO {

    /* Old Version that does not use PreparedStatement */
//    public static Map<Integer, String> getGenresMap() {
//        String sql = "SELECT DISTINCT genres.id, genres.name FROM genres JOIN genres_in_movies ON genres.id = genres_in_movies.genre_id ORDER BY genres.name ASC;";
//        try {
//            Map<Integer, String> genreMap = new LinkedHashMap<Integer, String>();
//            for (Map<String, Object> resultRow : QueryProcessor.processReadOp(sql)) {
//                genreMap.put((Integer) resultRow.get("id"), (String) resultRow.get("name"));
//            }
//            return genreMap;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static Map<Integer, String> getGenresMap() {
        String sqlString = "SELECT DISTINCT genres.id, genres.name FROM genres JOIN genres_in_movies ON genres.id = genres_in_movies.genre_id ORDER BY genres.name ASC;";

        try {
            Connection connection = DBConnectionManager.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);

            Map<Integer, String> genreMap = new LinkedHashMap<Integer, String>();
            for (Map<String, Object> resultRow : QueryProcessor.processReadOp(preparedStatement)) {
                genreMap.put((Integer) resultRow.get("id"), (String) resultRow.get("name"));
            }
            return genreMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
