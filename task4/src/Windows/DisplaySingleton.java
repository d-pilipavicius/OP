package Windows;

import javax.swing.*;
import java.awt.*;

public class DisplaySingleton extends JFrame {
    private static final DisplaySingleton instance = new DisplaySingleton();
    private JPanel panel;
    private DisplaySingleton() {
        super();
        setResizable(false);
        panel = new JPanel();
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static DisplaySingleton getInstance() {
        return instance;
    }
    public JPanel getCanvas(){
        return panel;
    }
    @Override
    public void remove(Component c) {
        if(c != panel){
            super.remove(c);
        }
    }
    public void setNewCanvas(JPanel panel) {
        this.panel.removeAll();
        this.panel.setLayout(new GridLayout(0,1));
        this.panel.add(panel);
        this.panel.repaint();
    }
}