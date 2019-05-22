package activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SecretKeyPanel extends JPanel {

    JLabel label;
    JTextField secretKey;
    JButton button;

    public SecretKeyPanel(MainFrame.Listener listener){
        label = new JLabel("Chave secreta");
        secretKey = new JTextField();
        secretKey.setPreferredSize(new Dimension(300, 40));
        button = new JButton("proxímo");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(secretKey.getText());
                listener.onClick(strings);
            }
        });
        add(label);
        add(secretKey);
        add(button);
    }

}
