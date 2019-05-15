package activity;

import javax.swing.*;

public class HeaderPanel extends JPanel {

    private static HeaderPanel instance;

    private HeaderPanel(){}

    public static HeaderPanel getInstance(){
        if(instance == null)
            instance = new HeaderPanel();

        return instance;
    }

    public void _setName(String name){
        JLabel _name = new JLabel("Nome: " + name);
        add(_name);
    }

    public void _setLogin(String login){
        JLabel _login = new JLabel("Login: " + login);
        add(_login);
    }

    public void _setGroup(String group){
        JLabel _group = new JLabel("Grupo: " + group);
        _group.setText("Grupo: " + group);
        add(_group);

    }

}
