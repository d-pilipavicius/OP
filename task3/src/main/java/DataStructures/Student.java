package DataStructures;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Student {
    private String name;
    private String group;
    private List<String> attendance;

    public Student(String name, String group) {
        this.name = name;
        this.group = group;
        attendance = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public String getGroup() {
        return group;
    }
    public String getDay(int index) {
        return attendance.get(index);
    }
    public int getDayCount() {
        return attendance.size();
    }
    public void removeDay(int index) {
        attendance.remove(index);
    }
    public void setGroup(String group) { this.group = group; }
    public int locateDay(int year, int month, int day) {
        String dayString = year+"-";
        dayString += ((month > 9) ? "" : "0")+month+"-";
        dayString += ((day > 9) ? "" : "0")+day;
        for(int i = 0; i < attendance.size(); ++i) {
            String temp = attendance.get(i);
            if(dayString.equals(temp))
                return i;
        }
        return -1;
    }
    public void includeDay(String day) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Date temp = format.parse(day);
        attendance.add(day);
    }
    public void includeDay(int year, int month, int day) throws ParseException {
        String date = year+"-";
        date += ((month > 9) ? "" : "0")+month+"-";
        date += ((day > 9) ? "" : "0")+day;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Date temp = format.parse(date);
        attendance.add(date);
    }
}