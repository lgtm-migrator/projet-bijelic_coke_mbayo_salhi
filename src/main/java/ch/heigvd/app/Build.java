package ch.heigvd.app;

import ch.heigvd.app.utils.MarkdownConverter;
import com.ibm.icu.impl.ICULocaleService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(name = "build")
public class Build implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to build " + "directory")
    private String sourcePath;

    final private String BUILD_DIRECTORY_NAME = "build";
    final private String MARKDOWN_FILE_TYPE = "md";
    final private Set<String> FILE_TYPE_TO_EXCLUDE = Set.of("yaml");

    @Override
    public Integer call() throws Exception {
        File myPath = new File(System.getProperty("user" + ".dir") + "/" + sourcePath + "/");

        System.out.println("Building in : " + sourcePath);

        copyFiles(sourcePath, sourcePath + "/" + BUILD_DIRECTORY_NAME);

        return 0;
    }

    /**
     * Recursive directory copiing with parsing for certain files
     * @param sourcePath Directory where files are located
     * @param buildPath Directory where files are being copied to
     */
    private void copyFiles(String sourcePath, String buildPath, CopyOption... options) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(buildPath);

        if(Files.exists(destination))
            FileUtils.deleteDirectory(destination.toFile());

        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(destination.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileExtension = FilenameUtils.getExtension(file.toString());
                if(!FILE_TYPE_TO_EXCLUDE.contains(fileExtension)){
                    if(fileExtension.equals(MARKDOWN_FILE_TYPE)){
                        StringBuilder HTMLContent = new StringBuilder();

                        try (FileInputStream fis = new FileInputStream(file.toString());
                             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                             BufferedReader reader = new BufferedReader(isr)
                        ) {
                            String str;
                            boolean startToCopy = false;
                            while ((str = reader.readLine()) != null) {
                                if(str.equals("==="))
                                    startToCopy = true;
                                if(startToCopy){
                                    HTMLContent.append(MarkdownConverter.convert(str));
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("Error while ");
                        }

                    }
                    Files.copy(file, destination.resolve(source.relativize(file)), options);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        // For each file in a directory
            // If it is a .md file
                // parseMarkdownToHTML
            // Else if not yaml
                // Simply copy file
            // Else if another direcory
                // create directory in dest
                // copyFiles of that directory
    }

    /**
     * Parse markdown file content to HTML
     * @param sourceFileName Source markdown file location
     * @param destinationFileName Destination HTML file location
     */
    private void parseMarkdownToHTML(String sourceFileName, String destinationFileName){
        // Open MD src file with readline
        // Create HTML file and open it with readline
        // Ignore header (remove) (check if there is a header)
        // Markdown (src) to HTML (dest) with the use of a buffer
        // Close files
    }
}
