import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Arrays.asList;

class ContactManager {
    private HashMap<String, Contact> contacts;
    private ReentrantLock lock;

    public ContactManager() {
        this.contacts = new HashMap<>();
        this.lock = new ReentrantLock();

        // example pre-population
        update(new Contact("John", 20, 253123321, null, List.of("john@mail.com")));
        update(new Contact("Alice", 30, 253987654, "CompanyInc.", asList("alice.personal@mail.com", "alice.business@mail.com")));
        update(new Contact("Bob", 40, 253123456, "Comp.Ld", asList("bob@mail.com", "bob.work@mail.com")));
    }


    // @TODO
    public void update(Contact c) {
        try {
            this.lock.lock();
            if(this.contacts.containsKey(c.name())) {
                this.contacts.replace(c.name(), c);
            }
            else {
                this.contacts.put(c.name(), c);
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    // @TODO
    public ContactList getContacts() {
        try {
            this.lock.lock();
            ContactList cl = new ContactList();

            for(Map.Entry<String, Contact> c : this.contacts.entrySet()) {
                cl.add(c.getValue());
            }

            return cl;
        }
        finally {
            this.lock.unlock();
        }
    }
}

class ServerWorker implements Runnable {
    private Socket socket;
    private ContactManager manager;

    public ServerWorker (Socket socket, ContactManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    // @TODO
    @Override
    public void run() {
        boolean open = true;

        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));

            this.manager.getContacts().serialize(dos);
            dos.flush();

            while(open) {
                Contact c = Contact.deserialize(dis);
                if(c==null) open = false;
                manager.update(c);
            }

            this.socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Server {

    public static void main (String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            ContactManager manager = new ContactManager();

            while (true) {
                Socket socket = serverSocket.accept();

                Thread worker = new Thread(new ServerWorker(socket, manager));

                worker.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}