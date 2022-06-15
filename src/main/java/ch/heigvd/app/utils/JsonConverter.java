package ch.heigvd.app.utils;

import ch.heigvd.app.utils.parsers.PageConfig;
import ch.heigvd.app.utils.parsers.SiteConfig;
import com.google.gson.Gson;
import io.javalin.http.SinglePageHandler;

/**
 * JsonConverter class
 */
public class JsonConverter {
    /**
     * Converts a json string into a java object for site config
     * @param input JSON string
     * @return Java object
     * @see <a href="https://github.com/google/gson">gson</a>
     */
    public static SiteConfig convertSite(String input){

        Gson gson = new Gson();

        return gson.fromJson(input, SiteConfig.class);
    }

    /**
     * Converts a json string into a java object for page config
     * @param input JSON string
     * @return Java object
     */
    public static PageConfig convertPage(String input){
        Gson gson = new Gson();

        return gson.fromJson(input, PageConfig.class);
    }

}

