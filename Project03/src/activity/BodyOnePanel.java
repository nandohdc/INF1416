package activity;

import javax.swing.*;

public class BodyOnePanel extends JPanel {

    private static BodyOnePanel instance;

    private BodyOnePanel(){
    }

    public static BodyOnePanel getInstance(){
        if(instance == null)
            instance = new BodyOnePanel();
        return instance;
    }

    public void setText(String text){
        removeAll();
        add(new JLabel(text));
        validate();
    }

}
