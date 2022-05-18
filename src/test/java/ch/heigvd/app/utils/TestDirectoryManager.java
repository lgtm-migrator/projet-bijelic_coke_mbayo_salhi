package ch.heigvd.app.utils;

import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Create and delete test directory
 */
public class TestDirectoryManager {

    /**
     * Create a basic test directory
     * @param dirName Test directory name
     * @param websiteName Test website name
     * @throws IOException Error during creation or deletion of files
     */
    public static void createBasicTestDirectory(Path dirName, String websiteName) throws IOException {
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

        String configJson = "{\"title\": \"Mon site internet\"}";

        //Files.createDirectories(dirName);
        System.out.println("Directory " + dirName + " is created!");

        Path websitePath = Paths.get(dirName + "/" + websiteName);

        Path dossierPath = Paths.get(websitePath + "/dossier/");
        Files.createDirectories(dossierPath);
        System.out.println("Directory " + dossierPath + " is created!");

        createFileWithContent(Paths.get(websitePath + "/dossier/page.md"), pageMdContent);

        Path imagePath = Paths.get(dossierPath + "/image.png");
        Files.createFile(imagePath);

        createFileWithContent(Paths.get(websitePath + "/index.md"), indexMdContent);

        createFileWithContent(Paths.get(websitePath + "/config.json"), configJson);
    }

    /**
     * Create a basic test directory with a template folder
     * @param dirName Test directory name
     * @param websiteName Test website name
     * @throws IOException Error during creation or deletion of files
     */
    public static void createTemplateTestDirectory(Path dirName, String websiteName) throws IOException {
        createBasicTestDirectory(dirName, websiteName);

        Path templatePath = Paths.get(dirName + "/" + websiteName + "/template/");
        Files.createDirectory(templatePath);
        System.out.println("Directory " + templatePath + " is created!");

        String pageMdContent = "titre: Mon premier article\n" +
                "auteur: Bertil Chapuis\n" +
                "date: 2021-03-10\n" +
                "---\n" +
                "# Mon titre\n" +
                "## Mon sous-titre\n" +
                "Le contenu de mon article.\n" +
                "![Une image](./image.png)";

        String layoutHtmlContent = "<html lang=\"en\">\n" +
                                    "<head>\n" +
                                        "<meta charset=\"utf-8\">\n" +
                                        "<title>{{ site.titre }} | {{ page.titre }}</title>\n" +
                                    "</head>\n" +
                                    "<body>\n" +
                                        "{{> menu }}\n" +
                                        "{{ content }}\n" +
                                    "</body>\n" +
                                    "</html>\n";

        String menuHtmlContent = "<ul>\n" +
                                    "<li><a href=\"/index.html\">home</a></li>\n" +
                                    "<li><a href=\"/content/page.html\">page</a></li>\n" +
                                 "</ul>";

        // Rewrite page.md file as it is not the same as the basic website one
        createFileWithContent(Paths.get(dirName + "/" + websiteName + "/dossier/page.md"), pageMdContent);

        createFileWithContent(Paths.get(templatePath + "/layout.html"), layoutHtmlContent);

        createFileWithContent(Paths.get(templatePath + "/menu.html"), menuHtmlContent);
    }


    /**
     * Delete test directory
     * @param dirName Name of the test directory
     * @throws IOException Error during test directory deletion
     */
    public static void deleteTestDirectory(Path dirName) throws IOException {
        System.out.println("Delete test directory if exists");
        FileUtils.deleteDirectory(dirName.toFile());
    }

    /**
     * Create a file in specified path with content
     * @param path Path where file should be created
     * @param content Content added into the file
     * @throws IOException Input or Output error during creating of the file
     */
    private static void createFileWithContent(Path path, String content) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toString()), StandardCharsets.UTF_8);
        writer.write(content);
        writer.flush();
        writer.close();
        System.out.println("File " + path + " is created and its content added!");
    }
}
