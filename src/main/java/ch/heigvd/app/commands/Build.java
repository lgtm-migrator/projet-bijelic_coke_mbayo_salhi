package ch.heigvd.app.commands;

import ch.heigvd.app.utils.JsonConverter;
import ch.heigvd.app.utils.parsers.MarkdownConverter;
import ch.heigvd.app.utils.parsers.PageConfig;
import ch.heigvd.app.utils.parsers.SiteConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Represent the layout that is applied to all HTML pages
 */
class Layout{
    HashMap<String, String> siteMetaData;
    String layoutHtmlContent;

    /**
     * Layout constructor
     * @param siteMetaData Map of the site metadata
     * @param layoutHtmlContent HTML layout content of the website
     */
    Layout(HashMap<String, String> siteMetaData, String layoutHtmlContent){
        if(siteMetaData.isEmpty())
            throw new IllegalArgumentException("Site metadatas is empty!");
        if(layoutHtmlContent.isEmpty())
            throw new IllegalArgumentException("No layout given!");
        this.siteMetaData = new HashMap<>(copyMap(siteMetaData));
    }

    /**
     * Get content of the layout
     * @return Content of the layout
     */
    protected String getLayoutHtmlContent(){
        return layoutHtmlContent;
    }

    /**
     * Get the site metdata map
     * @return Site metadata map
     */
    protected Map<String, String> getSiteMetaData(){
        return copyMap(this.siteMetaData);
    }

    /**
     * Copy map into a new one
     * @param originalMap Original map to copy from
     * @return Map where datas are copied in
     */
    private Map<String, String> copyMap(Map<String, String> originalMap){
        Map<String, String> copy = new HashMap<>();
        for(String key : originalMap.keySet()){
            copy.put(key, originalMap.get(key));
        }
        return copy;
    }
}

@Command(name = "build")
public class Build implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to build " + "directory")

    private Path sourcePath;

    private Layout layout = null;


    final private String BUILD_DIRECTORY_NAME = "build";
    final private String MARKDOWN_FILE_TYPE = "md";
    final private Set<String> DIRECTORIES_TO_EXCLUDE = Set.of("build", "template");
    final private Set<String> FILES_TO_EXCLUDE = Set.of("config.json");

    @Override
    public Integer call() throws Exception {
        System.out.println("Building in : " + sourcePath);


        Path buildPath = sourcePath.resolve(BUILD_DIRECTORY_NAME);

        System.out.println("buildPath = " + buildPath);

        copyFiles(sourcePath, buildPath);
        // Get values from config file and create Layout
        try {
            HashMap<String, String> map = new HashMap<>();

            Path configPath = sourcePath.resolve("config.json");
            String configContent = Files.readString(configPath);
            ch.heigvd.app.utils.parsers.SiteConfig config = JsonConverter.convertSite(configContent);

            map.put("title", config.getTitle());
            map.put("lang", config.getLang());
            map.put("charset", config.getCharset());

            Path layoutPath = sourcePath.resolve("template").resolve("layout.html");
            String layoutContent = Files.readString(layoutPath);

            layout = new Layout(map, layoutContent);

            copyFiles(sourcePath, sourcePath.resolve(BUILD_DIRECTORY_NAME));
        } catch (Exception e) {
            System.err.println("An error was encounter during the creation of the template: " + e.getMessage());
        }


        return 0;
    }

    /**
     * Recursive directory copiing with parsing for certain files
     * @param source Directory where files are located
     * @param destination Directory where files are being copied to
     */
    private void copyFiles(Path source, Path destination, CopyOption... options) throws IOException {
        if(Files.exists(destination)) {
            System.out.println("Directory build already exists. It will be deleted");
            FileUtils.deleteDirectory(destination.toFile());
            System.out.println("Directory build successfully deleted");
        }

        // Go through all directory and copy files and folders in build folder
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            /**
             * Visit directory and copy it in /build/
             * @param dir Path of the directory
             * @param attrs Attributes of directory
             * @return Status of the directory visit
             * @throws IOException Throw exception if creation fails
             */
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (!dir.startsWith(sourcePath.resolve("build")) && !dir.startsWith(sourcePath.resolve("template"))) {
                    if (dir.startsWith(sourcePath.resolve("template"))) {
                        HashMap<String, String> map = new HashMap<>();

                        // Get values from config file and create Layout
                        try {
                            Path configPath = sourcePath.resolve("config.json");
                            SiteConfig config = JsonConverter.convertSite(configPath.toString());

                            map.put("title", config.getTitle());
                            map.put("lang", config.getLang());
                            map.put("charset", config.getCharset());

                            Path layoutPath = sourcePath.resolve("template").resolve("layout.html");
                            String layoutContent = Files.readString(layoutPath);

                            layout = new Layout(map, layoutContent);
                        } catch (Exception e) {
                            System.err.println("An error was encounter during the creation of the template: " + e.getMessage());
                            return FileVisitResult.TERMINATE;
                        }
                    } else if (!dir.startsWith(sourcePath.resolve("build"))) {
                        if (!dir.startsWith(sourcePath.resolve("build")) || !dir.startsWith(sourcePath.resolve("template"))) {
                            try {
                                Path destinationPath = destination.resolve(source.relativize(dir));
                                Files.createDirectory(destinationPath);
                                System.out.println("Directory " + destinationPath + " successfully created");
                            } catch (IOException e) {
                                System.err.println("An error was encounter during the creation of a directory: " + e.getMessage());
                                return FileVisitResult.TERMINATE;
                            }
                        }
                    }
                }

                return FileVisitResult.CONTINUE;
            }

            /**
             * Visit files and copy or convert markdown to html
             * @param file Path of the file
             * @param attrs Attributes the file
             * @return Status of the file visit
             * @throws IOException Throw exception if creation fails
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // Check if visited file extension is not in excluded list
                if(!FILES_TO_EXCLUDE.contains(file.getFileName().toString())){
                    // Check if file extension is markdown to transform in HTML file
                    if(FilenameUtils.getExtension(file.toString()).equals(MARKDOWN_FILE_TYPE)){
                        StringBuilder htmlContent = new StringBuilder();
                        StringBuilder pageConfigContent = new StringBuilder();
                        PageConfig pageConfig = null;

                        try (FileInputStream fis = new FileInputStream(file.toString());
                             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                             BufferedReader reader = new BufferedReader(isr)
                        ) {
                            String str;
                            boolean startToCopy = false;
                            while ((str = reader.readLine()) != null) {
                                if(startToCopy){
                                    htmlContent.append(MarkdownConverter.convert(str));
                                }
                                else if(str.equals("---")){
                                    // Copy markdown file header to a PageConfig and start copying markdown from specific line
                                    pageConfig = JsonConverter.convertPage(pageConfigContent.toString());
                                    startToCopy = true;
                                }
                                else{
                                    pageConfigContent.append(str);
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("Error while reading markdown file");
                        }

                        Path htmlFile = Paths.get(
                                FilenameUtils.removeExtension(
                                        destination.resolve(source.relativize(file)).toString()) + ".html"
                        );
                        Files.createFile(htmlFile);

                        // Write HTML content in destination file
                        OutputStreamWriter htmlWriter = new OutputStreamWriter(new FileOutputStream(htmlFile.toString()), StandardCharsets.UTF_8);
                        htmlWriter.write(htmlContent.toString());
                        htmlWriter.flush();
                        htmlWriter.close();
                        System.out.println("File " + htmlFile + " successfully created");
                    }
                    // If not markdown, the file will be copied
                    else {
                        // Bug fix Linux
                        if(!file.startsWith(destination)) {
                            Files.copy(file, destination.resolve(source.relativize(file)), options);
                            System.out.println("File " + file + " successfully copied");
                        }
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
