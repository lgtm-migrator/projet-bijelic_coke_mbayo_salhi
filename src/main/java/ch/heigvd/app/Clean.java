package ch.heigvd.app;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "clean")
public class Clean implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("Clean");
        return 0;
    }
}
