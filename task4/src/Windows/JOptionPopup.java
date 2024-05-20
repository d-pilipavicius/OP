package Windows;

import javax.swing.*;

public abstract class JOptionPopup {
    public JOptionPopup(String message) {
        setupJ(message, "Accept", "Cancel", "Warning!");
    }
    public JOptionPopup(String message, String buttonYes, String buttonNo) {
        setupJ(message, buttonYes, buttonNo, "Warning!");
    }
    public JOptionPopup(String message, String buttonYes, String buttonNo, String frameInfo) {
        setupJ(message, buttonYes, buttonNo, frameInfo);
    }
    private void setupJ(String message, String buttonText1, String buttonText2, String info) {
        JFrame frame = new JFrame(info);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("<html>"+message+"</html>");
        JPanel buttonPanel = new JPanel();
        JButton button1 = new JButton(buttonText1);
        JButton button2 = new JButton(buttonText2);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);

        button1.addActionListener(e -> {
            acceptAction();
            frame.dispose();
        });
        button2.addActionListener(e -> {
            declineAction();
            frame.dispose();
        });

        panel.add(Box.createVerticalStrut(30));
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        buttonPanel.add(button1); buttonPanel.add(button2);
        panel.add(buttonPanel);
        frame.add(panel);

        frame.setResizable(false);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
    public abstract void acceptAction();
    public abstract void declineAction();
}
