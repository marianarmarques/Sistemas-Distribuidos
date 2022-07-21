package Teste2022;

import Teste2022.Exceptions.CabineNaoExistenteException;
import Teste2022.Exceptions.EleitorNaoRegistadoException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ServerWorker implements Runnable {

    private Socket socket;
    private Votacao votacao;

    public ServerWorker(Socket s, Votacao v) {
        this.socket = s;
        this.votacao = v;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            int i=0;
            boolean b;

            String line;
            while((line = in.readLine())!=null) {
                b = votacao.verifica(Integer.parseInt(line));
                if(!b) {
                    out.println("INVALIDO");
                    out.flush();
                    break;
                }
                else {
                    i = votacao.esperaPorCabine();
                    out.println("VOTE NA CABINE " + i);
                    out.flush();

                    String voto = in.readLine();
                    votacao.vota(Integer.parseInt(voto));

                    out.println("OK");
                    out.flush();

                }
            }
            if(i!=0) votacao.desocupaCabine(i);

            out.println(votacao.vencedor());
            out.flush();

            socket.shutdownInput();
            socket.shutdownOutput();

        } catch (IOException | EleitorNaoRegistadoException | InterruptedException | CabineNaoExistenteException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            Votacao v = new Votacao();

            while(true) {
                Socket s = ss.accept();

                Thread worker = new Thread(new ServerWorker(s, v));
                worker.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
