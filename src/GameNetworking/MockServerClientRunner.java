package GameNetworking;

/**
 * Created by Z_K on 4/1/2017.
 */
public class MockServerClientRunner {
    public static void main(String[] args){
        int port = 666;
        String serverName = "192.168.1.106";
        try {
            /** Only local testing purpose.
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
            }*/

            // Actually testing with server team
            TigerLandDelegate delegate = new TigerLandDelegate();
            delegate.TournamentProtocol();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
