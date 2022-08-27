package server;

import client.Client;
import client.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {

	Logger logger = Logger.getLogger("MyLog");
	FileHandler fh;

	private String IP;
	private int Port;
	ServerSocket serverSocket;
	private ArrayList<Client> listClient;

	public ArrayList<Client> getListClient() {
		return listClient;
	}

	public String getIP() {
		return IP;
	}

	public int getPort() {
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

		// Tạo thread riêng cho server
		Thread t = new Thread() {
			@Override
			public void run() {
				while (serverSocket != null && !serverSocket.isClosed()) {
					try {
						Socket socket = serverSocket.accept();

						System.out.println("New client connected " + socket);
						// write log.

						try {

							// This block configure the logger with handler and formatter
							fh = new FileHandler("Monitoring.log");
							logger.addHandler(fh);
							SimpleFormatter formatter = new SimpleFormatter();
							fh.setFormatter(formatter);

							// the following statement is used to log any messages
							logger.info("New client connected " + socket);

						} catch (SecurityException er) {
							er.printStackTrace();
						} catch (IOException er) {
							er.printStackTrace();
						}

						// Tạo thread cho từng client
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


	public void addNewClient(Client client) throws IOException {
		
		this.listClient.add(client);
		Main.getGuiServer().updateList_Client();
		
		// get the input stream from the connected socket
		Socket socket = new Socket("localhost", client.getSocket().getPort());
		InputStream inputStream = socket.getInputStream();
		// create a DataInputStream so we can read data from it.
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

		// read the message from the socket
		Message message;
		try {
			message = (Message)objectInputStream.readObject();
			Main.getGuiServer().fillTable(message);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(this.listClient);

	}

}
