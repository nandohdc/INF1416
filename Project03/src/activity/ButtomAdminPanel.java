package activity;

import javax.swing.*;
import java.util.ArrayList;

public class ButtomAdminPanel extends JPanel {

    JLabel jLabel;
    JButton buttonRegister;
    JButton buttonUpdate;
    JButton buttonFiles;
    JButton buttonExit;
    ArrayList<String> strings;

    public ButtomAdminPanel(MainFrame.Listener listener){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        jLabel = new JLabel("Menu Principal:");
        add(jLabel);
        buttonRegister = new JButton("Cadastrar um novo usuário");
        buttonRegister.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("register");
            listener.onClick(strings);
        });
        add(buttonRegister);
        buttonUpdate = new JButton("Alterar senha pessoal e certificado digital do usuário");
        buttonUpdate.addActionListener( e -> {
            strings = new ArrayList<>();
            strings.add("update");
            listener.onClick(strings);
        });
        add(buttonUpdate);
        buttonFiles = new JButton("Consultar pasta de arquivos secretos do usuário");
        buttonFiles.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("files");
            listener.onClick(strings);
        });
        add(buttonFiles);
        buttonExit = new JButton("Sair do Sistema");
        buttonExit.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("exit");
            listener.onClick(strings);
        });
        add(buttonExit);

    }

}
