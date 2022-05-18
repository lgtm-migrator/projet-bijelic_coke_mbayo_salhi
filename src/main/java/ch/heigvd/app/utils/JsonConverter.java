package ch.heigvd.app.utils;

import com.google.gson.Gson;

/**
 * JsonConverter class
 */
public class JsonConverter {
    /**
     * Convert a json string into a java object
     * @param input the json string
     * @return the java object
     * @see <a href="https://github.com/google/gson">gson</a>
     */
    public static JavaConfig convertSite(String input){
        Gson gson = new Gson();

        return gson.fromJson(input, JavaConfig.class);
    }

    public static PageConfig convertPage(String input){
        Gson gson = new Gson();

        return gson.fromJson(input, PageConfig.class);
    }
}

