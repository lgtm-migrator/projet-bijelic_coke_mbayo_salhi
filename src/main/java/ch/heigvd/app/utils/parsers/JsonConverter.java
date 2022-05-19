package ch.heigvd.app.utils.parsers;

import com.google.gson.Gson;


/**
 * JsonConverter class
 */
public class JsonConverter {
    /**
     * Convert a json string into a java object
<<<<<<< HEAD
=======
     *
>>>>>>> b64fd77 (Update file structure)
     * @param input the json string
     * @return the java object
     * @see <a href="https://github.com/google/gson">gson</a>
     */

    public static SiteConfig convertSite(String input){
        Gson gson = new Gson();
        SiteConfig siteConfig = gson.fromJson(input, SiteConfig.class);

        return siteConfig;
    }

    public static PageConfig convertPage(String input){
        Gson gson = new Gson();

        return gson.fromJson(input, PageConfig.class);
    }

}

