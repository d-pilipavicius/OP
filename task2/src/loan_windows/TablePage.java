package loan_windows;

import loan_calculator.DataStorage;
import number_work.NumeralWork;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class TablePage extends JPanel {
    private static String name = "Table";
    private static JFrame frame;
    private static String[][] data;
    private static String[] names = new String[]{"Month", "Principal", "Interest", "Month Total", "Remaining"};
    private static JTable table;
    private static DataStorage storage;
    private TablePage(int from, int to) {
        super(new BorderLayout());
        readStorage(from, to);
        table = new JTable(data, names);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        table.setEnabled(false);
        frame.add(this);
    }
    public static void openTable(DataStorage dataStorage, int from, int to) {
        if(frame != null) frame.dispose();
        frame = new JFrame(name);
        storage = dataStorage;
        frame.add(new TablePage(from, to));
        SwingUtilities.invokeLater(() -> new TablePage(from, to));
        frame.setSize(500, 800);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
    private void readStorage(int from, int to) {
        data = new String[to-from+1][names.length];
        double[] pr, inter, monthTot, remain;
        pr = storage.getPrincipals();
        inter = storage.getInterests();
        monthTot = NumeralWork.sumTwoArrays(pr, inter);
        remain = storage.getRemainings();
        for(int i = 0; i < to-from+1; ++i) {
            data[i][0] = (i+1+from)+"";
            data[i][1] = String.format(Locale.US, "%.2f", pr[from+i]);
            data[i][2] = String.format(Locale.US, "%.2f", inter[from+i]);
            data[i][3] = String.format(Locale.US, "%.2f", monthTot[from+i]);
            data[i][4] = String.format(Locale.US, "%.2f", remain[from+i]);
        }
    }
}
