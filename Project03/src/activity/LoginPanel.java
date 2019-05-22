package activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginPanel extends JPanel {

    JLabel label;
    JTextField email;
    JButton button;

    public LoginPanel(MainFrame.Listener listener){
        label = new JLabel("E-mail");
        email = new JTextField();
        email.setPreferredSize(new Dimension(300, 40));
        button = new JButton("proxímo");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(email.getText());
                listener.onClick(strings);
            }
        });
        add(label);
        add(email);
        add(button);
    }

}
