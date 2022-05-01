package ch.heigvd.app;

import io.javalin.http.Context;
import org.junit.Test;
import picocli.CommandLine;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class ServeTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    /**
     * statique serve path doit lancer un serveur
     */
    public void shouldCreateWebServer(){
        // run the command
        Main app = new Main();
        StringWriter sw = new StringWriter();
        CommandLine cmd = new CommandLine(app);
        cmd.setOut(new PrintWriter(sw));

        File path = new File("montest/sitetest/");
        File root = new File("montest/");

        //if directory doesn't exist, init it
        if(!Files.exists(root.toPath())){
            cmd.execute("init", path.toString());
        }

        cmd.execute("serve", path.toString());

        //Reach server
        String hostname = "localhost";
        int port = 7070;
        Socket clientSocket;
        BufferedReader is;
        PrintWriter os;
        try {
            clientSocket = new Socket(hostname, port);

            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            os = new PrintWriter(clientSocket.getOutputStream());

            //Construction et envoi d'une requête GET
            StringBuilder request = new StringBuilder("GET / HTTP/1.0\r\n");
            request.append("Host: "+ hostname +"\r\n");
            request.append("Accept: text/plain, text/html, application/json\r\n");
            request.append("Accept-Charset: utf-8\r\n");
            request.append("\r\n");

            os.println(request.toString());
            os.flush();

            //Reception de la réponse
            StringBuilder result = new StringBuilder();
            String response;
            while((response = is.readLine()) != null){
                System.out.println(response);
            }

            assertNotEquals("",response);

            //fermetures des stream/socket
            os.close();
            is.close();
            clientSocket.close();

        } catch (IOException ex){
            //M'indique s'il y a un problème dans le try
            fail();
        }
    }

}
