package activity;

import javax.swing.*;

public class HeaderPanel extends JPanel {

    public HeaderPanel(String name, String login, String group){
        JLabel _name = new JLabel("Nome: " + name);
        add(_name);
        JLabel _login = new JLabel("Login: " + login);
        add(_login);
        JLabel _group = new JLabel("Grupo: " + group);
        add(_group);
    }

}
