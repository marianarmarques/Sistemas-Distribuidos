import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class ClientHandler extends Thread {
    private Socket s;
    private MediaGlobal mg;
    public ClientHandler(Socket s, MediaGlobal mg) {
        this.s = s;
        this.mg = mg;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            PrintWriter out = new PrintWriter(this.s.getOutputStream());

            int total = 0, n = 0;

            String line;
            while ((line = in.readLine()) != null) {
                n++;
                total += parseInt(line);
                out.println(total);
                out.flush();

                mg.acrescenta(parseInt(line));
            }

            out.println(total / n);
            out.flush();
            out.println(mg.calculaMedia());
            out.flush();

            this.s.shutdownOutput();
            this.s.shutdownInput();
            this.s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
