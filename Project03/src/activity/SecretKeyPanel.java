package activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SecretKeyPanel extends JPanel {

    JLabel label;
    JLabel pem;
    JTextField pemText;
    JPasswordField secretKey;
    JButton button;

    public SecretKeyPanel(MainFrame.Listener listener){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel jPanelPem = new JPanel();
        jPanelPem.setLayout(new BoxLayout(jPanelPem, BoxLayout.LINE_AXIS));
        pem = new JLabel("Chave privada");
        pemText = new JTextField();
        label = new JLabel("Chave secreta");
        secretKey = new JPasswordField();
        secretKey.setPreferredSize(new Dimension(300, 40));
        button = new JButton("proxímo");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(new String(secretKey.getPassword()));
                strings.add(pemText.getText());
                listener.onClick(strings);
            }
        });
        JPanel jPanelKey = new JPanel();
        jPanelKey.setLayout(new BoxLayout(jPanelKey, BoxLayout.LINE_AXIS));

        jPanelPem.add(pem);
        jPanelPem.add(pemText);
        jPanelKey.add(label);
        jPanelKey.add(secretKey);
        add(jPanelPem);
        add(jPanelKey);
        add(button);
    }

}
