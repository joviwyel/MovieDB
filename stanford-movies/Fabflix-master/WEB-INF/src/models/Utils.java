
package models;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static class FullName {
        public String firstName;
        public String lastName;
        
        public FullName() {
            firstName = "";
            lastName = "";
        }
        
        public void parse(String fullName) {
            assert(fullName != null);
            String[] fullNameSplit = fullName.split("\\s+");
            for (int i = 0; i < fullNameSplit.length - 1; i++) {
                firstName += fullNameSplit[i] + " ";
            }
            lastName = fullNameSplit[fullNameSplit.length - 1].trim();
            firstName = firstName.trim();
        }
    }

    /**
     * Takes in a query string and returns a map
     * of its parameters. Use request.getParameter(String param)
     * when the desired parameter name is already known.
     */
    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    /**
     * Adds "http://" to start of url if not present.
     */
    public static String normalizeUrl(String url) {
        if (url != null && !url.toLowerCase().startsWith("http://") &&
                !url.toLowerCase().startsWith("https://")) {
            return "http://" + url;
        } else {
            return url;
        }
    }
    
}
