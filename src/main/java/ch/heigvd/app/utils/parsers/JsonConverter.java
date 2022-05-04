package ch.heigvd.app.utils.parsers;

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
    public static SiteConfig convert(String input){
        Gson gson = new Gson();
        SiteConfig javaConf = gson.fromJson(input, SiteConfig.class);

        return javaConf;
    }
}
