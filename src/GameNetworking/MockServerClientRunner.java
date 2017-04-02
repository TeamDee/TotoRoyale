package GameNetworking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

/**
 * Created by Z_K on 4/1/2017.
 */
public class MockServerClientRunner {
    public static void main(String[] args){
        int port = 6066;
        String serverName = "localhost";
        try {
            Thread t = new MockTigerLandServer(port);
            t.start();

            TigerLandClient client = new TigerLandClient(serverName, port);
            DataOutputStream outputStream = client.getDataOutputStream();
            DataInputStream inputStream = client.getDataInputStream();
            Scanner scanner = new Scanner(System.in);
            while(true){
                String message = scanner.nextLine();
                outputStream.writeUTF(message);
                String received = inputStream.readUTF();
                System.out.println("Received:\n" + received);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
