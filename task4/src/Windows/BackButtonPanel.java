package Windows;

import javax.swing.*;
import java.awt.*;

public class BackButtonPanel extends JPanel {
    private boolean wasPressed = false;
    private JButton button = new JButton("<-");
    private JPanel returnPanel;
    public BackButtonPanel(JPanel returnPanel, int originalW, int originalH) {
        button.setBackground(new Color(232, 17, 35));
        this.returnPanel = returnPanel;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(button);
        button.addActionListener(e -> {
            wasPressed = true;
            activateButton(originalW, originalH);
        });
    }
    public void activateButton(int w, int h) {
        DisplaySingleton window = DisplaySingleton.getInstance();
        window.setNewCanvas(returnPanel);
        window.setSize(w, h);
        window.repaint();
    }
    public boolean wasPressed() {
        return wasPressed;
    }
}
