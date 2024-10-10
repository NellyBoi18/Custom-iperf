import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Iperfer {
    public static void main(String[] args) {
        // Check for minimum number of arguments to determine mode
        if (args.length < 1) {
            System.out.println("Error: missing or additional arguments");
            return;
        }

        // Determine mode based on the provided arguments and call the respective method
        if (args[0].equals("-c") && args.length == 7) { 
            runClient(args);
        } else if (args[0].equals("-s") && args.length == 3) { 
            runServer(args);
        } else {
            System.out.println("Error: missing or additional arguments");
        }


    }

    private static void runClient(String[] args) {
        String serverHostname = args[2];
        int serverPort = Integer.parseInt(args[4]);
        int time = Integer.parseInt(args[6]);

        if (serverPort < 1024 || serverPort > 65535) {
            System.out.println("Error: port number must be in the range 1024 to 65535");
            return;
        }

        try (Socket socket = new Socket(serverHostname, serverPort)) {
            OutputStream out = socket.getOutputStream();
            byte[] data = new byte[1000]; // 1000 bytes of zeroed data
            long endTime = System.currentTimeMillis() + (time * 1000);
            long totalBytesSent = 0;

            while (System.currentTimeMillis() < endTime) {
                out.write(data);
                totalBytesSent += data.length;
            }

            double rateMbps = ((totalBytesSent * 8.0) / (time * 1000000.0)); 
            System.out.println("sent=" + (totalBytesSent / 1000) + " KB rate=" + rateMbps + " Mbps");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void runServer(String[] args) {
        int listenPort = Integer.parseInt(args[2]);
        
        if (listenPort < 1024 || listenPort > 65535) {
            System.out.println("Error: port number must be in the range 1024 to 65535");
            return;
        }
        
        try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
            System.out.println("Server is listening on port " + listenPort);
            Socket clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();
            byte[] data = new byte[1000]; // Buffer to store received data
            long totalBytesReceived = 0;
            long startTime = System.currentTimeMillis();
            
            int length;
            while ((length = in.read(data)) != -1) {
                totalBytesReceived += length;
            }
            
            long endTime = System.currentTimeMillis();

            double rateMbps = (((totalBytesReceived * 8.0) / 1000000 )/ ((endTime - startTime) / 1000.0)); 
            System.out.println("received=" + (totalBytesReceived / 1000) + " KB rate=" + rateMbps + " Mbps");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




