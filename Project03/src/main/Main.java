package main;

import activity.MainFrame;
import model.GroupDAO;
import model.UserDAO;
import security.Authencation;

import java.util.Date;

public class Main {

    private static Authencation auth;

    private static MainFrame mainFrame;

    public static void main(String[] args) {
//        UserDAO userDAO = new UserDAO("lucasxvirtual@gmail.com", "123456", false, null, 0, 0, "123", 0, GroupDAO.get(1));
//        userDAO.save();

        auth = new Authencation();
        mainFrame = MainFrame.getInstance();
        loadSetLogin();

    }

    private static void loadSetLogin() {
        mainFrame.setLogin(strings -> {
            UserDAO user = UserDAO.get(strings.get(0));
            if (user == null) {
                mainFrame.showError("Usuario nao existe");
            } else {

                if (!auth.FirstValidation(user.getCertificateFactory(), strings.get(0))) {
                    System.out.println("O e-mail fornecido está incorreto.");
                } else {
                    loadPasswordView(user);
                }

            }

        });

    }

    private static void loadPasswordView(UserDAO user) {
        mainFrame.setPassword(strings -> {

            if (strings.get(0) == null) {
               /* if (!auth.SecondValidation(strings, user.getSalt(), user.getHashPassword())) {
                    System.out.println("O Senha fornecida está incorreta.");
                    user.setAttempt(user.getAttempt() + 1);
                    if (3 - user.getAttempt() > 0) {//se o contador de senha for menor que 3
                        System.out.println("Faltam " + (3 - user.getAttempt()) + " tentativas");
                        loadPasswordView(user);
                    } else {
                        System.out.println("Usuário bloqueado.");
                        //voltar para primeira tela de login - email.
                        loadSetLogin();
                    }
                } else {
                    System.out.println("A senha está correta");
                    user.setAttempt(0);
                    loadSecretKeyView(user);
                }*/


            }
        });
    }

    private static void loadSecretKeyView(UserDAO userDAO) {
        mainFrame.setSecretKey(strings -> {
            if (userDAO.getGroup().getName().equals("Administrador"))
                loadAdminView(userDAO);
            else
                loadUserView(userDAO);
        });
    }

    private static void loadAdminView(UserDAO userDAO) {
        mainFrame.setMainScreenAdmin(userDAO.getLoginName(), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalAccess(),
                strings -> {

                });
    }

    private static void loadUserView(UserDAO userDAO) {

    }
}