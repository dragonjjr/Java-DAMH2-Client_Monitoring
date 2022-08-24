package server;
import client.Client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


public class Server{

    private String IP;
    private int Port;
    ServerSocket serverSocket;
    private ArrayList<Client> listClient;

    public ArrayList<Client> getListClient() {
        return listClient;
    }

    public String getIP()
    {
        return IP;
    }

    public int getPort()
    {
        return Port;
    }


    public Server() throws UnknownHostException {
            IP = InetAddress.getLocalHost().getHostAddress();
            Port = 6000;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(Port);
        listClient = new ArrayList<Client>();
        System.out.println("Server is listening on port " + Port);


        //Tạo thread riêng cho server
        Thread t = new Thread(){
            @Override
            public void run(){
                while (serverSocket != null && !serverSocket.isClosed()) {
                    try {
                        Socket socket = serverSocket.accept();

                        System.out.println("New client connected " + socket);

                        //Tạo thread cho từng client
                        ClientHandle clientHandler = new ClientHandle(socket);
                        clientHandler.start();

                    } catch (IOException e) {
                        break;
                    }
                }
            }
        };
        t.start();
    }

    public void CloseServer() throws IOException {
        serverSocket.close();
    }


    public void addNewClient(Client client)
    {
        this.listClient.add(client);
        System.out.println(this.listClient);
        Main.getGuiServer().updateList_Client();
    }

}
