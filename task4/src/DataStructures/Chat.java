package DataStructures;

import javax.print.DocFlavor;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private boolean friendChat;
    private String chatName;
    private List<String> peopleInChat = new ArrayList<>();
    private List<String> chats = new ArrayList<>();
    public Chat(String name) {
        chatName = name;
        friendChat = false;
    }
    public String getChatName() {
        return chatName;
    }
    public List<String> getChats() {
        return chats;
    }
    public void addChat(String message) {
        chats.add(message);
    }
    public void addPerson(String name) {
        peopleInChat.add(name);
    }
    public void removePerson(String name) {
        peopleInChat.remove(name);
    }
    public int getMessageCount() {
        return chats.size();
    }
    public int getPeopleCount() {
        return peopleInChat.size();
    }
    public void setFriendChat(boolean friendChat) {
        this.friendChat = friendChat;
    }
    public List<String> getPeople() {
        return peopleInChat;
    }
    public boolean isFriendChat() {
        return friendChat;
    }
    public void saveChat(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write((friendChat) ? "1\n" : "0\n");
        writer.write(getPeopleCount()+"\n");
        for(int i = 0; i < getPeopleCount(); ++i) {
            writer.write(peopleInChat.get(i)+"\n");
        }
        writer.write(getMessageCount()+"\n");
        for(int i = 0; i < getMessageCount(); ++i) {
            writer.write(chats.get(i)+"\n");
        }
        writer.close();
    }
    public void loadChat(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        friendChat = reader.readLine().equals("1");
        peopleInChat = new ArrayList<>();
        chats = new ArrayList<>();
        int count = Integer.parseInt(reader.readLine());
        if(count == 0) {
            reader.close();
            Path targetPath = Path.of("TRASHED_CHATS/"+chatName+".txt");
            Path sourcePath = Path.of(path);
            try {
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch(IOException ex) {
                System.out.println("Error while removing the non used chat.");
            }
            return;
        }
        for(int i = 0; i < count; ++i) {
            peopleInChat.add(reader.readLine());
        }
        count = Integer.parseInt(reader.readLine());
        for(int i = 0; i < count; ++i) {
            chats.add(reader.readLine());
        }
        reader.close();
    }
}
