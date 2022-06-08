package ch.heigvd.app;

import ch.heigvd.app.utils.TestDirectoryManager;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class MainTest
{

    // Creates a test directory
    private final Path testPath = Paths.get("monTEST");
    private final Path sitePath = testPath.resolve("siteTEST");
    private final Path buildPath = sitePath.resolve("build");

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    /**
     * Test the "statique Clean /my/site" command
     */
    public void statiqueCleanShouldDeleteDirectory() {
        // Tests if the test directory already exists to avoid overwriting a
        // legit directory
        assertFalse(Files.exists(testPath));
        buildPath.toFile().mkdirs();

        // Creates test files
        try {
            for(int i = 0; i < 10; ++i) {
                File f = buildPath.resolve("test" + i + "txt").toFile();
                f.createNewFile();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        // Delete the file directory using "statique clean PATH" by running
        // from the command line

        Main app = new Main();
        StringWriter sw = new StringWriter();
        CommandLine cmd = new CommandLine(app);
        cmd.setOut(new PrintWriter(sw));

        File path = buildPath.toFile();

        System.out.println("Site path = " + sitePath);

        int exitCode = cmd.execute("clean", sitePath.toString());
        assertEquals(0, exitCode);

        assertFalse("Directory still exists", Files.exists(path.toPath()));

        File pathToTestDir =
                new File(System.getProperty("user" + ".dir") + testPath);

        // Delete the test directory
        try {
            FileUtils.deleteDirectory(testPath.toFile());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    // Test that the init command init a directory with config files
    public void statiqueInitShouldInitADirectory() {
        // run the command
        Main app = new Main();
        StringWriter sw = new StringWriter();
        CommandLine cmd = new CommandLine(app);
        cmd.setOut(new PrintWriter(sw));

        // check that the test directory doesn't exist
        assertFalse("Test directory already exists",
                Files.exists(testPath));

        int exitCode = cmd.execute("init", sitePath.toString());
        assertEquals(0, exitCode);

        // check that config.json exists
        // check that index.md exists

        String confFile = "config.json";
        String indexFile = "index.md";

        assertTrue(Files.exists(sitePath.resolve(confFile)));
        assertTrue(Files.exists(sitePath.resolve(indexFile)));

        try {
            FileUtils.deleteDirectory(testPath.toFile());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    /**
     * Test the "static -V" command
     */
    public void staticVersionShouldExitWithZero() {
        int exitCode = new CommandLine(new Main()).execute("-V");
        assertEquals(0, exitCode);
    }
}
