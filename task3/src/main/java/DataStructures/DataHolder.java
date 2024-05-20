package DataStructures;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    private List<Student> students;
    private List<String> groups;
    private static DataHolder globalHolder;
    public DataHolder() {
        students = new ArrayList<>();
        groups = new ArrayList<>();
    }
    public static void resetGlobal() {
        globalHolder = new DataHolder();
    }
    public static DataHolder getGlobalHolder() {
        if(globalHolder == null)
            resetGlobal();
        return globalHolder;
    }

    public void addStudent(Student student) {
        for(Student stud : students) {
            if(stud == student)
                return;
        }
        students.add(student);
    }
    public void removeStudent(Student student) {
        for(int i = 0; i < students.size(); ++i) {
            if(students.get(i) == student) {
                students.remove(i);
                return;
            }
        }
    }
    public void removeStudent(String name) {
        for(int i = 0; i < students.size(); ++i) {
            if(students.get(i).getName().equals(name)) {
                students.remove(i);
                removeStudent(name);
                return;
            }
        }
    }
    public void addGroup(String groupName) {
        for(String name : groups) {
            if(name.equals(groupName))
                return;
        }
        groups.add(groupName);
    }
    public void removeGroup(String groupName) {
        for(int i = 0; i < groups.size(); ++i) {
            if(groups.get(i).equals(groupName)) {
                groups.remove(i);
                for(Student stud : students) {
                    if(stud.getGroup().equals(groupName)) {
                        stud.setGroup("");
                    }
                }
                return;
            }
        }
    }
    public Student[] getStudents() {
        Student[] stud = new Student[students.size()];
        for(int i = 0; i < students.size(); ++i) {
            stud[i] = students.get(i);
        }
        return stud;
    }
    public Student getStudentByName(String name) {
        for(Student student : students) {
            if(student.getName().equals(name))
                return student;
        }
        return null;
    }
    public String[] getStudentNames() {
        String[] names = new String[students.size()];
        for(int i = 0; i < students.size(); ++i) {
            names[i] = students.get(i).getName();
        }
        return names;
    }
    public String[] getGroupNames() {
        String[] names = new String[groups.size()];
        for(int i = 0; i < groups.size(); ++i) {
            names[i] = groups.get(i);
        }
        return names;
    }
}
