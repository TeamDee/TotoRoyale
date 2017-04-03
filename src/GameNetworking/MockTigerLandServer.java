package GameNetworking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Z_K on 4/1/2017.
 */
public class MockTigerLandServer extends Thread{
    private ServerSocket serverSocket;

    public MockTigerLandServer(int port){
        try{
            System.out.println("Attempting to start a server socket on port " + port + " ...");
            serverSocket = new ServerSocket(port);
            System.out.println("Server socket successfully established.");
        } catch(IOException ex){
            System.out.println("Cannot start a server socket!");
        }
    }

    public void run(){
        while(true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                while(true) {
                    String message = in.readUTF();
                    System.out.println(message);

//                    out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
//                            + "\n");
                    out.writeUTF("Echo received: " + message);
                }
//                server.close();
            }
            catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }
            catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
