package ch.heigvd.app;

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
    public void createBasicFolderForTesting(){
        deleteTestDirectory();

        String indexMdContent = "titre: Mon premier article\n" +
                "auteur: Bertil Chapuis\n" +
                "date: 2021-03-10\n" +
                "---\n" +
                "# Mon premier article\n" +
                "## Mon sous-titre\n" +
                "Le contenu de mon article.\n"+
                "![Une image](./image.png)";

        String pageMdContent = "titre: Ma premiere page\n" +
                "auteur: Bertil Chapuis\n" +
                "date: 2021-03-10\n" +
                "---\n" +
                "# Ma première page\n";

        String configYaml = "title: Mon site internet";

        try{
            Path dossierPath = Paths.get(websiteName + "dossier/");
            Files.createDirectories(dossierPath);
            System.out.println("Directory " + dossierPath + " is created!");

            OutputStreamWriter pageWriter = new OutputStreamWriter(new FileOutputStream(dossierPath + "/page.md"), StandardCharsets.UTF_8);
            pageWriter.write(pageMdContent);
            pageWriter.flush();
            pageWriter.close();
            System.out.println("File dossier/page.md is created and its content added!");

            Path imagePath = Paths.get(dossierPath + "/image.png");
            Files.createFile(imagePath);

            OutputStreamWriter indexWriter = new OutputStreamWriter(new FileOutputStream(websiteName + "/index.md"), StandardCharsets.UTF_8);
            indexWriter.write(indexMdContent);
            indexWriter.flush();
            indexWriter.close();
            System.out.println("File index.md is created and its content added!");

            OutputStreamWriter configWriter = new OutputStreamWriter(new FileOutputStream(websiteName + "/config.yaml"), StandardCharsets.UTF_8);
            configWriter.write(configYaml);
            configWriter.flush();
            configWriter.close();
            System.out.println("File config.yaml is created and its content added!");

        } catch (IOException e) {
            System.err.println("Failed to create directory and files" + e.getMessage());
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
    public void deleteTestDirectory() {
        System.out.println("Delete test directory if exists");
        try{
            FileUtils.deleteDirectory(Paths.get(dirName).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



