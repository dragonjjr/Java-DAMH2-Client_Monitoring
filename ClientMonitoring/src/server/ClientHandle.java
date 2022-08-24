package server;

import client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandle extends Thread{

    Client client;
    public ClientHandle(Socket s) throws IOException {
        client = new Client();
        client.setSocket(s);
        client.setDos(new DataOutputStream(client.getSocket().getOutputStream()));
        client.setDis(new DataInputStream(client.getSocket().getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String header = client.getDis().readUTF();
                if (header == null)
                    throw new IOException();

                System.out.println("Header: " + header);

                Main.getServer().addNewClient(client);
            }
        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
