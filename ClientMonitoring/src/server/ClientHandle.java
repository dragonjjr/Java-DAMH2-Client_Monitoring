package server;

import client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.MessageDigest;

public class ClientHandle extends Thread{

    Client client;
    private String messageClient;
    
    public String getMessageClient() {
		return messageClient;
	}

	public void setMessageClient(String messageClient) {
		this.messageClient = messageClient;
	}

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
//                String header = client.getDis().readUTF();
//                if (header == null)
//                    throw new IOException();
//
//                System.out.println("Header: " + header);

                Main.getServer().addNewClient(client);
            }
        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
