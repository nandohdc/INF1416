package activity;

import javax.swing.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private static MainFrame instance;
    private JPanel jPanel = new JPanel();

    public static MainFrame getInstance(){
        if(instance == null){
            instance = new MainFrame("Trab1");
            instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            instance.pack();
            instance.setSize(540, 540);
            instance.setVisible(true);
        }
        return instance;
    }

    private MainFrame(){
    }

    private MainFrame(String name){
        super(name);
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        add(jPanel);
    }

    public void setLogin(Listener listener){
        jPanel.removeAll();
        validate();
        jPanel.add(new LoginPanel(listener));
        validate();
    }

    public void setSecretKey(Listener listener){
        jPanel.removeAll();
        validate();
        jPanel.add(new SecretKeyPanel(listener));
        validate();
    }

    public void setPassword(Listener listener){
        jPanel.removeAll();
        validate();
        jPanel.add(new PasswordPanel(listener));
        validate();
    }

    public void setMainScreenAdmin(String name, String group, String login, int totalAccess, Listener listener){
        jPanel.removeAll();
        validate();
        setHeader(name, group, login);
        setTotalAccess(totalAccess);
        setButtomAdmin(listener);
        validate();
    }

    public void setRegister(String name, String group, String login, int totalUsers, Listener listener){
        jPanel.removeAll();
        validate();
        setHeader(name, group, login);
        setTotalUsers(totalUsers);
        jPanel.add(new RegisterPanel(listener));
        validate();
    }

    public void setUpdate(String name, String group, String login, int totalAccess, Listener listener){
        jPanel.removeAll();
        validate();
        setHeader(name, group, login);
        setTotalAccess(totalAccess);
        jPanel.add(new UpdatePanel(listener));
        validate();
    }

    public void setExit(String name, String group, String login, int totalAccess, Listener listener){
        jPanel.removeAll();
        validate();
        setHeader(name, group, login);
        setTotalAccess(totalAccess);
        jPanel.add(new ExitPanel(listener));
        validate();
    }

    public void setFiles(String name, String group, String login, int totalQuery, String[][]files, Listener listener){
        jPanel.removeAll();
        validate();
        setHeader(name, group, login);
        setTotalQuery(totalQuery);
        jPanel.add(new FilesPanel(listener, files));
        validate();
    }

    private void setHeader(String name, String group, String login){
        jPanel.add(new HeaderPanel(name, login, group));
    }

    private void setTotalAccess(int totalAccess){
        BodyOnePanel bodyOnePanel = BodyOnePanel.getInstance();
        bodyOnePanel.setText("Total de acessos do usuário: " + totalAccess);
        jPanel.add(bodyOnePanel);
    }

    private void setButtomAdmin(Listener listener){
        ButtomAdminPanel buttomAdminPanel = new ButtomAdminPanel(listener);
        jPanel.add(buttomAdminPanel);
    }

    private void setTotalUsers(int totalUsers) {
        BodyOnePanel bodyOnePanel = BodyOnePanel.getInstance();
        bodyOnePanel.setText("Total de usuários do sistema: " + totalUsers);
        jPanel.add(bodyOnePanel);
    }

    private void setTotalQuery(int totalQuery) {
        BodyOnePanel bodyOnePanel = BodyOnePanel.getInstance();
        bodyOnePanel.setText("Total de consultas do usuário: " + totalQuery);
        jPanel.add(bodyOnePanel);
    }

    public void showError(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    public void removeBodyOne(){
        jPanel.remove(BodyOnePanel.getInstance());
    }

    public interface Listener {
        void onClick(ArrayList<String> strings);
    }

}
