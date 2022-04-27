package ch.heigvd.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.javalin.Javalin;

@Command(name = "serve")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to init " +
            "directory")
    private String path;

    @Override
    public Integer call() throws Exception {
        Javalin app = Javalin.create().start(7070);
        int i = 0;

        app.get("/", ctx -> ctx.result("Hello World"));

        TimeUnit.SECONDS.sleep(5);

        System.out.println("Serve");
        return 0;
    }
}
