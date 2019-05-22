package activity;

import javax.swing.*;
import java.util.ArrayList;

public class ExitPanel extends JPanel {

    private JLabel labelExit;
    private JLabel labelConfirm;
    private JButton buttonExit;
    private JButton buttonBack;
    private ArrayList<String> strings;

    public ExitPanel(MainFrame.Listener listener){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        labelExit = new JLabel("Saída do sistema:");
        add(labelExit);
        labelConfirm = new JLabel("Pressione o botão Sair para confirmar.");
        add(labelConfirm);
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.LINE_AXIS));
        buttonExit = new JButton("Sair");
        buttonExit.addActionListener(e -> {
            strings = new ArrayList<>();
            strings.add("exit");
            listener.onClick(strings);
        });
        panelButton.add(buttonExit);
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
