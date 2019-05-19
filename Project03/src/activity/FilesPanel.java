package activity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class FilesPanel extends JPanel {

    private JLabel labelList;
    private JTextField list;
    private JButton buttonList;
    private JTable table;
    private JButton buttonBack;
    private ArrayList<String> strings;

    public FilesPanel(MainFrame.Listener listener, String[][] list){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel panelCert = new JPanel();
        panelCert.setLayout(new BoxLayout(panelCert, BoxLayout.LINE_AXIS));
        labelList = new JLabel("Caminho do arquivo do certificado digital:");
        panelCert.add(labelList);
        this.list = new JTextField();
        panelCert.add(this.list);
        add(panelCert);

        buttonList = new JButton("Listar");
        buttonList.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("list");
            strings.add(this.list.getText());
            listener.onClick(strings);
        });
        add(buttonList);

        buttonBack = new JButton("Voltar");
        buttonBack.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("back");
            listener.onClick(strings);
        });
        add(buttonBack);

        String[] header = {"nome código","nome secreto", "dono", "grupo"};
        DefaultTableModel tableModel = new DefaultTableModel(list, header);
        table = new JTable(tableModel){
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };
        table.getSelectionModel().addListSelectionListener(e -> {
            strings = new ArrayList<>();
            strings.add("item");
            strings.add(String.valueOf(table.getSelectedRow()));
            listener.onClick(strings);
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(380, 10, 400, 400);
        table.setFillsViewportHeight(true);
        add(table.getTableHeader());
        add(scrollPane);

//        buttonBack = new JButton("Voltar");
//        buttonBack.addActionListener(e -> {
//            strings = new ArrayList<>();
//            strings.add("back");
//            listener.onClick(strings);
//        });
//        add(buttonBack);
    }

}
