package LogIn;

import Windows.DisplaySingleton;

import javax.swing.*;

public class NewMain {
    public static void main(String[] args) {
        Connection connection = Connection.getInstance();
        connection.setServer("78.61.223.49", 7777);

        DisplaySingleton window = DisplaySingleton.getInstance();
        window.setNewCanvas(new LogIn());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
