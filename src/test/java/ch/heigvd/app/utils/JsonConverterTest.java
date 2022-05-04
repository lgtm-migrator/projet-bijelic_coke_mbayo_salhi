package ch.heigvd.app.utils;

import ch.heigvd.app.utils.parsers.SiteConfig;
import ch.heigvd.app.utils.parsers.JsonConverter;
import ch.heigvd.app.utils.parsers.PageConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JsonConverterTest class
 */
public class JsonConverterTest {

    @Test
    public void parserShouldParseJsonStringCorrectly() {
        SiteConfig result = new SiteConfig("titre", "fr", "utf-8");
        String input = "{\n" +
                "  \"title\" : \"titre\",\n" +
                "  \"lang\" : \"fr\",\n" +
                "  \"charset\" : \"utf-8\"\n" +
                "}";
        SiteConfig output = JsonConverter.convert(input);

        assertEquals(result.getTitle() , output.getTitle());
        assertEquals(result.getLang(), output.getLang());
        assertEquals(result.getCharset(), output.getCharset());
    }

    @Test
    public void parserShouldParsePageJsonStringCorrectly() {
        PageConfig result = new PageConfig("title", "jdoe", "2022-03-29");
        String input = "{\n" +
                "  \"title\" : \"title\",\n" +
                "  \"author\" : \"jdoe\",\n" +
                "  \"date\" : \"2022-03-29\"\n" +
                "}";
        PageConfig output = JsonConverter.pageConvert(input);

        assertEquals(result.getTitle() , output.getTitle());
        assertEquals(result.getAuthor(), output.getAuthor());
        assertEquals(result.getDate(), output.getDate());
    }

}
