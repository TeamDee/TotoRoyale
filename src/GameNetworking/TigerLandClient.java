package GameNetworking;

import java.io.*;
import java.net.Socket;

/**
 * Created by Z_K on 4/1/2017.
 */
public class TigerLandClient {
    private Socket client;

    public TigerLandClient(String serverName, int portNumber){
        try{
            System.out.println("Connecting to Server: " + serverName + " on port number: " + portNumber + " ...");
            client = new Socket(serverName, portNumber);
            System.out.println("Connection established.");
        } catch (IOException ex) {
            System.out.println("Connection failed.");
        }
    }

    public BufferedReader getDataInputStream(){
        BufferedReader dataInputStream = null;
        try{
            dataInputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch(IOException ex){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\nFailed to obtain a DataInputStream.");
        }
        return dataInputStream;
    }

    public PrintWriter getDataOutputStream(){
        PrintWriter dataOutputStream = null;
        try{
            dataOutputStream = new PrintWriter(client.getOutputStream(), true);
        } catch(IOException ex){
            System.out.println("Failed to obtain a DataOutputStream.");
        }
        return dataOutputStream;
    }
}
