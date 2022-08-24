package server;

import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Main {
    private static ServerGUI guiServer;
    private static Server server;

    public static ServerGUI getGuiServer() {
        return guiServer;
    }

    public static Server getServer() {
        return server;
    }
    public static void setServer(Server server) {
        Main.server = server;
    }

    static {
        try {
            guiServer = new ServerGUI();
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(Main.guiServer ,"Failed start GUI server");
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        server = new Server();
        guiServer.createAndShowGUI();
    }
}
