package ch.heigvd.app.commands;

import ch.heigvd.app.Main;
import io.javalin.plugin.rendering.FileRenderer;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import io.javalin.Javalin;
import utils.watchDir.WatchDir;

@Command(name = "serve")
public class Serve implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to serve directory")
    private Path path;

    @CommandLine.Option(names = {"-w", "--watch"}, description = "Allows to regenerate site when modification are made")
    private boolean watchDir;

    @Override
    public Integer call() throws Exception {
        Main appMain = new Main();
        CommandLine cmd = new CommandLine(appMain);
        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));
        Javalin app = Javalin.create().start(7070);
        AtomicBoolean running = new AtomicBoolean(true);
        File index =
                new File(System.getProperty("user" + ".dir"));
        Path indexPath = index.toPath().resolve(path.toString()).resolve(
                "build").resolve("index.html");


        if(!Files.exists(indexPath)){
            System.out.println("Le fichier index.html n'existe pas!");
            TimeUnit.SECONDS.sleep(3);
            return -1;
        }

        System.out.println("Serve");
        app.get("/", ctx -> ctx.html(new String(Files.readAllBytes(indexPath))));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop();
            running.set(false);
        }));

        if (watchDir) {
            WatchDir watcher = new WatchDir(path, false);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(watcher);
            executor.shutdown();

            while(!future.isCancelled()) {
                if (watcher.isReserve()) {
                    cmd.execute("build", path.toString());
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("Re-served");
                    watcher.setReserve(false);
                }

            }
            // Shutdown after 10 seconds
            executor.awaitTermination(30, TimeUnit.SECONDS);
            // abort watcher
            future.cancel(true);

            executor.awaitTermination(1, TimeUnit.SECONDS);
            executor.shutdownNow();
        }

        return 0;
    }
}
