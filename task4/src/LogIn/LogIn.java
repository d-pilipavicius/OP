package LogIn;

import DataStructures.Chat;
import Windows.DisplaySingleton;
import Windows.JPopup;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LogIn extends JPanel {
    public LogIn() {
        DisplaySingleton window = DisplaySingleton.getInstance();
        JLabel temp0 = new JLabel("CHAT.java");
        JLabel temp1 = new JLabel("Username");
        JPanel userPanel = new JPanel();
        JButton logInButton = new JButton("Log in");

        window.setSize(400, 300);
        JTextField username = new JTextField();
        username.setPreferredSize(new Dimension(150, 30));

        userPanel.add(temp1); userPanel.add(username);
        temp0.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        temp0.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        logInButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        logInButton.addActionListener(e -> {
            //NOTHING WAS ENTERED IN THE USERNAME AREA
            if(username.getText().isEmpty() || username.getText().indexOf(' ') != -1) {
                JPopup popup = new JPopup("Username contains spaces or is empty.") {
                    public void acceptAction() {}
                };
                return;
            }

            //CONNECTION TO SERVER
            try {
                Connection connection = Connection.getInstance();
                connection.connectToServer();
                //Connecting
                connection.sendLine("CONNECTING");
                connection.receiveLine();
                //Sending username
                connection.sendLine(username.getText());
                //Getting username
                connection.setNickname(connection.receiveLine());
                connection.sendLine("1");
                //Getting friends
                int friendCount = Integer.parseInt(connection.receiveLine());
                connection.sendLine("2");
                for(int i = 0; i < friendCount; ++i) {
                    connection.addFriend(connection.receiveLine());
                    connection.sendLine("3");
                }
                //Getting chats
                int chatCount = Integer.parseInt(connection.receiveLine());
                connection.sendLine("4");
                for(int i = 0; i < chatCount; ++i) {
                    Chat chat = new Chat(connection.receiveLine());
                    connection.sendLine("5");
                    int count = Integer.parseInt(connection.receiveLine());
                    connection.sendLine("6");
                    for(int j = 0; j < count; ++j) {
                        chat.addPerson(connection.receiveLine());
                        connection.sendLine("7");
                    }
                    count = Integer.parseInt(connection.receiveLine());
                    connection.sendLine("8");
                    for(int j = 0; j < count; ++j) {
                        chat.addChat(connection.receiveLine());
                        connection.sendLine("9");
                    }
                    connection.addChat(chat);
                }

            } catch(IOException ex) {
                JPopup popup = new JPopup("Server is offline") {
                    public void acceptAction() {}
                };
                return;
            }
            DisplaySingleton singleton = DisplaySingleton.getInstance();
            singleton.setNewCanvas(new TitlePage());
            singleton.setSize(600, 500);
            Connection connection = Connection.getInstance();
            System.out.println(connection.getChatCount());
        });


        add(temp0);
        add(Box.createVerticalStrut(20));
        add(userPanel);
        add(Box.createVerticalStrut(20));
        add(logInButton);
        add(Box.createVerticalStrut(60));
    }
}
