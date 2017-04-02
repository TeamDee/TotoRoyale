package GameNetworking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        } catch (IOException ex){
            System.out.println("Connection failed.");
        }
    }

    public DataInputStream getDataInputStream(){
        DataInputStream dataInputStream = null;
        try{
            dataInputStream = new DataInputStream(client.getInputStream());
        } catch(IOException ex){
            System.out.println("Failed to obtain a DataInputStream.");
        }
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream(){
        DataOutputStream dataOutputStream = null;
        try{
            dataOutputStream = new DataOutputStream(client.getOutputStream());
        } catch(IOException ex){
            System.out.println("Failed to obtain a DataOutputStream.");
        }
        return dataOutputStream;
    }
}
