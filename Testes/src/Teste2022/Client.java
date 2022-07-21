package Teste2022;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String [] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        String line;
        while((line = systemIn.readLine())!=null) {
            out.println(line);
            out.flush();

            String estaVerificado = in.readLine();
            System.out.println(estaVerificado);

            if(!estaVerificado.equals("INVALIDO")) {
                String voto = systemIn.readLine();
                out.println(voto);
                out.flush();
            }
            else break;

            String confirmacao = in.readLine();
            System.out.println(confirmacao);
        }

        socket.shutdownOutput();
        System.out.println("Vencedor: " + in.readLine());

        socket.shutdownInput();
        socket.close();
    }
}
