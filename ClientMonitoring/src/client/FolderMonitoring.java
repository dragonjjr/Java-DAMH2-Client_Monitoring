package client;

import java.io.*;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FolderMonitoring extends Thread {

    private DataOutputStream dos;
    private DataInputStream dis;

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
    public FolderMonitoring(DataInputStream dis, DataOutputStream dos)
    {
        this.dos = dos;
        this.dis = dis;
    }

    @Override
    public void run() {
        try {
            //Client connect server
            dos.writeUTF("");

            //Mornitoring folder
            FileSystem fs = FileSystems.getDefault();
            WatchService ws = fs.newWatchService();
            Path pTemp = Paths.get("D:/Monitoring");
            pTemp.register(ws, new WatchEvent.Kind[]{ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE});
            while (true) {
                WatchKey k = ws.take();
                for (WatchEvent<?> e : k.pollEvents()) {
                    Object c = e.context();
                    dos.writeUTF(String.valueOf(e.kind()));
                    dos.writeUTF(String.valueOf(e.count()));
                    dos.writeUTF(String.valueOf(c));
                    System.out.printf("%s %d %s\n", e.kind(), e.count(), c);
                }
                k.reset();
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
