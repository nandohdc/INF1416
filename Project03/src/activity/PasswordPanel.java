package activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class PasswordPanel extends Panel {

    JLabel label;
    JPasswordField password;
    JButton button;
    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    ArrayList<String> strings = new ArrayList<>();

    public PasswordPanel(MainFrame.Listener listener){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        label = new JLabel("Senha");
        password = new JPasswordField();
        password.setEditable(false);
        password.setPreferredSize(new Dimension(300, 40));
        button = new JButton("proxímo");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onClick(strings);
                strings = new ArrayList<>();
                password.setText("");
            }
        });
        add(label);
        add(password);
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.LINE_AXIS));
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        button5 = new JButton();
        fillButtons();
        horizontalPanel.add(button1);
        horizontalPanel.add(button2);
        horizontalPanel.add(button3);
        horizontalPanel.add(button4);
        horizontalPanel.add(button5);
        add(horizontalPanel);
        add(button);
        setOnClickButtons();
    }

    public void setOnClickButtons(){
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickButton(button1);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickButton(button2);
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickButton(button3);
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickButton(button4);
            }
        });
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickButton(button5);
            }
        });

    }

    public void fillButtons(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("1");strings.add("2");strings.add("3");strings.add("4");strings.add("5");
        strings.add("6");strings.add("7");strings.add("8");strings.add("9");strings.add("0");
        strings.remove(getFirstRandonForButton(button1, strings));
        strings.remove(getFirstRandonForButton(button2, strings));
        strings.remove(getFirstRandonForButton(button3, strings));
        strings.remove(getFirstRandonForButton(button4, strings));
        strings.remove(getFirstRandonForButton(button5, strings));
        strings.remove(getSecondRandonForButton(button1, strings));
        strings.remove(getSecondRandonForButton(button2, strings));
        strings.remove(getSecondRandonForButton(button3, strings));
        strings.remove(getSecondRandonForButton(button4, strings));
        strings.remove(getSecondRandonForButton(button5, strings));
    }

    public void onClickButton(JButton button){
        String[] choices = button.getText().split(" ");
        if(strings.size() == 0){
            strings.add(choices[0]);
            strings.add(choices[1]);
        } else {
            ArrayList<String> newStrings = new ArrayList<>();
            for(String string : strings){
                newStrings.add(string + choices[0]);
                newStrings.add(string + choices[1]);
            }
            strings = newStrings;
        }
        password.setText(strings.get(0));
        fillButtons();
        validate();
    }

    public int getFirstRandonForButton(JButton button, ArrayList<String> strings){
        int random = new Random().nextInt(strings.size());
        button.setText(strings.get(random));
        return random;
    }

    public int getSecondRandonForButton(JButton button, ArrayList<String> strings){
        int random = new Random().nextInt(strings.size());
        button.setText(button.getText() + " " + strings.get(random));
        return random;
    }

}
