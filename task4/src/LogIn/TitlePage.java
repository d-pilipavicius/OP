package LogIn;

import DataStructures.Chat;
import Windows.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TitlePage extends JPanel {
    public TitlePage() {
        //Setting up the page
        Connection connection = Connection.getInstance();
        JPanel chatButtons = new JPanel(new GridLayout(0,1));
        JScrollPane pane = new JScrollPane(chatButtons);
        JPanel choiceButtons = new JPanel();
        JButton addFriend = new JButton("Add friend");
        JButton removeFriend = new JButton("Remove friend");
        JButton addGroup = new JButton("Add group");
        JButton removeGroup = new JButton("Remove group");
        JButton logOff = new JButton("Disconnect");

        //Layout
        pane.setPreferredSize(new Dimension(200, 400));
        choiceButtons.setLayout(new BoxLayout(choiceButtons, BoxLayout.Y_AXIS));
        addFriend.setAlignmentX(CENTER_ALIGNMENT);
        removeFriend.setAlignmentX(CENTER_ALIGNMENT);
        addGroup.setAlignmentX(CENTER_ALIGNMENT);
        removeGroup.setAlignmentX(CENTER_ALIGNMENT);
        logOff.setAlignmentX(CENTER_ALIGNMENT);

        //Building left side
        List<Chat> chatList = connection.getChats();
        for(Chat chat : chatList) {
            JButton openChat = new JButton(chat.getChatName());
            openChat.addActionListener(e -> {
                DisplaySingleton singleton = DisplaySingleton.getInstance();
                singleton.setSize(1000, 800);
                singleton.setNewCanvas(new ChatPanel(chat.getChatName(), 1000, 800, chat.getChats(), new BackButtonPanel(this, 600, 500)));
            });
            chatButtons.add(openChat);
        }

        //Building right side
        addFriend.setPreferredSize(new Dimension(200, 40));
        removeFriend.setPreferredSize(new Dimension(200, 40));
        addGroup.setPreferredSize(new Dimension(200, 40));
        removeGroup.setPreferredSize(new Dimension(200, 40));

        choiceButtons.add(addFriend); choiceButtons.add(Box.createVerticalStrut(20));
        choiceButtons.add(removeFriend); choiceButtons.add(Box.createVerticalStrut(20));
        choiceButtons.add(addGroup); choiceButtons.add(Box.createVerticalStrut(20));
        choiceButtons.add(removeGroup); choiceButtons.add(Box.createVerticalStrut(100));
        choiceButtons.add(logOff);

        addFriend.addActionListener(e -> {
            DisplaySingleton singleton = DisplaySingleton.getInstance();
            singleton.dispose();
            JTypePane typePane = new JTypePane("Write the nickname of a person you'd like to add.") {
                @Override
                public void acceptAction() {
                    if(getTextFromBox().isEmpty() || getTextFromBox().indexOf(' ') != -1) {
                        JPopup popup = new JPopup("The nickname you entered is illegal.") {
                            public void acceptAction() { reappear(); }
                        };
                        return;
                    }
                    if(getTextFromBox().equals(connection.getNickname())) {
                        JPopup popup = new JPopup("You cannot add yourself as a friend.") {
                            public void acceptAction() {
                                reappear();
                            }
                        };
                        return;
                    }
                    try {
                        connection.sendLine("ADD_FRIEND");
                        connection.receiveLine();
                        connection.sendLine(getTextFromBox());
                        String temp = connection.receiveLine();
                        if(temp.equals("OK")) {
                            connection.sendLine("1");
                            String name = connection.receiveLine();
                            connection.addFriend(name);
                            Chat chat = new Chat(connection.getNickname()+"&"+name);
                            chat.setFriendChat(true);
                            connection.addChat(chat);
                            JPopup popup = new JPopup(name+" added as a friend!", "Great!", "New friend added") {
                                public void acceptAction() {
                                    singleton.setNewCanvas(new TitlePage());
                                    singleton.setVisible(true);
                                }
                            };
                        }
                        else if(temp.equals("FRIENDS")) {
                            JPopup popup = new JPopup(getTextFromBox()+" is already in your friends list.") {
                                public void acceptAction() { reappear(); }
                            };
                        }
                        else {
                            JPopup popup = new JPopup("The provided nickname does not exist.") {
                                public void acceptAction() {
                                    reappear();
                                }
                            };
                        }
                    } catch(IOException ex) {
                        try {
                            connection.close();
                            DisplaySingleton.getInstance().setNewCanvas(new LogIn());
                        } catch(IOException exc) {}
                    }
                }
                public void declineAction() {
                    singleton.setNewCanvas(new TitlePage());
                    singleton.setVisible(true);
                }
            };
        });
        removeFriend.addActionListener(e -> {
            String[] friends = connection.getFriendsArr();
            if(friends.length == 0)
                return;
            MultipleChoice multipleChoice = new MultipleChoice(friends, "Remove friends", "Choose the friends you'd like to remove") {
                @Override
                public void submittedRun() {
                    List<String> temp = getSelectedData();
                    if(temp.size() == 0) {
                        return;
                    }
                    try {
                        connection.sendLine("REMOVE_FRIEND");
                        connection.receiveLine();
                        connection.sendLine(temp.size()+"");
                        connection.receiveLine();
                        for(String friend : temp) {
                            connection.sendLine(friend);
                            String tempS = connection.receiveLine();
                            connection.removeFriend(friend);
                            connection.removeChat(tempS);
                        }
                        DisplaySingleton singleton = DisplaySingleton.getInstance();
                        singleton.dispose();
                        singleton.setNewCanvas(new TitlePage());
                        singleton.setVisible(true);
                    } catch(Exception ex) {
                        try {
                            connection.close();
                            DisplaySingleton.getInstance().setNewCanvas(new LogIn());
                        } catch(IOException exc) {}
                    }
                }
            };
        });
        addGroup.addActionListener(e -> {
            String[] friends = connection.getFriendsArr();
            JTypePane typePane = new JTypePane("Choose the group name.") {
                @Override
                public void acceptAction() {
                    if(getTextFromBox().trim().isEmpty()) {
                        JPopup popup = new JPopup("The name you entered is illegal.") {
                            public void acceptAction() { reappear(); }
                        };
                        return;
                    }
                    MultipleChoice multipleChoice = new MultipleChoice(friends, "Create a new group", "Choose the people you'd like to insert to the group.") {
                        @Override
                        public void submittedRun() {
                            List<String> temp = getSelectedData();
                            if(temp.size() == 0) {
                                return;
                            }
                            try {
                                connection.sendLine("ADD_GROUP");
                                connection.receiveLine();
                                connection.sendLine(getTextFromBox());
                                if(connection.receiveLine().equals("OK")) {
                                    connection.sendLine(temp.size()+"");
                                    connection.receiveLine();
                                    for(String people : temp) {
                                        connection.sendLine(people);
                                        connection.receiveLine();
                                    }
                                    Chat chat = new Chat(getTextFromBox());
                                    chat.addPerson(connection.getNickname());
                                    for(String people : temp) {
                                        chat.addPerson(people);
                                    }
                                    connection.addChat(chat);
                                    DisplaySingleton singleton = DisplaySingleton.getInstance();
                                    singleton.dispose();
                                    singleton.setNewCanvas(new TitlePage());
                                    singleton.setVisible(true);
                                }
                                else {
                                    JPopup popup = new JPopup("The group name you entered already exists.") {
                                        public void acceptAction() {}
                                    };
                                }

                            } catch(IOException ex) {
                                try {
                                    connection.close();
                                    DisplaySingleton.getInstance().setNewCanvas(new LogIn());
                                } catch(IOException exc) {}
                            }

                        }
                    };
                }
                public void declineAction() {}
            };

        });
        removeGroup.addActionListener(e -> {
            if(connection.getChatCount() == 0)
                return;
            try {
                connection.sendLine("REMOVE_GROUP");
                connection.receiveLine();
                int chatCount = connection.getChatCount();
                connection.sendLine(chatCount+"");
                connection.receiveLine();
                String[] chats = connection.getGroupChatArr();
                for(String chat : chats) {
                    connection.sendLine(chat);
                    connection.receiveLine();
                }
                connection.sendLine("1");
                chatCount = Integer.parseInt(connection.receiveLine());
                if(chatCount == 0)
                    return;
                chats = new String[chatCount];
                connection.sendLine("1");
                for(int i = 0; i < chatCount; ++i) {
                    chats[i] = connection.receiveLine();
                    connection.sendLine("1");
                }
                connection.receiveLine();
                MultipleChoice multipleChoice = new MultipleChoice(chats, "Remove groups", "Choose the groups you'd like to remove.") {
                    @Override
                    public void submittedRun() {
                        List<String> chosen = getSelectedData();
                        try {
                            connection.sendLine("REMOVE_GROUP2");
                            connection.receiveLine();
                            connection.sendLine(chosen.size()+"");
                            connection.receiveLine();
                            for(String element : chosen) {
                                connection.sendLine(element);
                                connection.removeChat(element);
                                connection.receiveLine();
                            }
                            DisplaySingleton singleton = DisplaySingleton.getInstance();
                            singleton.dispose();
                            singleton.setNewCanvas(new TitlePage());
                            singleton.setVisible(true);
                        }
                        catch(IOException ex) {
                            try {
                                connection.close();
                                DisplaySingleton.getInstance().setNewCanvas(new LogIn());
                            } catch(IOException exc) {}
                        }
                    }
                };
            } catch(IOException ex) {
                try {
                    connection.close();
                    DisplaySingleton.getInstance().setNewCanvas(new LogIn());
                } catch(IOException exc) {}
            }
        });
        logOff.addActionListener(e -> {
            JOptionPopup popup = new JOptionPopup("Are you sure you want to exit?") {
                @Override
                public void acceptAction() {
                    try {
                        connection.sendLine("LOGGING_OFF");
                        connection.close();
                        DisplaySingleton singleton = DisplaySingleton.getInstance();
                        singleton.setNewCanvas(new LogIn());
                    } catch(Exception ex) {
                        System.out.println("ERROR WHILE EXITING.");
                    }
                }
                public void declineAction() {}
            };
        });

        //Adding the components
        add(pane);
        add(Box.createHorizontalStrut(150));
        add(choiceButtons);
    }
}
