package main;

import activity.MainFrame;
import model.UserDAO;
import security.Authencation;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
//        UserDAO userDAO = new UserDAO("lucasxvirtual@gmail.com", "lucas", "123456", false, null, 0, 0, null, 0, null, GroupDAO.get(1));
//        userDAO.save();

        Authencation auth = new Authencation();
        UserDAO otherUser = UserDAO.get("lucasxvirtual@gmail.com");
        UserDAO inexistentUser = UserDAO.get("lucasxvirtual1@gmail.com");
        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setPassword(new MainFrame.Listener() {
            @Override
            public void onClick(ArrayList<String> strings1) {
                mainFrame.setMainScreenAdmin(otherUser.getName(), otherUser.getGroup().getName(), otherUser.getLoginName(), otherUser.getTotalAccess());

                if (auth.FirstValidation(null, otherUser.getLoginName())){ //Caso o e-mail seja válido, passar para próxima etapa.
                    if(true){ //Caso contrário, o processo deve seguir para a segunda etapa.

                    } else{//o acesso do usuário estiver bloqueado, o mesmo deve ser apropriadamente avisado e o processo deve permanecer na primeira etapa.

                    }

                } else {
                    //Caso o e-mail seja inválido, avaliar quais sao as punições.
                    //Se a identificação for inválida, o usuário deve ser apropriadamente avisado
                    // e o processo deve permanecer na primeira etapa.

                }
            }
        });
    }

}
