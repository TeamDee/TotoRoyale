package GameNetworking;

/**
 * Created by Z_K on 4/1/2017.
 */
public class MockServerClientRunner {
    public static void main(String[] args){
        int port = 666;
//        int port = 1708;
//        String serverName = "10.136.31.59";
//          String serverName = "10.136.15.159";
//        String serverName = "10.136.125.76";
//        String serverName = "10.228.1.171";
//        String serverName = "10.192.246.753";
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
//            byte[] rawAddress = { (byte)192, (byte)168, (byte)1, (byte)142};
//            InetAddress address = InetAddress.getByAddress(rawAddress);
//            Socket socket = new Socket(address, 6969);
//            System.out.print("Conneccted!");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
