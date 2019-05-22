package activity;

import javax.swing.*;
import java.util.ArrayList;

public class UpdatePanel extends JPanel {

    private JLabel labelCert;
    private JTextField cert;
    private JLabel labelPassword;
    private JPasswordField password;
    private JLabel labelPasswordConfirm;
    private JPasswordField passwordConfirm;
    private JButton buttonUpdate;
    private ArrayList<String> strings;

    public UpdatePanel(MainFrame.Listener listener){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel panelCert = new JPanel();
        panelCert.setLayout(new BoxLayout(panelCert, BoxLayout.LINE_AXIS));
        labelCert = new JLabel("Caminho do certificado digital:");
        panelCert.add(labelCert);
        cert = new JTextField();
        panelCert.add(cert);
        add(panelCert);

        JPanel panelPassword = new JPanel();
        panelPassword.setLayout(new BoxLayout(panelPassword, BoxLayout.LINE_AXIS));
        labelPassword = new JLabel("Senha pessoal:");
        panelPassword.add(labelPassword);
        password = new JPasswordField();
        panelPassword.add(password);
        add(panelPassword);

        JPanel panelPasswordConfirm = new JPanel();
        panelPasswordConfirm.setLayout(new BoxLayout(panelPasswordConfirm, BoxLayout.LINE_AXIS));
        labelPasswordConfirm = new JLabel("Confirmação senha pessoal:");
        panelPasswordConfirm.add(labelPasswordConfirm);
        passwordConfirm = new JPasswordField();
        panelPasswordConfirm.add(passwordConfirm);
        add(panelPasswordConfirm);

        buttonUpdate = new JButton("Salvar e voltar");
        buttonUpdate.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add(cert.getText());
            strings.add(new String(password.getPassword()));
            strings.add(new String(passwordConfirm.getPassword()));
            listener.onClick(strings);
        });
        add(buttonUpdate);
    }

}
