package ch.heigvd.app.utils;

import ch.heigvd.app.utils.parsers.SiteConfig;
import ch.heigvd.app.utils.parsers.JsonConverter;
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

}
