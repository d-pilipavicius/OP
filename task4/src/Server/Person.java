package Server;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private String nickname;
    private List<String> friends;
    private List<String> groups;
    public Person() {
        friends = new ArrayList<>();
        groups = new ArrayList<>();
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
    public void addGroup(String group) {
        groups.add(group);
    }
    public int getFriendCount() {
        return friends.size();
    }
    public int getChatCount() {
        return groups.size();
    }
    public void removeFriend(String friend) {
        friends.remove(friend);
    }
    public void removeGroup(String group) {
        groups.remove(group);
    }
    public List<String> getFriends() {
        return friends;
    }
    public List<String> getGroups() {
        return groups;
    }
    public boolean isFriend(String name) {
        return friends.contains(name);
    }
    public void loadPerson(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        int count = Integer.parseInt(reader.readLine());
        for(int i = 0; i < count; ++i) {
            friends.add(reader.readLine());
        }
        count = Integer.parseInt(reader.readLine());
        for(int i = 0; i < count; ++i) {
            groups.add(reader.readLine());
        }
        reader.close();
    }
    public void savePerson(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(friends.size()+"\n");
        for(String friend : friends)
            writer.write(friend+"\n");
        writer.write(groups.size()+"\n");
        for(String group : groups)
            writer.write(group+"\n");
        writer.close();
    }
}
