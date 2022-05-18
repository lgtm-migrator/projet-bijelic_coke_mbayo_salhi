package ch.heigvd.app.commands;

import io.javalin.plugin.rendering.FileRenderer;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.javalin.Javalin;

@Command(name = "serve")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to serve " +
            "directory")
    private String path;

    @Override
    public Integer call() throws Exception {
        Javalin app = Javalin.create().start(7070);
        FileRenderer FileRenderer;
        AtomicBoolean running = new AtomicBoolean(true);
        File index = new File(System.getProperty("user" + ".dir") + "/" + path + "/build/index.html");
        if(!Files.exists(index.toPath())){
            System.out.println("Le fichier index.html n'existe pas!");
            TimeUnit.SECONDS.sleep(3);
            return -1;
        }
        System.out.println("Serve");
        app.get("/", ctx -> ctx.html(new String(Files.readAllBytes(Paths.get(index.toString())))));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop();
            running.set(false);
        }));

        // run until server shutdown (may be a better option there)
        //while(running.get()){
            TimeUnit.SECONDS.sleep(5);
        //}

        return 0;
    }
}
