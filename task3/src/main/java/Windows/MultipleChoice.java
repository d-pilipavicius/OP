package Windows;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class MultipleChoice extends JFrame {
    private JPanel panel;
    private JLabel label;
    private boolean submitted = false;
    private JCheckBox[] checkBoxes;

    public MultipleChoice(String[] data, String frameName, String panelTitle) {
        super(frameName);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        label = new JLabel(panelTitle);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        checkBoxes = new JCheckBox[data.length];

        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        int longestName = 0;
        for(int i = 0; i < data.length; ++i) {
            checkBoxes[i] = new JCheckBox(data[i]);
            checkPanel.add(checkBoxes[i]);
            if(data[i].length() > longestName)
                longestName = data[i].length();
        }
        JScrollPane pane = new JScrollPane(checkPanel);

        JPanel twoButtons = new JPanel();
        JButton button1 = new JButton("Save");
        JButton button2 = new JButton("Cancel");
        twoButtons.add(button1); twoButtons.add(button2);
        button1.addActionListener(e -> {
            dispose();
            submitted = true;
            submittedRun();
        });
        button2.addActionListener(e -> {
            dispose();
        });

        panel.add(label); panel.add(pane); panel.add(twoButtons);

        add(panel);
        setSize(300, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public java.util.List<String> getSelectedData() {
        if(!submitted)
            return null;
        List<String> list = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                list.add(checkBox.getText());
            }
        }
        return list;
    }
    public abstract void submittedRun();
}
