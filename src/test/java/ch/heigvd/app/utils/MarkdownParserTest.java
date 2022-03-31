package ch.heigvd.app.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarkdownParserTest {

   @Test
   public void parserShouldParseImagesCorrectly() {
      String result = "<img src=\"./image.png\" alt=\"Une image\"/>";
      String input = "![Une image](./image.png)";
      String output = MarkdownParser.parse(input);

      assertEquals(result,output);
   }

   @Test
   public void parserShouldParseLinksCorrectly() {
      String result = "<a href=\"www.google.com\">Lien vers google</a>";
      String input = "[Lien vers google](www.google.com)";
      String output = MarkdownParser.parse(input);

      assertEquals(result,output);
   }

   @Test
   public void parserShouldParseHeadingsCorrectly() {
      String result1 = "<h1>Mon premier article</h1>";
      String input1 = "# Mon premier article";
      String output1 = MarkdownParser.parse(input1);

      assertEquals(result1,output1);

      String result2 = "<h2>Mon sous-titre</h2>";
      String input2 = "## Mon sous-titre";
      String output2 = MarkdownParser.parse(input2);

      assertEquals(result2,output2);
   }

   @Test
   public void parserShouldParseParagraphsCorrectly() {
      String result = "<p>Le contenu de mon article.</p>";
      String input = "Le contenu de mon article.";
      String output = MarkdownParser.parse(input);

      assertEquals(result,output);
   }


   @Test
   public void parserShouldParseWholePageCorrectly() {

      String result = "<h1>Mon premier article</h1>\n" +
              "<h2>Mon sous-titre</h2>\n" +
              "<p>Le contenu de mon article.</p>\n" +
              "<img src=\"./image.png\" alt=\"Une image\"/>";

      String input = "# Mon premier article\n" +
              "## Mon sous-titre\n" +
              "Le contenu de mon article.\n" +
              "![Une image](./image.png)";


      String output = MarkdownParser.parse(input);

      assertEquals(result, output);
   }




}
