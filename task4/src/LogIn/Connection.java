package LogIn;

import DataStructures.Chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connection {
    private String ip;
    private int port;
    private Socket socket;
    private String nickname;
    private BufferedReader reader;
    private BufferedWriter writer;
    private List<String> friends = new ArrayList<>();
    private List<String> chatRooms = new ArrayList<>();
    private Map<String, Chat> chatName = new HashMap<>();
    private final static Connection instance = new Connection();
    private Connection() {
    }
    public static Connection getInstance() {
        return instance;
    }
    public void setServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    public void connectToServer() throws IOException {
        socket = new Socket(ip, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    public boolean isServerOnline() {
        try {
            sendLine("1");
            receiveLine();
        } catch(IOException ex) {
            chatRooms.clear();
            chatName.clear();
            return false;
        }
        return true;
    }
    public void sendLine(String text) throws IOException {
        writer.write(text+"\n");
        writer.flush();
    }
    public String receiveLine() throws IOException {
        return reader.readLine();
    }
    public Chat getChat(String roomName) {
        return chatName.get(roomName);
    }
    public void addChat(Chat chat) {
        chatRooms.add(chat.getChatName());
        chatName.put(chat.getChatName(), chat);
    }
    public int getChatCount() {
        return chatRooms.size();
    }
    public List<Chat> getChats() {
        List<Chat> chats = new ArrayList<>();
        for(String room : chatRooms)
            chats.add(chatName.get(room));
        return chats;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }
    public void addFriend(String friend) {
        friends.add(friend);
    }
    public void removeFriend(String friend) {
        friends.remove(friend);
    }
    public void removeChat(String nickname) {
        chatName.get(nickname).removePerson(getNickname());
        chatRooms.remove(nickname);
        chatName.remove(nickname);
    }
    public String[] getFriendsArr() {
        String[] friendTemp = new String[friends.size()];
        for(int i = 0; i < friends.size(); ++i) {
            friendTemp[i] = friends.get(i);
        }
        return friendTemp;
    }
    public String[] getGroupChatArr() {
        String[] chatTemp = new String[getChatCount()];
        for(int i = 0; i < getChatCount(); ++i) {
            chatTemp[i] = chatRooms.get(i);
        }
        return chatTemp;
    }
    public void close() throws IOException {
        nickname = null;
        reader = null;
        writer = null;
        friends.clear();
        chatRooms.clear();
        chatName.clear();
        socket.close();
    }
}
