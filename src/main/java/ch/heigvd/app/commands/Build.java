package ch.heigvd.app.commands;

import ch.heigvd.app.utils.JsonConverter;
import ch.heigvd.app.utils.parsers.MarkdownConverter;
import ch.heigvd.app.utils.parsers.PageConfig;
import ch.heigvd.app.utils.parsers.SiteConfig;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import utils.watchDir.WatchDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Represent the layout that is applied to all HTML pages
 */
class Layout{
    private final HashMap<String, String> siteMetaData;
    private String layoutHtmlContent;

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

        this.layoutHtmlContent = layoutHtmlContent;
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

    final private String CONFIG_FILENAME = "config.json";
  
    @CommandLine.Option(names = {"-w", "--watch"}, description = "Allows to regenerate site when modification are made")
    private boolean watchDir;

    final private String BUILD_DIRECTORY_NAME = "build";
    final private String MARKDOWN_FILE_TYPE = "md";
    final private Set<String> DIRECTORIES_TO_EXCLUDE = Set.of("build");
    final private Set<String> FILES_TO_EXCLUDE = Set.of(CONFIG_FILENAME);

    @Override
    public Integer call() throws Exception {
        if (watchDir) {
            WatchDir watcher = new WatchDir(sourcePath, false);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(watcher);
            executor.shutdown();
            // The while loop allows to rebuild, but it make an infinite loop that should probably be corrected later
            while(!future.isCancelled()) {
                if (watcher.isRebuild()) {
                    building();
                    watcher.setRebuild(false);
                }
            }
            // Shutdown after 10 seconds
            executor.awaitTermination(30, TimeUnit.SECONDS);
            // abort watcher
            future.cancel(true);

            executor.awaitTermination(1, TimeUnit.SECONDS);
            executor.shutdownNow();
        }else {
            building();
        }

        return 0;
    }

    private void building() throws Exception{
        File myPath = new File(System.getProperty("user" + ".dir") + "/" + sourcePath + "/");

        System.out.println("Building in : " + sourcePath);


        Path buildPath = sourcePath.resolve(BUILD_DIRECTORY_NAME);

        System.out.println("buildPath = " + buildPath);

        // Get values from config file and create Layout
        try {
            HashMap<String, String> map = new HashMap<>();

            Path configPath = sourcePath.resolve(CONFIG_FILENAME);
            String configContent = Files.readString(configPath, StandardCharsets.UTF_8);
            ch.heigvd.app.utils.parsers.SiteConfig siteConfig = JsonConverter.convertSite(configContent);


            map.put("title", siteConfig.getTitle());
            map.put("lang", siteConfig.getLang());
            map.put("charset", siteConfig.getCharset());

            Path layoutPath = sourcePath.resolve("template").resolve("layout.html");
            String layoutContent = Files.readString(layoutPath, StandardCharsets.UTF_8);

            layout = new Layout(map, layoutContent);

            copyFiles(sourcePath, buildPath);
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
             *
             * @param dir   Path of the directory
             * @param attrs Attributes of directory
             * @return Status of the directory visit
             * @throws IOException Throw exception if creation fails
             */
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if(DIRECTORIES_TO_EXCLUDE.contains(dir.getFileName().toString()))
                    return FileVisitResult.CONTINUE;

                if (dir.startsWith(sourcePath.resolve("template"))) {
                    HashMap<String, String> map = new HashMap<>();

                    // Get values from config file and create Layout
                    try {
                        Path configPath = sourcePath.resolve(CONFIG_FILENAME);
                        String configContent = Files.readString(configPath);
                        SiteConfig config = JsonConverter.convertSite(configContent);

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
                } else {
                    try {
                        Path destinationPath = destination.resolve(source.relativize(dir));
                        Files.createDirectory(destinationPath);
                        System.out.println("Directory " + destinationPath + " successfully created");
                    } catch (IOException e) {
                        System.err.println("An error was encounter during the creation of a directory: " + e.getMessage());
                        return FileVisitResult.TERMINATE;
                    }
                }

                return FileVisitResult.CONTINUE;
            }

            /**
             * Visit files and copy or convert markdown to html
             *
             * @param file  Path of the file
             * @param attrs Attributes the file
             * @return Status of the file visit
             * @throws IOException Throw exception if creation fails
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // Check if visited file extension is not in excluded list
                if (!FILES_TO_EXCLUDE.contains(file.getFileName().toString())) {
                    String fileExtension = FilenameUtils.getExtension(file.toString());
                    // Check if file extension is markdown to transform in HTML file
                    if (fileExtension.equals(MARKDOWN_FILE_TYPE)) {
                        String pageContent = null;
                        StringBuilder htmlContent = new StringBuilder();
                        StringBuilder pageConfigContent = new StringBuilder();
                        PageConfig pageConfig = null;
                        Map<String, String> pageMetaData = new HashMap<>();

                        try (FileInputStream fis = new FileInputStream(file.toString());
                             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                             BufferedReader reader = new BufferedReader(isr)
                        ) {
                            String str;
                            boolean startToCopy = false;
                            while ((str = reader.readLine()) != null) {
                                if (startToCopy) {
                                    htmlContent.append(MarkdownConverter.convert(str));
                                } else if (str.equals("---")) {
                                    // Copy markdown file header to a PageConfig and start copying markdown from specific line
                                    pageConfig = JsonConverter.convertPage(pageConfigContent.toString());
                                    pageMetaData.put("title", pageConfig.getTitle());
                                    pageMetaData.put("author", pageConfig.getAuthor());
                                    pageMetaData.put("date", pageConfig.getDate());
                                    startToCopy = true;
                                } else {
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

                        if(layout != null){
                            Map<String, Object> data = new HashMap();
                            data.put("site", layout.getSiteMetaData());
                            data.put("page", pageMetaData);
                            data.put("content", htmlContent.toString().trim());

                            TemplateLoader loader = new FileTemplateLoader(sourcePath.resolve("template").toString(), ".html");
                            Handlebars handlebars = new Handlebars(loader);
                            Template template = handlebars.compileInline(layout.getLayoutHtmlContent());
                            pageContent = template.apply(data);
                        }

                        // Write HTML content in destination file
                        OutputStreamWriter htmlWriter = new OutputStreamWriter(new FileOutputStream(htmlFile.toString()), StandardCharsets.UTF_8);
                        htmlWriter.write(pageContent);
                        htmlWriter.flush();
                        htmlWriter.close();
                        System.out.println("File " + htmlFile + " successfully created");
                    } else if (fileExtension.equals("html")) {
                        System.out.println("Convert to html with handlebars : " + file);
                    }
                    // If not markdown, the file will be copied
                    else {
                        // Bug fix Linux
                        if (!file.startsWith(destination) && !file.startsWith(sourcePath.resolve("template"))) {
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
