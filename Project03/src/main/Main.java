package main;

import activity.MainFrame;
import model.UserDAO;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
//        UserDAO userDAO = new UserDAO("lucasxvirtual@gmail.com", "lucas", "123456", false, null, 0, 0, null, 0, null, GroupDAO.get(1));
//        userDAO.save();
        UserDAO otherUser = UserDAO.get("lucasxvirtual@gmail.com");
        UserDAO inexistentUser = UserDAO.get("lucasxvirtual1@gmail.com");
        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setPassword(new MainFrame.Listener() {
            @Override
            public void onClick(ArrayList<String> strings1) {
                mainFrame.setMainScreenAdmin(otherUser.getName(), otherUser.getGroup().getName(), otherUser.getLoginName(), otherUser.getTotalAccess());
            }
        });
    }

}
