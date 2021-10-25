package viewer;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.TableView;

public class ResultsTableModel implements TableModel {
    private Object[][] data;
    private String[] columnNames;



    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (inRange(rowIndex, columnIndex)) {
            return data[rowIndex][columnIndex];
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Object o = getValueAt(0, columnIndex);
        if (o == null) {
            return null;
        } else {
            return o.getClass();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (inRange(rowIndex, columnIndex)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (inRange(rowIndex, columnIndex)) {
            data[rowIndex][columnIndex] = aValue;
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    private boolean rowInRange(int rowIndex) {
        return (rowIndex >= 0) && (rowIndex < getRowCount());
    }

    private boolean colInRange(int colIndex) {
        return (colIndex >= 0) && (colIndex < getColumnCount());
    }

    private boolean inRange(int rowIndex, int colIndex) {
        return rowInRange(rowIndex) && colInRange(colIndex);
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }
}
