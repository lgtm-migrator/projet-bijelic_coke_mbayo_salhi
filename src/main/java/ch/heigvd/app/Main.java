package ch.heigvd.app;

import picocli.CommandLine.Command;

/**
 * Hello world!
 *
 */
@Command(name = "main")
public class Main
{
    @Command(name = "new")
    public void newCommand(){
        // New command to be implemented here
    }

    @Command(name = "clean")
    public void cleanCommand(){
        // Clean command to be implemented here
    }

    @Command(name = "build")
    public void buildCommand(){
        // Build command to be implemented here
    }

    @Command(name = "serve")
    public void serveCommand(){
        // Serve command to be implemented here
    }
}
