package Exame2016;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String [] args) throws IOException {
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line;
            while((line = systemIn.readLine())!=null) {
                if(line.equals("Pergunta")) {
                    out.println(line);
                    out.flush();

                    String pergunta = in.readLine();
                    System.out.println("Pergunta: " + pergunta);

                    String resposta = systemIn.readLine();
                    out.println(resposta);
                    out.flush();

                    String avaliacao = in.readLine();
                    System.out.println("Avaliação: " + avaliacao);
                }
            }

            socket.shutdownOutput();
            socket.getInputStream();
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
