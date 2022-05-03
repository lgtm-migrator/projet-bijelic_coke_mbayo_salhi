package ch.heigvd.app.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JsonConverterTest class
 */
public class JsonConverterTest {

    @Test
    public void parserShouldParseJsonStringCorrectly() {
        JavaConfig result = new JavaConfig("titre", "fr", "utf-8");
        String input = "{\n" +
                "  \"title\" : \"titre\",\n" +
                "  \"lang\" : \"fr\",\n" +
                "  \"charset\" : \"utf-8\"\n" +
                "}";
        JavaConfig output = JsonConverter.convert(input);

        assertEquals(result.title , output.title);
        assertEquals(result.lang, output.lang);
        assertEquals(result.charset, output.charset);
    }

    @Test
    public void parserShouldParsePageJsonStringCorrectly() {
        PageConfig result = new PageConfig("titre", "jdoe", "2022-03-29");
        String input = "{\n" +
                "  \"titre\" : \"titre\",\n" +
                "  \"auteur\" : \"jdoe\",\n" +
                "  \"date\" : \"2022-03-29\"\n" +
                "}";
        PageConfig output = JsonConverter.pageConvert(input);

        assertEquals(result.titre , output.titre);
        assertEquals(result.auteur, output.auteur);
        assertEquals(result.date, output.date);
    }

}
