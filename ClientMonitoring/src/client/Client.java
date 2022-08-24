package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public Client() {}

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void startClient(String ipServer, int portServer) throws IOException {
        this.socket = new Socket(ipServer, portServer);
        this.dis = new DataInputStream(this.socket.getInputStream());
        this.dos = new DataOutputStream(this.socket.getOutputStream());
        Test();
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public void Test() {
        FolderMonitoring fdm = new FolderMonitoring(this.getDis(),this.getDos());
        fdm.start();
    }

    public String toString()
    {
        return this.socket.toString();
    }
}
