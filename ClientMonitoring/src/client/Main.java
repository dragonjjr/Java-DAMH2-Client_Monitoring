package client;
import java.text.ParseException;

public class Main {
    private static ClientGUI guiClient = new ClientGUI();


    public static void main(String[] args) throws ParseException {
        guiClient.createAndShowGUI();
    }
}
