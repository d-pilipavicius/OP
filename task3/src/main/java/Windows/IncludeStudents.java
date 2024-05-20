package Windows;

import DataStructures.*;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class IncludeStudents {
    private static boolean updatedStudents;
    private static boolean updatedGroups;
    public static void loadEditStudents() {
        updatedStudents = false;
        updatedGroups = false;
        DisplaySingleton window = DisplaySingleton.getInstance();
        JPanel panel = window.getCanvas();
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(TitleWindowSetup.backButton());
        panel.add(getInsertPanel());
        panel.add(getStudentGroup());
        panel.add(getRemovePanel());
        window.setSize(500, 250);
    }
    public static JPanel getInsertPanel() {
        DataHolder holder = DataHolder.getGlobalHolder();
        JPanel panel = new JPanel();

        JLabel text1 = new JLabel("Add a new student:");
        JLabel text2 = new JLabel("Add a new group: ");

        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        field1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!field1.getText().trim().isEmpty()) {
                        Student student = new Student(field1.getText().trim(), "");
                        holder.addStudent(student);
                        field1.setText("");
                        updatedStudents = false;
                    }
                }
            }
        });
        field2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!field2.getText().trim().isEmpty()) {
                        holder.addGroup(field2.getText().trim());
                        field2.setText("");
                        updatedGroups = false;
                    }
                }
            }
        });

        field1.setPreferredSize(new Dimension(200, 30));
        field2.setPreferredSize(new Dimension(200, 30));

        JButton button1 = new JButton("Add student");
        JButton button2 = new JButton("Add group");
        button1.addActionListener(e -> {
            if(!field1.getText().trim().isEmpty()) {
                Student student = new Student(field1.getText().trim(), "");
                holder.addStudent(student);
                field1.setText("");
                updatedStudents = false;
            }
        });
        button2.addActionListener(e -> {
            if(!field2.getText().trim().isEmpty()) {
                holder.addGroup(field2.getText().trim());
                field2.setText("");
                updatedGroups = false;
            }
        });

        panel.add(text1); panel.add(field1); panel.add(button1);
        panel.add(text2); panel.add(field2); panel.add(button2);
        panel.setPreferredSize(new Dimension(400, 300));
        return panel;
    }

    public static JPanel getStudentGroup() {
        DataHolder holder = DataHolder.getGlobalHolder();
        JPanel panel = new JPanel();

        JComboBox<String> studentBox = new JComboBox<>(new String[]{"-"});
        studentBox.setPreferredSize(new Dimension(100, 30));
        JLabel label2 = new JLabel(" ASSIGN TO GROUP ");
        JComboBox<String> groupBox = new JComboBox<>(new String[]{"-"});
        groupBox.setPreferredSize(new Dimension(100, 30));

        studentBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if(updatedStudents)
                    return;
                studentBox.removeAllItems();
                studentBox.addItem("-");
                Student[] students = holder.getStudents();
                for(Student student : students) {
                    studentBox.addItem(student.getName());
                }
                updatedStudents = true;
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        studentBox.addActionListener(e -> {
            if(studentBox.getItemCount() <= 1 || studentBox.getSelectedIndex() == 0) {
                groupBox.setSelectedIndex(0);
                return;
            }
            Student stud = holder.getStudentByName((String) studentBox.getSelectedItem());
            String group = stud.getGroup();
            if(!group.trim().isEmpty()) {
                for(int i = 1; i < groupBox.getItemCount(); ++i) {
                    if(groupBox.getItemAt(i).equals(group)) {
                        groupBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
        groupBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if(updatedGroups)
                    return;
                groupBox.removeAllItems();
                groupBox.addItem("-");
                String[] groups = holder.getGroupNames();
                for(String group : groups) {
                    groupBox.addItem(group);
                }
                updatedGroups = true;
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        JButton setButton = new JButton("Set group");
        setButton.addActionListener(e -> {
            if(studentBox.getSelectedIndex() != 0 && groupBox.getSelectedIndex() != 0) {
                Student student = holder.getStudentByName((String) studentBox.getSelectedItem());
                student.setGroup((String) groupBox.getSelectedItem());
                studentBox.setSelectedIndex(0); groupBox.setSelectedIndex(0);
            }
        });

        panel.add(studentBox); panel.add(label2); panel.add(groupBox); panel.add(setButton);
        panel.setPreferredSize(new Dimension(400, 50));
        return panel;
    }

    public static JPanel getRemovePanel() {
        DataHolder holder = DataHolder.getGlobalHolder();
        JPanel panel = new JPanel();
        JButton button1 = new JButton("Remove students");
        JButton button2 = new JButton("Remove groups");
        button1.addActionListener(e -> {
            MultipleChoice choiceWindow = new MultipleChoice(holder.getStudentNames(), "Student removal", "Choose the students you wish to remove") {
                @Override
                public void submittedRun() {
                    List<String> list = getSelectedData();
                    for(String name : list) {
                        holder.removeStudent(name);
                    }
                    updatedStudents = false;
                }
            };
        });
        button2.addActionListener(e -> {
            MultipleChoice choiceWindow = new MultipleChoice(holder.getGroupNames(), "Group removal", "Choose the groups you wish to remove") {
                @Override
                public void submittedRun() {
                    List<String> list = getSelectedData();
                    for(String name : list) {
                        holder.removeGroup(name);
                    }
                    updatedGroups = false;
                }
            };
        });
        panel.add(button1);
        panel.add(button2);
        panel.setPreferredSize(new Dimension(150, 150));
        return panel;
    }
}
