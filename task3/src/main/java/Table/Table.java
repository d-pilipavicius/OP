package Table;

import DataStructures.*;
import javax.swing.*;
import javax.swing.table.*;

public class Table extends JTable implements RowModifier {
    public Table() {
        super(new DefaultTableModel(new String[0][0], new String[]{"Name", "Group", "Date"}));
        setEnabled(false);
    }
    public Table(Student[] students) {
        super(new DefaultTableModel(new String[0][0], new String[]{"Name", "Group", "Date"}));
        for(int i = 0; i < students.length; ++i) {
            for(int j = 0; j < students[i].getDayCount(); ++j) {
                addRow();
                valuesToRows(getRowCount()-1, new String[]{
                        students[i].getName(), students[i].getGroup(), students[i].getDay(j)});
            }
        }
        setEnabled(false);
    }
    public Table(Object[][] data) {
        super(new DefaultTableModel(data, new String[]{"Name", "Group", "Date"}));
        setEnabled(false);
    }

    @Override
    public void addRow() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(getRowCount()+1);
    }
    @Override
    public void removeRow(int row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        for(int i = 0; i < getColumnCount(); ++i) {
            for (int j = row; j < getRowCount()-1; j++) {
                setValueAt(getValueAt(j+1, i), j, i);
            }
        }
        model.setRowCount(model.getRowCount()-1);
    }

    @Override
    public void addRows(int rowCount) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(model.getRowCount()+rowCount);
    }
    @Override
    public String[] getRowData(int row) {
        String[] rowData = new String[3];
        rowData[0] = (String) getValueAt(row, 0);
        rowData[1] = (String) getValueAt(row, 1);
        rowData[2] = (String) getValueAt(row, 2);
        return rowData;
    }
    @Override
    public void valuesToRows(int row, String[] values) {
        for(int i = 0; i < getColumnCount(); ++i) {
            setValueAt(values[i], row, i);
        }
    }
    public void resetValues() {
        DataHolder holder = DataHolder.getGlobalHolder();
        int rows = getRowCount();
        for(int i = 0; i < rows; ++i)
            removeRow(0);
        Student[] studs = holder.getStudents();
        int rowCount = 0;
        for(int i = 0; i < studs.length; ++i) {
            for(int j = 0; j < studs[i].getDayCount(); ++j) {
                addRow();
                valuesToRows(rowCount++, new String[]{studs[i].getName(), studs[i].getGroup(), studs[i].getDay(j)});
            }
        }
    }
    public void getValuesFromHolder(DataHolder holder) {
        int rows = getRowCount();
        for(int i = 0; i < rows; ++i) {
            removeRow(0);
        }
        Student[] studs = holder.getStudents();
        int rowCount = 0;
        for(int i = 0; i < studs.length; ++i) {
            for(int j = 0; j < studs[i].getDayCount(); ++j) {
                addRow();
                valuesToRows(rowCount++, new String[]{studs[i].getName(), studs[i].getGroup(), studs[i].getDay(j)});
            }
        }
    }
}
