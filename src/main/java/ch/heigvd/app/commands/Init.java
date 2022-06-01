package ch.heigvd.app.commands;

import org.apache.commons.io.FileUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "init")
public class Init implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to init " +
            "directory")
    private String path;

    @CommandLine.Option(names = {"-f", "--force"}, description = "Overwrite files")
    private boolean overwrite;

    @Override
    public Integer call() throws Exception {

        Path myPath = Paths.get(System.getProperty("user" + ".dir")).resolve(path);

        System.out.println("Initializing: " + path);


        Boolean exists = false;
        if(!overwrite) {
            File f = Paths.get("config").toFile();

            // Populates the array with names of files and directories
            String[] pathnames = f.list();

            System.out.println("Pathnames = " + pathnames);

            // check if files already exists in destination folder
            if(pathnames != null){
                for (String file : pathnames) {
                    File pathToFile = myPath.resolve(file).toFile();
                    if (Files.exists(pathToFile.toPath())) {
                        System.out.println("File \"" + file + "\" already exists");
                        if (!exists) {
                            exists = true;
                        }
                    }
                }
            }

            if (exists) {
                System.out.println("Files already exists in destination folder. " +
                        "If you want to overwrite them use :");
                System.out.println("statique init <Path> --force>");
            }
        }

        // copy config directory to init path
        if(!exists || overwrite) {
            File sourceDirectory = Paths.get("config").toFile();
            File destinationDirectory = myPath.toFile();

            sourceDirectory.mkdirs();

            FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
        }

        return 0;
    }
}
