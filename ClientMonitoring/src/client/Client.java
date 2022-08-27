package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ClientGUI gui;
    private ObjectOutputStream oos;
    
    public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

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
        this.oos = new ObjectOutputStream(this.socket.getOutputStream());
        Monitoring();
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public void Monitoring() {
        FolderMonitoring fdm = new FolderMonitoring(this.gui,this.oos,this.getDis(),this.getDos());
        fdm.start();
    }

    public String toString()
    {
        return this.socket.toString();
    }
    
    public Client(ClientGUI gui)
    {
    	this.gui = gui;
    }
}
