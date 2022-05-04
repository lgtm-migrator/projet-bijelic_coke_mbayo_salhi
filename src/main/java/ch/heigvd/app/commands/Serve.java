package ch.heigvd.app.commands;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "serve")
public class Serve implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("Serve");
        return 0;
    }
}
