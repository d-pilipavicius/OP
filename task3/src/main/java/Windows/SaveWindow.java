package Windows;

import DataStructures.*;
import IOHandler.CSVHandler;
import IOHandler.ExcelHandler;

import javax.swing.*;
import java.awt.*;

public class SaveWindow {
    public static void loadSaveWindow() {
        DisplaySingleton window = DisplaySingleton.getInstance();
        JPanel panel = window.getCanvas();
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        window.setSize(400, 300);

        JPanel checkBoxPanel = new JPanel(new GridLayout(1, 0));
        JLabel title = new JLabel("Doc type");
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JCheckBox csvBox = new JCheckBox("CSV");
        JCheckBox excelBox = new JCheckBox("EXCEL");
        checkBoxPanel.add(new JLabel());
        checkBoxPanel.add(csvBox); checkBoxPanel.add(excelBox);
        checkBoxPanel.add(new JLabel());

        JPanel saveOrLoadPanel = new JPanel(new GridLayout(1, 0));
        JLabel title1 = new JLabel("Save or load file?");
        title1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JCheckBox saveBox = new JCheckBox("Save");
        JCheckBox loadBox = new JCheckBox("Load");
        saveOrLoadPanel.add(new JLabel());
        saveOrLoadPanel.add(saveBox); saveOrLoadPanel.add(loadBox);
        saveOrLoadPanel.add(new JLabel());

        JPanel filePanel = new JPanel();
        JTextField fileName = new JTextField();
        JLabel extension = new JLabel();
        fileName.setPreferredSize(new Dimension(200, 30));
        filePanel.add(fileName); filePanel.add(extension);

        JButton saveLoadButton = new JButton();
        saveLoadButton.setVisible(false);
        saveLoadButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

        csvBox.addActionListener(e -> {
            if(csvBox.isSelected()) {
                if(excelBox.isSelected())
                    excelBox.setSelected(false);
                extension.setText(".csv");
                if(saveBox.isSelected() || loadBox.isSelected()) {
                    saveLoadButton.setVisible(true);
                }
            }
            else {
                if(!excelBox.isSelected()) {
                    extension.setText("");
                    saveLoadButton.setVisible(false);
                }
            }
            panel.repaint();
        });
        excelBox.addActionListener(e -> {
            if(excelBox.isSelected()) {
                csvBox.setSelected(false);
                extension.setText(".xlsx");
                if(saveBox.isSelected() || loadBox.isSelected()) {
                    saveLoadButton.setVisible(true);
                }
            }
            else {
                if(!csvBox.isSelected()) {
                    extension.setText("");
                    saveLoadButton.setVisible(false);
                }
            }
            panel.repaint();
        });

        saveBox.addActionListener(e -> {
            if(saveBox.isSelected()) {
                loadBox.setSelected(false);
                saveLoadButton.setText("Save");
                if(csvBox.isSelected() || excelBox.isSelected()) {
                    saveLoadButton.setVisible(true);
                }
            }
            else {
                saveLoadButton.setVisible(false);
            }
        });
        loadBox.addActionListener(e -> {
            if(loadBox.isSelected()) {
                saveBox.setSelected(false);
                saveLoadButton.setText("Load");
                if(csvBox.isSelected() || excelBox.isSelected()) {
                    saveLoadButton.setVisible(true);
                }
            }
            else {
                saveLoadButton.setVisible(false);
            }
        });

        saveLoadButton.addActionListener(e -> {
            if(saveBox.isSelected()) {
                if(csvBox.isSelected()) {
                    CSVHandler save = new CSVHandler(fileName.getText().trim()+".csv", false);
                    save.writeOutData();
                }
                else {
                    ExcelHandler writer = new ExcelHandler(fileName.getText().trim()+".xlsx", false);
                    writer.writeOutData();
                }
            }
            else {
                if(csvBox.isSelected()) {
                    CSVHandler reader = new CSVHandler(fileName.getText()+".csv", true);
                    reader.loadData();
                }
                else {
                    ExcelHandler reader = new ExcelHandler(fileName.getText().trim()+".xlsx", true);
                    reader.inputData();
                }
            }
        });

        panel.add(TitleWindowSetup.backButton());
        panel.add(title);
        panel.add(checkBoxPanel);
        panel.add(title1);
        panel.add(saveOrLoadPanel);
        panel.add(filePanel);
        panel.add(saveLoadButton);
        panel.repaint();
    }
}
