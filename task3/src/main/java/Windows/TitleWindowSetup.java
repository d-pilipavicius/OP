package Windows;

import javax.swing.*;
import java.awt.*;

public class TitleWindowSetup {
    public static void titleWindow() {
        DisplaySingleton window = DisplaySingleton.getInstance();
        JPanel panel = window.getCanvas();
        panel.setLayout(new FlowLayout());
        panel.removeAll();
        JButton button1 = new JButton("Edit students/groups");
        button1.addActionListener(e -> {
            IncludeStudents.loadEditStudents();
        });

        JButton button2 = new JButton("Edit students attendance");
        button2.addActionListener(e -> {
            AttendanceWindow.loadAttendance();
        });

        JButton button3 = new JButton("Save/Import");
        button3.addActionListener(e -> {
            SaveWindow.loadSaveWindow();
        });

        panel.add(button1); panel.add(button2); panel.add(button3);
        window.setSize(500, 75);
        window.setTitle("TITLE");
        window.setResizable(false);
    }

    public static JPanel backButton() {
        JButton button = new JButton("<-");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        button.setBackground(new Color(0xFF5454));
        button.addActionListener(e -> {
            TitleWindowSetup.titleWindow();
        });
        panel.add(button);
        return panel;
    }
}
