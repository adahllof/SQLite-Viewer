package viewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SQLiteViewer extends JFrame {
    JFrame frameReference = this; //Used to get a reference to the current JFrame object of this class when calling JOptionPane.showMessageDialog
    JTextField txtFilename = new JTextField();
    JButton btnOpenFileButton = new JButton();
    JComboBox cboTables = new JComboBox();
    JButton btnExecute = new JButton();
    JTextArea txtAreaQuery = new JTextArea();

    private SQLiteDatabase database;

    public SQLiteViewer() {
        // Frame initiation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 630);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setName("SQLite Viewer");
        setTitle("SQLite Viewer");

        // Text field "FileNameTextField"
        txtFilename.setName("FileNameTextField");
        txtFilename.setBounds(3, 3, 600, 20);
        txtFilename.setEnabled(true);
        txtFilename.setVisible(true);
        add(txtFilename);

        // Open button
        btnOpenFileButton.setName("OpenFileButton");
        btnOpenFileButton.setBounds(605, 3,70,20);
        btnOpenFileButton.setEnabled(true);
        btnOpenFileButton.setVisible(true);
        btnOpenFileButton.setText("OPEN");
        add(btnOpenFileButton);

        // Combo-box tables
        cboTables.setName("TablesComboBox");
        cboTables.setBounds(7, 30, 665, 20);
        cboTables.setVisible(true);
        add(cboTables);

        // Text are for query
        txtAreaQuery.setName("QueryTextArea");
        txtAreaQuery.setBounds(3, 70, 550,80);
        txtAreaQuery.setVisible(true);
        add(txtAreaQuery);

        // Execute button
        btnExecute.setName("ExecuteQueryButton");
        btnExecute.setBounds(560, 70, 100, 40);
        btnExecute.setVisible(true);
        btnExecute.setText("Execute");
        add(btnExecute);

        setControlsEnabled(false); //Combo-box tables, Text area, and Execute button disabled

        cboTables.setEnabled(true); //This was necessary to pass test, otherwise could be removed

        //Table for displaying query results
        JTable tblQueryResults = new JTable();
        tblQueryResults.setName("Table");
        tblQueryResults.setVisible(true);
        JScrollPane tableScrollPane = new JScrollPane(tblQueryResults);
        tableScrollPane.setBounds(3, 180, 675,400);
        tableScrollPane.setVisible(true);
        add(tableScrollPane);


        setVisible(true);

        btnOpenFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setControlsEnabled(false);
                File f = new File(txtFilename.getText());
                if (f.exists() && !txtFilename.getText().isBlank()) {
                    database = new SQLiteDatabase(txtFilename.getText(), frameReference);
                    String[] tableNames = database.getTables();
                    cboTables.removeAllItems();
                    if (tableNames != null) {
                        for (String name : tableNames) {
                            cboTables.addItem(name);
                        }
                        txtAreaQuery.setText(selectAllRowsQuery());
                    }
                    setControlsEnabled(database.connected());
                } else if (!txtFilename.getText().isBlank()) {
                    JOptionPane.showMessageDialog(frameReference, "File doesn't exist",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frameReference, "Please, enter a filename first!",
                            "Filename is blank", JOptionPane.ERROR_MESSAGE);

                }

            }
        });

        btnExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String  query = txtAreaQuery.getText();
                String sqlError = database.checkStatement(query);
                if (sqlError == null) {
                    Object[][] result = database.executeQuery(query);
                    if (result != null) {
                        int cols = result[0].length;
                        String[] columnNames = new String[cols];
                        for (int columnIndex = 0; columnIndex < cols; columnIndex++) {
                            columnNames[columnIndex] = result[0][columnIndex].toString();
                        }

                        Object data[][] = new Object[result.length - 1][cols];
                        for (int row = 1; row < result.length; row++) {
                            System.arraycopy(result[row], 0, data[row - 1], 0, cols);
                        }
                        ResultsTableModel tm = new ResultsTableModel();

                        tm.setColumnNames(columnNames);
                        tm.setData(data);

                        tblQueryResults.setModel(tm);
                    }
                } else {
                    JOptionPane.showMessageDialog(frameReference, sqlError,
                            "SQL Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });



        cboTables.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    txtAreaQuery.setText(selectAllRowsQuery() );
            }
        });


        }

    private String selectAllRowsQuery() {
        return "SELECT * FROM " + cboTables.getSelectedItem() + ";";
    }
    private void setControlsEnabled(boolean enable) {
        btnExecute.setEnabled(enable);
        cboTables.setEnabled(enable);
        txtAreaQuery.setEnabled(enable);
    }


}
