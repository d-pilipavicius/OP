package Windows;

import DataStructures.Chat;
import LogIn.Connection;
import LogIn.LogIn;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class ChatPanel extends JPanel {
    private int winW, winH;
    private int click = 0;
    private int lastChats;
    private JTextField textField;
    private Color messageColor;
    private java.util.List<String> chats;
    private String chat;
    private JPanel backButton;
    private JPanel connector = new JPanel();
    private boolean threadRun = false;
    public ChatPanel(String chat, int winW, int winH, java.util.List<String> chatMessages, BackButtonPanel backButton) {
        super();
        this.backButton = backButton;
        this.winH = winH;
        this.winW = winW;
        lastChats = 0;
        chats = chatMessages;
        textField = new JTextField();
        messageColor = new Color(0x71FF73);
        this.chat = chat;
        for(String message : chats) {
            connector.add(chatMessage(message,true));
        }
        Thread thread = new Thread(() -> {
            while(!backButton.wasPressed()) {
                threadRun = true;
                checkForMessages("");
                threadRun = false;
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    break;
                }
            }
        });
        thread.start();
        loadWindow(backButton);
    }
    public void loadWindow(JPanel backButton) {
        removeAll();

        JPanel boxAndField = new JPanel();
        JScrollPane chatBox = new JScrollPane();
        JPanel textAndButton = new JPanel();
        JButton sendButton = new JButton("Send");

        sendButton.setPreferredSize(new Dimension(winH*2/15, 30));
        textAndButton.setLayout(new BoxLayout(textAndButton, BoxLayout.X_AXIS));
        boxAndField.setLayout(new BoxLayout(boxAndField, BoxLayout.Y_AXIS));
        setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
        chatBox.setPreferredSize(new Dimension(winH*2/3, winW*2/3));
        connector.setLayout(new BoxLayout(connector, BoxLayout.Y_AXIS));
        chatBox.setViewportView(connector);
        textField.setPreferredSize(new Dimension(winH*2/3-winH*2/15, 30));
        textAndButton.add(textField); textAndButton.add(sendButton);
        boxAndField.add(chatBox); boxAndField.add(textAndButton);
        JScrollBar verticalScrollBar = chatBox.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        sendButton.addActionListener(e -> {
            if(!textField.getText().trim().isEmpty() && !threadRun) {
                click++;
                checkForMessages(textField.getText());
                if(click%2==1)textField.setText("");
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(backButton);
        add(boxAndField);
    }
    public void setMessageColor(Color color) {
        messageColor = color;
    }
    public void loadChats() {
    }
    public JPanel chatMessage(String text, boolean leftAlignment) {
        JPanel textPanel = new JPanel();
        JLabel label = new JLabel(text);
        JPanel bubble = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bubble.add(label);
        bubble.setBackground(messageColor);
        if(leftAlignment)
            textPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        else
            textPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        textPanel.add(bubble);
        return textPanel;
    }

    public synchronized void checkForMessages(String sentMessage) {
        Connection connection = Connection.getInstance();
        try {
            connection.sendLine("UPDATE_CHAT");
            connection.receiveLine();
            connection.sendLine(chat);
            connection.receiveLine();
            Chat chat1 = connection.getChat(chat);
            connection.sendLine(sentMessage);
            connection.receiveLine();
            connection.sendLine(chat1.getChats().size()+"");
            int missingChats = Integer.parseInt(connection.receiveLine());
            connection.sendLine("1");
            for(int i = 0; i < missingChats; ++i) {
                String temp = connection.receiveLine();
                chat1.addChat(temp);
                chats.add(temp);
                connection.sendLine("1");
                connector.add(chatMessage(temp, true));
            }
            lastChats = chats.size();
            connection.receiveLine();
        } catch(IOException ex) {
            try {
                connection.close();
                DisplaySingleton.getInstance().setNewCanvas(new LogIn());
            } catch(IOException exc) {}
        }
        DisplaySingleton singleton = DisplaySingleton.getInstance();
        singleton.setSize(winW-1, winH-1);
        singleton.setSize(winW, winH);
    }
}
