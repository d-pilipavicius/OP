package Windows;

import javax.swing.*;
import java.awt.*;

public class DisplaySingleton extends JFrame {
    private static final DisplaySingleton instance = new DisplaySingleton();
    private JPanel panel;
    private DisplaySingleton() {
        super();
        panel = new JPanel();
        add(panel);
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
    @Override
    public void remove(int index) {
        if(index != 0)
            super.remove(index);
    }
    @Override
    public void removeAll() {
        super.removeAll();
        add(panel);
    }
}