package ch.heigvd.app.commands;

import org.apache.commons.io.FileUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "clean")
public class Clean implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to build directory")
    private String path;

    @Override
    public Integer call() throws Exception {

        Path pathToClean = Paths.get(System.getProperty("user" + ".dir")).resolve(path).resolve("build");
        System.out.println("PathTClean = " + pathToClean);

        FileUtils.deleteDirectory(pathToClean.toFile());

        System.out.println("Build directory cleaned");

        return 0;
    }
}
