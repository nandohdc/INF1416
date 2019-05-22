package log;

import activity.MainFrame;
import activity.RegistryPanel;
import model.MessageDAO;
import model.RegistryDAO;
import model.UserDAO;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        ArrayList<RegistryDAO> registryDAOS = RegistryDAO.getAll();
        String[][] strings = new String[registryDAOS.size()][4];
        int i = 0;
        for(RegistryDAO registryDAO : registryDAOS){
            strings[i][0] = MessageDAO.get(registryDAO.getMessageId()).getText();
            if(registryDAO.getUserId() != null)
                strings[i][1] = registryDAO.getUserId().getLoginName();
            else
                strings[i][1] = "-";
            strings[i][2] = registryDAO.getFile();
            strings[i][3] = registryDAO.getCreated();
            i++;
        }
        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setLog(strings);

    }

}
