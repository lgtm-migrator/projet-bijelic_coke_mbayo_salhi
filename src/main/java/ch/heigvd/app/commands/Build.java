package ch.heigvd.app.commands;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "build")
public class Build implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("Build");
        return 0;
    }
}
