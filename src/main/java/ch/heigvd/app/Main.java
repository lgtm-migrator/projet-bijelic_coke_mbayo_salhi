package ch.heigvd.app;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.util.concurrent.Callable;


@Command(name = "static",
        mixinStandardHelpOptions = true,
        description = "Generate random static websites",
        subcommands = {New.class, Clean.class, Build.class, Serve.class})
public class Main implements Callable<Integer>
{
    @CommandLine.Option(names = {"-version"}, description = "display version " +
            "info")
    boolean versionInfoRequested;

    @Override
    public Integer call() {
        if (versionInfoRequested) {
            String version = System.getProperty(
                    "project.version");

            System.out.println("Current version : " + version);
        } else {
            System.out.println("Static");
        }
        return 0;
    }

    public static void main( String[] args )
    {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
