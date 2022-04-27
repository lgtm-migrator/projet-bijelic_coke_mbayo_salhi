package ch.heigvd.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "build")
public class Build implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to build " + "directory")
    private String sourcePath;
    final private String BUILD_DIRECTORY_NAME = "build";

    @Override
    public Integer call() throws Exception {
        File myPath = new File(System.getProperty("user" + ".dir") + "/" + sourcePath + "/");

        System.out.println("Building in : " + sourcePath);

        createBuildFolder(sourcePath);

        return 0;
    }

    /**
     * Create a /build folder in specified directory
     */
    private void createBuildFolder(String directory) throws IOException {
        Path folderPath = Paths.get(directory);
        if(Files.exists(folderPath)){
            Path buildPath = Paths.get(folderPath + "/build");
            Files.createDirectories(buildPath);
        }
        else {
            throw new FileNotFoundException("Specified file does not exists !");
        }
    }

    /**
     * Recursive directory coping with parsing for certain files
     * @param directory Directory where files are located
     */
    private void copyFiles(String directory){
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
