package ch.heigvd.app;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

import io.javalin.Javalin;

@Command(name = "serve")
public class Serve implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("Serve");
        return 0;
    }
}
