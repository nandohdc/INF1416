package activity;

import javax.swing.*;
import java.util.ArrayList;

public class RegisterPanel extends JPanel {

    private JLabel register;
    private JLabel labelCert;
    private JTextField cert;
    private JLabel labelGroup;
    private JComboBox<String> group;
    private JLabel labelPassword;
    private JPasswordField password;
    private JLabel labelPasswordConfirm;
    private JPasswordField passwordConfirm;
    private JButton buttonRegister;
    private JButton buttonBack;
    private ArrayList<String> strings;

    public RegisterPanel(MainFrame.Listener listener){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        register = new JLabel("Formulário de Cadastro:");
        add(register);
        JPanel panelCert = new JPanel();
        panelCert.setLayout(new BoxLayout(panelCert, BoxLayout.LINE_AXIS));
        labelCert = new JLabel("Caminho do arquivo do certificado digital:");
        panelCert.add(labelCert);
        cert = new JTextField();
        panelCert.add(cert);
        add(panelCert);

        JPanel panelGroup = new JPanel();
        panelGroup.setLayout(new BoxLayout(panelGroup, BoxLayout.LINE_AXIS));
        labelGroup = new JLabel("Grupo:");
        panelGroup.add(labelGroup);
        String[] choices = {"Administrador", "Usuario"};
        group = new JComboBox<>(choices);
        panelGroup.add(group);
        add(panelGroup);

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

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.LINE_AXIS));
        buttonRegister = new JButton("Cadastrar");
        buttonRegister.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("register");
            strings.add(cert.getText());
            strings.add(group.getSelectedItem().toString());
            strings.add(new String(password.getPassword()));
            strings.add(new String(passwordConfirm.getPassword()));
            listener.onClick(strings);
        });
        panelButton.add(buttonRegister);
        buttonBack = new JButton("Voltar");
        buttonBack.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("back");
            listener.onClick(strings);
        });
        panelButton.add(buttonBack);
        add(panelButton);

    }

}
