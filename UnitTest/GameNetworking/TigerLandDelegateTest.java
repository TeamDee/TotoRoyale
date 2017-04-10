package GameNetworking;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Z_K on 4/6/2017.
 */
public class TigerLandDelegateTest {
    TigerLandDelegate delegate;
    ServerSocket server;
    BufferedReader clientIn, serverIn;
    PrintWriter clientOut, serverOut;
    @Before
    public void setUp(){
        try {
            server = new ServerSocket(6065);
            delegate = new TigerLandDelegate("localhost", 6065);
            Socket socket = server.accept();
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOut = new PrintWriter(socket.getOutputStream());
            clientIn = delegate.getClient().getDataInputStream();
            clientOut = delegate.getClient().getDataOutputStream();
        } catch (IOException ex){
            Assert.fail();
        }
    }

    @Test
    public void AuthenticationTest(){
        serverOut.println("TWO SHALL ENTER ONE SHALL LEAVE");
        serverOut.println("WAIT FOR THE TOURNAMENT TO BEGIN " + "10101");
        boolean outcome = delegate.AuthenticationProtocol(clientIn, clientOut);
        Assert.assertTrue(outcome);
    }

    @Test
    public void ChallengeTest(){

    }
}
