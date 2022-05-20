package ch.heigvd.app.commands;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

@Command(name = "publish")
public class Publish implements Callable<Integer> {
    private final static Logger LOGGER = Logger.getLogger(Publish.class.getName());

    @Override
    public Integer call() throws Exception {

        

        return 0;
    }
}
