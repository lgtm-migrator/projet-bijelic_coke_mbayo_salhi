package ch.heigvd.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "build")
public class Build implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to build " + "directory")
    private String sourcePath;
    final private String buildDirectoryName = "build";

    @Override
    public Integer call() throws Exception {
        File myPath = new File(System.getProperty("user" + ".dir") + "/" + sourcePath + "/");

        System.out.println("Building in : " + sourcePath);

        return 0;
    }

    /**
     * Create a /build folder in specified directory
     */
    private void createBuildFolder(String directory){
        // If directory is not empty
            // Create a /build folder
        // Else
            // throw error
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
     * @param srcFileName Source markdown file location
     * @param destFileName Destination HTML file location
     */
    private void parseMarkdownToHTML(String srcFileName, String destFileName){
        // Open MD src file with readline
        // Create HTML file and open it with readline
        // Ignore header (remove) (check if there is a header)
        // Markdown (src) to HTML (dest) with the use of a buffer
        // Close files
    }
}
