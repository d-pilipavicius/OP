package loan_windows;

import loan_calculator.*;
import number_work.*;
import swing_changes.*;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataPage extends JPanel {
    private static final String panelName = "Data";
    private static JFrame frame;
    private static LineGraph lineGraph;
    private static int[][] data;
    private static DataStorage storage;
    private DataPage(){
        super();
        setLayout(null);
        ImprovedLabel rangeLabel = new ImprovedLabel(710, 50, "Get table with specific dates");

        ImprovedLabel from = new ImprovedLabel(710, 83, "From:");
        ImprovedTextField yearField1 = new ImprovedTextField(740, 80, 40, 20, "year");
        ImprovedTextField monthField1 = new ImprovedTextField(790, 80, 40, 20, "month");

        ImprovedLabel to = new ImprovedLabel(720, 113, "To:");
        ImprovedTextField yearField2 = new ImprovedTextField(740, 110, 40, 20, "year");
        ImprovedTextField monthField2 = new ImprovedTextField(790, 110, 40, 20, "month");

        JButton newRange = new JButton("Get");
        newRange.setSize(60, 20);
        newRange.setLocation(750, 140);
        newRange.setFont(new Font("Consolas", Font.PLAIN, 10));

        JButton saveData = new JButton("Save");
        saveData.setSize(60, 40);
        saveData.setLocation(770, 350);
        saveData.setFont(new Font("Consolas", Font.PLAIN, 10));

        newRange.addActionListener(e -> {
            try {
                int fromYear = ( (yearField1.isEmpty()) ? 0 : Integer.parseInt(yearField1.getText().trim()) );
                int fromMonth = ( (monthField1.isEmpty()) ? 0 : Integer.parseInt(monthField1.getText().trim()) );
                if(yearField1.isEmpty() && monthField1.isEmpty() || fromMonth > 12) {
                    System.out.println(yearField1.getText());
                    System.out.println(monthField1.getText());
                    System.out.println(yearField2.getText());
                    System.out.println(monthField2.getText());
                    System.out.println(fromMonth);
                    throw new NumberFormatException();
                }
                int toYear = ( (yearField2.isEmpty()) ? 0 : Integer.parseInt(yearField2.getText().trim()) );
                int toMonth = ( (monthField2.isEmpty()) ? 0 : Integer.parseInt(monthField2.getText().trim()) );
                if(yearField1.isEmpty() && monthField2.isEmpty() || toMonth > 12)
                    throw new NumberFormatException();
                TablePage.openTable(storage, fromYear*12+fromMonth-1, toYear*12+toMonth-1);
            }
            catch(NumberFormatException ex) {
                System.out.println("MAKE SURE YOU HAVE ENTERED THE DATA CORRECTLY.");
                ex.printStackTrace();
            }
        });

        saveData.addActionListener(e -> {
            SimpleDateFormat formatter = new SimpleDateFormat("yyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            storage.getReport("data"+formatter.format(date)+".txt");
        });

        readStorage();
        lineGraph = new LineGraph(0, 0, 700, 400, data);
        frame.add(rangeLabel);
        frame.add(from);
        frame.add(yearField1);
        frame.add(monthField1);
        frame.add(to);
        frame.add(yearField2);
        frame.add(monthField2);
        frame.add(newRange);
        frame.add(saveData);
    }
    public static void openData(DataStorage dataStorage) {
        if(frame != null) frame.dispose();
        frame = new JFrame(panelName);
        storage = dataStorage;
        frame.setSize(900, 450);
        frame.add(new DataPage());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        TablePage.openTable(dataStorage, 0, storage.getNrOfMonths()-1);
    }
    private void readStorage() {
        int numberOfGraphs = 1;
        data = new int[numberOfGraphs][storage.getNrOfMonths()];
        data[0] = NumeralWork.doubleArrToInt(storage.getTotalPayed());
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        lineGraph.displayGraph(g);
    }
}
