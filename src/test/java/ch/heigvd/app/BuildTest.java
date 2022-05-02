package ch.heigvd.app;

import ch.heigvd.app.utils.TestDirectoryManager;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class BuildTest {
    private final String dirName = "monTEST/";
    private final String websiteName = dirName + "siteTEST/";

    @Before
    public void initTestDirectory(){
        try {
            TestDirectoryManager.createBasicFolderForTesting(dirName, websiteName);
        } catch (IOException e) {
            System.err.println("Error while creating test directory " + e.getMessage());
        }
    }

    @Test
    public void statiqueBuildShouldCopyRightFilesAndConvertMarkdownToHTML() throws IOException {
        String pageHTMLContent = "<h1>Ma première page</h1>\n";

        String indexHTMLContent = "<h1>Mon premier article</h1>\n" +
                "<h2>Mon sous-titre</h2>\n" +
                "<p>Le contenu de mon article.</p>\n" +
                "<p><img src=\"./image.png\" alt=\"Une image\" /></p>\n";

        Main app = new Main();
        StringWriter sw = new StringWriter();
        CommandLine cmd = new CommandLine(app);
        cmd.setOut(new PrintWriter(sw));

        String testPath = websiteName;

        int exitCode = cmd.execute("build", testPath);
        assertEquals(0, exitCode);

        Path buildPath = Paths.get(testPath + "/build");
        assertTrue("Build folder could not be created", Files.exists(buildPath));

        Path dossierPath = Paths.get(buildPath + "/dossier");
        assertTrue("Dossier folder could not be created", Files.exists(dossierPath));

        Path imagePath = Paths.get(dossierPath + "/image.png");
        assertTrue("Image file could not be copied", Files.exists(imagePath));

        Path pagePath = Paths.get(dossierPath + "/page.html");
        assertTrue("Page.html file could not be created", Files.exists(pagePath));

        Path configPath = Paths.get(buildPath + "/config.yaml");
        assertFalse("Config file should not be copied", Files.exists(configPath));

        Path indexPath = Paths.get(buildPath + "/index.html");
        assertTrue("Index.html file could not be created", Files.exists(indexPath));

        assertEquals("Page.html content is not as expected!", pageHTMLContent,
                FileUtils.readFileToString(pagePath.toFile(), StandardCharsets.UTF_8)
        );

        assertEquals("Index.html content is not as expected!", indexHTMLContent,
                FileUtils.readFileToString(indexPath.toFile(), StandardCharsets.UTF_8)
        );
    }

    @After
    public void deleteTestDirectory(){
        try {
            TestDirectoryManager.deleteTestDirectory(dirName);
        } catch (IOException e) {
            System.err.println("Error while deleting test directory " + e.getMessage());
        }
    }
}



