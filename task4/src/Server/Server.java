package Server;

import DataStructures.Chat;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends JPanel {
    private boolean running;
    private volatile boolean ableToStop;
    private ServerSocket serverSocket;
    private List<String> connectedClients = new ArrayList<>();
    private Map<String, Socket> clients = new HashMap<>();
    private List<Chat> chatRooms;
    private Map<String, Chat> chatName = new HashMap<>();
    private Map<String, Person> getPersonsInfo = new HashMap<>();
    private List<String> availableNicknames;
    public Server() throws IOException {
        //INITIALIZING SERVER
        serverSocket = new ServerSocket(8888);
        running = true;
        ableToStop = false;
        loadAllChats();
        loadAllPeople();

        //INITIALIZING VARIABLES
        JLabel label = new JLabel("Server is running");
        JButton button = new JButton("Close server");
        Thread thread = new Thread(() -> {
            try {
                startListening();
            } catch(IOException ex) {
                System.out.println("ERROR WHILE RUNNING THREAD");
            }
        });

        //SETTING ACTION LISTENERS
        button.addActionListener(e -> {
            try {
                running = false;
                Socket socket = new Socket("localhost", 7777);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write("SERVER_EXIT\n");
                writer.flush();
                saveAllChats();
                saveAllPeople();
                while(!ableToStop) {
                    ;
                }
                for(String client : connectedClients)
                    if(clients.get(client) != null)
                        clients.get(client).close();
                serverSocket.close();
                System.exit(0);
            } catch(IOException ex) {
                System.exit(0);
            }
        });
        thread.start();

        //SETTING LAYOUTS AND ALIGNMENTS FOR ELEMENTS
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        label.setAlignmentX(CENTER_ALIGNMENT);
        button.setAlignmentX(CENTER_ALIGNMENT);


        //ADDING ELEMENTS
        add(Box.createVerticalStrut(30));
        add(label);
        add(Box.createVerticalStrut(20));
        add(button);
        add(Box.createVerticalStrut(30));
    }
    public void loadAllChats() throws IOException {
        File path = new File("CHATS");
        File[] chats = path.listFiles();
        chatRooms = new ArrayList<>();
        for(File chat : chats) {
            Chat chatFile = new Chat(chat.getName().substring(0, chat.getName().length()-4));
            chatFile.loadChat(chat.getAbsolutePath());
            if(chatFile.getPeopleCount() == 0)
                continue;
            chatRooms.add(chatFile);
            chatName.put(chatFile.getChatName(), chatFile);
        }
    }
    public void loadAllPeople() throws IOException {
        File path = new File("PEOPLE");
        File[] people = path.listFiles();
        availableNicknames = new ArrayList<>();
        for(File person : people) {
            Person user = new Person();
            user.setNickname(person.getName().substring(0, person.getName().length()-4));
            user.loadPerson(person.getAbsolutePath());
            availableNicknames.add(user.getNickname());
            getPersonsInfo.put(user.getNickname(), user);
        }
    }
    public void saveAllChats() throws IOException {
        for(Chat chatRoom : chatRooms) {
            chatRoom.saveChat("CHATS/"+chatRoom.getChatName()+".txt");
        }
    }
    public void saveAllPeople() throws IOException {
        for(String nick : availableNicknames)
            getPersonsInfo.get(nick).savePerson("PEOPLE/"+nick+".txt");
    }

    public void startListening() throws IOException {
        while(running) {
            Socket client = serverSocket.accept();
            Thread thread = new Thread(() -> {
                try {
                    handleClient(client);
                } catch(IOException ex) {
                    System.out.println("A client has disconnected in the middle of connecting.");
                }
            });
            thread.start();
        }
        ableToStop = true;
    }
    public void handleClient(Socket client) throws IOException {
        Person person = null;
        while(running) {
            String temp = receiveLine(client);
            System.out.println("Received: "+temp);
            switch(temp) {
                case "SERVER_EXIT" -> {
                    return;
                }
                case "1" -> {
                    sendLine("1", client);
                }
                case "CONNECTING" -> {
                    sendLine("", client);
                    temp = receiveLine(client);
                    System.out.println("Connected client: "+temp);
                    connectedClients.add(temp);
                    clients.put(temp, client);
                    sendLine(temp, client);
                    receiveLine(client);
                    person = getPersonsInfo.get(temp);
                    if(person == null) {
                        availableNicknames.add(temp);
                        System.out.println("This person is not in the records");
                        person = new Person();
                        person.setNickname(temp);
                        person.savePerson("PEOPLE/"+temp+".txt");
                        getPersonsInfo.put(temp, person);
                    }
                    sendLine(person.getFriendCount()+"", client);
                    receiveLine(client);
                    System.out.println("Friend count: "+person.getFriendCount());
                    List<String> tempList = person.getFriends();
                    for(String friend : tempList) {
                        sendLine(friend, client);
                        receiveLine(client);
                    }
                    tempList = person.getGroups();
                    sendLine(tempList.size()+"", client);
                    System.out.println("Groups: "+tempList.size());
                    receiveLine(client);
                    for(String chat : tempList) {
                        Chat tempChat = chatName.get(chat);
                        sendLine(chat, client);
                        receiveLine(client);
                        sendLine(tempChat.getPeopleCount()+"", client);
                        receiveLine(client);
                        List<String> tempChatList = tempChat.getPeople();
                        for(String user : tempChatList) {
                            sendLine(user, client);
                            receiveLine(client);
                        }
                        sendLine(tempChat.getMessageCount()+"", client);
                        receiveLine(client);
                        tempChatList = tempChat.getChats();
                        for(String chatMessage : tempChatList) {
                            sendLine(chatMessage, client);
                            receiveLine(client);
                        }
                    }
                    System.out.println("Client is inside.");
                }
                case "ADD_FRIEND" -> {
                    sendLine("1", client);
                    String nameOfFriend = receiveLine(client);
                    if(availableNicknames.contains(nameOfFriend)) {
                        if(person.isFriend(nameOfFriend))
                            sendLine("FRIENDS", client);
                        else {
                            sendLine("OK", client);
                            receiveLine(client);
                            sendLine(nameOfFriend, client);
                            person.addFriend(nameOfFriend);
                            person.addGroup(person.getNickname()+"&"+nameOfFriend);
                            Chat chat = new Chat(person.getNickname()+"&"+nameOfFriend);
                            Person friend = getPersonsInfo.get(nameOfFriend);
                            friend.addFriend(person.getNickname());
                            friend.addGroup(chat.getChatName());
                            chat.setFriendChat(true);
                            chat.addPerson(person.getNickname());
                            chat.addPerson(nameOfFriend);
                            chatName.put(chat.getChatName(), chat);
                            chatRooms.add(chat);
                            chat.saveChat("CHATS/"+chat.getChatName()+".txt");
                            friend.savePerson("PEOPLE/"+friend.getNickname()+".txt");
                            person.savePerson("PEOPLE/"+person.getNickname()+".txt");
                        }
                    }
                    else {
                        sendLine("NO_RECORD_MATCHED", client);
                    }
                }
                case "REMOVE_FRIEND" -> {
                    sendLine("1", client);
                    int size = Integer.parseInt(receiveLine(client));
                    sendLine("1", client);
                    for(int i = 0; i < size; ++i) {
                        String friend = receiveLine(client);
                        Chat chat;
                        if(chatName.get(person.getNickname()+"&"+friend) != null)
                            chat = chatName.get(person.getNickname()+"&"+friend);
                        else
                            chat = chatName.get(friend+"&"+person.getNickname());
                        chat.removePerson(person.getNickname());
                        chat.removePerson(friend);
                        person.removeFriend(friend);
                        Person friendsInfo = getPersonsInfo.get(friend);
                        friendsInfo.removeFriend(person.getNickname());
                        person.removeGroup(chat.getChatName());
                        friendsInfo.removeGroup(chat.getChatName());
                        chatName.remove(chat.getChatName());
                        chatRooms.remove(chat);
                        chat.saveChat("CHATS/"+chat.getChatName()+".txt");
                        person.savePerson("PEOPLE/"+person.getNickname()+".txt");
                        friendsInfo.savePerson("PEOPLE/"+friend+".txt");
                        sendLine(chat.getChatName(), client);
                    }
                }
                case "ADD_GROUP" -> {
                    sendLine("1", client);
                    String name = receiveLine(client);
                    if(chatName.get(name) == null) {
                        Chat chat = new Chat(name);
                        sendLine("OK", client);
                        int peopleCount = Integer.parseInt(receiveLine(client));
                        sendLine("1", client);
                        chat.addPerson(person.getNickname());
                        person.addGroup(chat.getChatName());
                        for(int i = 0; i < peopleCount; ++i) {
                            name = receiveLine(client);
                            chat.addPerson(name);
                            Person tempPerson = getPersonsInfo.get(name);
                            tempPerson.addGroup(chat.getChatName());
                            tempPerson.savePerson("PEOPLE/"+tempPerson.getNickname()+".txt");
                            sendLine("1", client);
                        }
                        chat.saveChat("CHATS/"+chat.getChatName()+".txt");
                        chatName.put(chat.getChatName(), chat);
                        chatRooms.add(chat);
                        person.savePerson("PEOPLE/"+person.getNickname()+".txt");
                    }
                    else {
                        sendLine("null", client);
                    }
                }
                case "REMOVE_GROUP" -> {
                    sendLine("1", client);
                    int count = Integer.parseInt(receiveLine(client));
                    sendLine("1", client);
                    String[] values = new String[count];
                    for(int i = 0; i < count; ++i) {
                        values[i] = receiveLine(client);
                        sendLine("1", client);
                    }
                    receiveLine(client);
                    List<String> tempList = new ArrayList<>();
                    for(String value : values) {
                        if(chatName.get(value).isFriendChat())
                            continue;
                        tempList.add(value);
                    }
                    sendLine(tempList.size()+"", client);
                    if(tempList.size() == 0)
                        continue;
                    receiveLine(client);
                    for(String value : tempList) {
                        sendLine(value, client);
                        receiveLine(client);
                    }
                    sendLine("1", client);
                }
                case "REMOVE_GROUP2" -> {
                    sendLine("1", client);
                    int tempSize = Integer.parseInt(receiveLine(client));
                    sendLine("1", client);
                    for(int i = 0; i < tempSize; ++i) {
                        String groupName = receiveLine(client);
                        person.removeGroup(groupName);
                        Chat chat = chatName.get(groupName);
                        chat.removePerson(person.getNickname());
                        chat.saveChat("CHATS/"+chat.getChatName()+".txt");
                        sendLine("1", client);
                    }
                    person.savePerson("PERSON/"+person.getNickname()+".txt");
                }
                case "UPDATE_CHAT" -> {
                    sendLine("1", client);
                    String tempMess = receiveLine(client);
                    Chat chat = chatName.get(tempMess);
                    sendLine("1", client);
                    tempMess = receiveLine(client);
                    sendLine("1", client);
                    int chatSize = Integer.parseInt(receiveLine(client));
                    if(!tempMess.equals("")) {
                        chat.addChat(person.getNickname()+": "+tempMess);
                    }
                    sendLine((chat.getMessageCount()-chatSize)+"", client);
                    receiveLine(client);
                    List<String> chatMes = chat.getChats();
                    for(int i = chatSize; i < chat.getMessageCount(); ++i) {
                        sendLine(chatMes.get(i), client);
                        receiveLine(client);
                    }
                    chat.saveChat("CHATS/"+chat.getChatName()+".txt");
                    sendLine("1", client);
                }
                case "LOGGING_OFF" -> {
                    clients.remove(person.getNickname());
                    person.savePerson("PEOPLE/"+person.getNickname()+".txt");
                    connectedClients.remove(person.getNickname());
                    client.close();
                    return;
                }
            }
        }
    }
    public void sendLine(String line, Socket client) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        writer.write(line+"\n");
        writer.flush();
    }
    public String receiveLine(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        return reader.readLine();
    }
}
