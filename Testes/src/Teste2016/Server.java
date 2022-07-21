package Teste2016;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ServerWorker implements Runnable {
    private Socket socket;
    private Controlador controlador;

    public ServerWorker(Socket socket, Controlador controlador) {
        this.socket = socket;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            int i=0;

            String line;
            while((line=in.readLine())!=null) {
                if(line.equals("Pergunta")) {
                    Questao q = this.controlador.obtem(i);

                    out.println(q.getPergunta());
                    out.flush();

                    String resposta = in.readLine();

                    String avaliacao = q.charToString(resposta);
                    if(!avaliacao.equals("Errada")) i = q.id();
                    out.println(avaliacao);
                    out.flush();
                }
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Server {

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket ss = new ServerSocket(12345);

            Controlador controlador = new Controlador();

            while(true) {
                Socket socket = ss.accept();

                Thread worker = new Thread(new ServerWorker(socket, controlador));
                worker.start();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
