package Windows;

import javax.crypto.spec.PSource;
import javax.swing.*;
import java.awt.*;

public abstract class JPopup {
    public JPopup(String message) {
        setupJ(message, "OK", "Warning!");
    }
    public JPopup(String message, String buttonText) {
        setupJ(message, buttonText, "Warning!");
    }
    public JPopup(String message, String buttonText, String windowText) {
        setupJ(message, buttonText, windowText);
    }

    private void setupJ(String message, String buttonText, String info) {
        JFrame frame = new JFrame(info);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("<html>"+message+"</html>");
        JButton button = new JButton(buttonText);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);

        button.addActionListener(e -> {
            acceptAction();
            frame.dispose();
        });

        panel.add(Box.createVerticalStrut(30));
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        panel.add(button);
        frame.add(panel);

        frame.setResizable(false);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
    public abstract void acceptAction();
}
