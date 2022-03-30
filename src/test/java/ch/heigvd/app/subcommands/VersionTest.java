package ch.heigvd.app.subcommands;

import ch.heigvd.app.Main;
import org.junit.Test;
import picocli.CommandLine;
import java.io.*;
import static org.junit.Assert.assertEquals;

public class VersionTest {

   @Test
   public void result() {
      int exitCode1 = new CommandLine(new Main()).execute("-V");
      int exitCode2 = new CommandLine(new Main()).execute("--version");

      assertEquals(0, exitCode1);
      assertEquals(0, exitCode2);
   }

   @Test
   public void staticVShouldOutputCorrectMessage() throws IOException {

      String result = "Current version : " + System.getProperty("project" +
              ".version") + "\n";

      try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
         System.setOut(new PrintStream(output));
         new CommandLine(new Main()).execute("-V");
         assertEquals(result, output.toString());
      }
   }

   @Test
   public void staticVersionShouldOutputCorrectMessage() throws IOException {

      String result = "Current version : " + System.getProperty("project" +
              ".version") + "\n";

      try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
         System.setOut(new PrintStream(output));
         new CommandLine(new Main()).execute("--version");
         assertEquals(result, output.toString());
      }
   }

}
