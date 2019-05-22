package activity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class RegistryPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public RegistryPanel(String[][]list){
        String[] header = {"mensagem", "usuario", "file", "data"};
        tableModel = new DefaultTableModel(list, header);
        table = new JTable(tableModel){
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(380, 10, 600, 600);
        table.setFillsViewportHeight(true);
        add(table.getTableHeader());
        add(scrollPane);
    }

}
