import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    public static Contact parseLine (String userInput) {
        String[] tokens = userInput.split(" ");

        if (tokens[3].equals("null")) tokens[3] = null;

        return new Contact(
                tokens[0],
                Integer.parseInt(tokens[1]),
                Long.parseLong(tokens[2]),
                tokens[3],
                new ArrayList<>(Arrays.asList(tokens).subList(4, tokens.length)));
    }


    public static void main (String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        ContactList cl = ContactList.deserialize(dis);
        System.out.println(cl);

        String userInput;
        while ((userInput = systemIn.readLine()) != null) {
            Contact newContact = parseLine(userInput);
            System.out.println(newContact.toString());

            newContact.serialize(dos);
            dos.flush();
        }
        socket.close();
    }
}
