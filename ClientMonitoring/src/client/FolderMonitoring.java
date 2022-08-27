package client;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.nio.file.StandardWatchEventKinds.*;

public class FolderMonitoring extends Thread {

	private DataOutputStream dos;
	private DataInputStream dis;
	private ClientGUI gui;
	private ObjectOutputStream oos;
	
	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	Logger logger = Logger.getLogger("MyLog");
	FileHandler fh;

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public FolderMonitoring(ClientGUI gui,ObjectOutputStream oos,DataInputStream dis, DataOutputStream dos) {
		this.dos = dos;
		this.dis = dis;
		this.gui = gui;
		this.oos = oos;
	}

	@Override
	public void run() {
		try {
			Message message = new Message();
			// Client connect server
			dos.writeUTF("");
		
			// Mornitoring folder
			FileSystem fs = FileSystems.getDefault();
			WatchService ws = fs.newWatchService();
			Path pTemp = Paths.get("D:/Monitoring");
			pTemp.register(ws, new WatchEvent.Kind[] { ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE });
			while (true) {
				WatchKey k = ws.take();
				for (WatchEvent<?> e : k.pollEvents()) {
					Object c = e.context();
//					dos.writeUTF(String.valueOf(e.kind()));
//					dos.writeUTF(String.valueOf(e.count()));
//					dos.writeUTF(String.valueOf(c));
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					message.setTime(dtf.format(now).toString());
					message.setKind(e.kind().toString());
					message.setAction(c.toString());
					
					oos.writeObject(message);
					
					System.out.printf("%s %d %s\n", e.kind(), e.count(), c);
					gui.fillTable(message);
					// write log
					try {

						// This block configure the logger with handler and formatter
						fh = new FileHandler("Monitoring.log");
						logger.addHandler(fh);
						SimpleFormatter formatter = new SimpleFormatter();
						fh.setFormatter(formatter);

						// the following statement is used to log any messages
						logger.info(e.kind() + " " + e.count() + " " + c);

					} catch (SecurityException er) {
						er.printStackTrace();
					} catch (IOException er) {
						er.printStackTrace();
					}
				}
				k.reset();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
