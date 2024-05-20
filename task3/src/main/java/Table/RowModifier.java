package Table;

public interface RowModifier {
    public abstract void addRow();
    public abstract void removeRow(int row);
    public abstract void addRows(int rowCount);
    public abstract String[] getRowData(int row);
    public abstract void valuesToRows(int row, String[] values);
}

