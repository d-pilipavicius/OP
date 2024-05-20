package Server;

import Windows.DisplaySingleton;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        DisplaySingleton singleton = DisplaySingleton.getInstance();
        singleton.getCanvas().add(server);
        singleton.setSize(300, 300);
        singleton.setVisible(true);
    }
}
