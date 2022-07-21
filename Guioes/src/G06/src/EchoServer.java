import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class EchoServer {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);

            MediaGlobal mg = new MediaGlobal();

            while (true) {
                Socket socket = ss.accept();
                Thread t = new ClientHandler(socket, mg);
                t.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
