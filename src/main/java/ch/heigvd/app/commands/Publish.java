package ch.heigvd.app.commands;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Command(name = "publish")
public class Publish implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to publish")
    private String path;
    private final static Logger LOGGER = Logger.getLogger(Publish.class.getName());

    @Override
    public Integer call() throws Exception {

        LOGGER.setLevel(Level.INFO);
        System.out.println("publishing directory on GitHub: " + path.toString());

        //File localPath = new File(System.getProperty("user" + ".dir"));
        File localPath = new File(path);
        System.out.println(localPath.toString());

        LOGGER.info(localPath.toString());

        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        // Check if the current directory is a git repository. If not it
        // initialize it.
        Git git = null;
        try {
            git = Git.open(localPath);
            System.out.println("localpath: " + path);
            LOGGER.info("Already a Git repository: " + path);
        } catch (RepositoryNotFoundException e) {
            LOGGER.info("Initialising " + path + " as a git repository");
            try {
                git = Git.init().setDirectory(localPath).call();
                LOGGER.log(Level.INFO, "Sucessfully initialized git " +
                        "repository");

                System.out.println("Enter the repository url: ");
                String url = scanner.nextLine();  // Read user input
                System.out.println("url: " + url);  // Output user input

                // add remote repository
                RemoteAddCommand remoteAddCommand = git.remoteAdd();
                remoteAddCommand.setName("origin");

                remoteAddCommand.setUri(new URIish(url));

                remoteAddCommand.call();
                LOGGER.log(Level.INFO, "Sucessfully set remote");
            } catch (GitAPIException e1) {
                System.out.println("Error setting git project");
                e1.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Error openning git project");
            e.printStackTrace();
        }

        // Ask the user to enter the remote git repository url and their
        // GitHub Access token

        System.out.println("Enter your GitHub access token: ");

        String token = scanner.nextLine();  // Read user input

        if (git != null) {
            git.add().addFilepattern(localPath.getAbsolutePath() + File.separator + "build").call();
            System.out.println(localPath.getAbsolutePath() + File.separator + "build");
            LOGGER.log(Level.INFO, "Added build/ directory to stagging");

            git.commit().setMessage("static publish build directory").setSign(false).call();

            // push to remote
            PushCommand pushCommand = git.push();

            pushCommand.setCredentialsProvider(new
                    UsernamePasswordCredentialsProvider(token, ""));
            pushCommand.call();

            LOGGER.log(Level.INFO, "Push was successful");
            System.out.println("publish done");
        } else {
            LOGGER.log(Level.SEVERE, "Error: git variable null");
        }

        return 0;
    }
}
