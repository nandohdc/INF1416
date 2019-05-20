package main;

import activity.MainFrame;
import model.GroupDAO;
import model.RegistryDAO;
import model.UserDAO;
import security.Authencation;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Main {

    private static MainFrame mainFrame;
    private static Authencation auth = new Authencation();

    public static void main(String[] args) {

        mainFrame = MainFrame.getInstance();
        //loadLoginView();
        loadAdminView(UserDAO.get("admin@inf1416.puc-rio.br"));
    }

    private static void loadLoginView() {
        mainFrame.setLogin(strings -> {
            UserDAO user = UserDAO.get(strings.get(0));
            if (user == null) {
                mainFrame.showError("Usuario não existe");
            } else {
                if (user.isBlocked()) {
                    Date current = addMinutesToJavaUtilDate(stringToDate(user.getTimeBlocked()), 2);
                    if (current.getTime() > new Date().getTime()) {
                        mainFrame.showError("Usuário Bloqueado");
                        return;
                    } else {
                        user.setBlocked(false);
                        user.update();
                    }
                }

                if (!auth.FirstValidation(user.getLoginName(), strings.get(0))) {
                    mainFrame.showError("O e-mail fornecido está incorreto.");
                } else {
                    loadPasswordView(user);
                }
            }
        });
    }

    private static void loadPasswordView(UserDAO user) {
        mainFrame.setPassword(strings -> {
            if (!auth.SecondValidation(strings, user.getSalt(), user.getPassword())) {
                mainFrame.showError("O Senha fornecida está incorreta.");
                user.setAttempt(user.getAttempt() + 1);
                user.update();
                if (3 - user.getAttempt() > 0) {//se o contador de senha for menor que 3
                    System.out.println("Faltam " + (3 - user.getAttempt()) + " tentativas");
                    loadPasswordView(user);
                } else {
                    mainFrame.showError("Usuário bloqueado.");
                    user.setBlocked(true);
                    user.setTimeBlocked(dateToString(new Date()));
                    user.update();
                    loadLoginView();//voltar para primeira tela de login - email.
                }
            } else {
                System.out.println("A senha está correta");
                user.setAttempt(0);
                user.update();
                loadSecretKeyView(user);
            }
        });
    }

    private static void loadSecretKeyView(UserDAO user) {
        mainFrame.setSecretKey(strings -> {
            if (user.getCertificate() != null && !user.getCertificate().trim().isEmpty()) {

                if (!auth.ThirdValidation(strings.get(0), "Keys/admin-pkcs8-des.pem", getCertificatePublicKey(user.getCertificate()))) {
                    //Se a verificação for negativa, o usuário deve ser apropriadamente avisado e
                    // o processo deve contabilizar um erro de verificação da chave privada,
                    // retornando para o início da terceira etapa
                    mainFrame.showError("A Secret Key fornecida está incorreta.");
                    user.setAttempt(user.getAttempt() + 1);
                    user.update();
                    if (3 - user.getAttempt() > 0) {//se o contador de senha for menor que 3
                        System.out.println("Faltam " + (3 - user.getAttempt()) + " tentativas");
                        loadSecretKeyView(user);
                    } else {
                        mainFrame.showError("Usuário bloqueado.");
                        user.setBlocked(true);
                        user.setTimeBlocked(dateToString(new Date()));
                        user.update();
                        loadLoginView();//voltar para primeira tela de login - email.
                    }

                } else {// Se a verificação for positiva, o processo deve permitir acesso ao sistema.
                    //Entrar no sistema;
                    System.out.println("A Chave Privada está correta!");
                    user.setAttempt(0);
                    user.setTotalAccess(user.getTotalAccess() + 1);
                    user.update();
                    if (user.getGroup().getName().equals("Administrador")) {
                        loadAdminView(user);
                    } else {
                        loadUserView(user);
                    }
                }
            }

        });
    }

    private static void loadAdminView(UserDAO userDAO) {
        mainFrame.setMainScreenAdmin(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalAccess(),
                strings -> {
                    if (strings.get(0).equals("register"))
                        loadRegisterView(userDAO);
                    else if (strings.get(0).equals("update"))
                        loadUpdateView(userDAO);
                    else if (strings.get(0).equals("exit"))
                        loadExitView(userDAO);
                    else if (strings.get(0).equals("files"))
                        loadFileView(userDAO);
                });
    }

    private static void loadUserView(UserDAO userDAO) {

    }

    private static void loadFileView(UserDAO userDAO) {
        String[][] strings = {{"t1", "t2", "t3", "t4"}, {"t1", "t2", "t3", "t4"}, {"t1", "t2", "t3", "t4"}};
        mainFrame.setFiles(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalQuery(), strings, strings1 -> {
            if (strings1.get(0).equals("back"))
                loadAdminView(userDAO);
        });
    }

    private static void loadRegisterView(UserDAO userDAO) {
        mainFrame.setRegister(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), 10, strings -> {
            if(strings.get(0).equals("back")){
                loadAdminView(userDAO);
            } else {
                Path cfPath = Paths.get(strings.get(1));
                byte[] fileBytes = auth.readFile(cfPath);

                InputStream stream = new ByteArrayInputStream(fileBytes);
                CertificateFactory factory = null;
                try {
                    factory = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate) factory.generateCertificate(stream);
                    stream.close();

                    String subjectDN = cert.getSubjectDN().getName();
                    int start = subjectDN.indexOf("=");
                    int end = subjectDN.indexOf(",");
                    String email = subjectDN.substring(start + 1, end);

                    String sCertificate = certificateToString(cert);

                    String salt = auth.SaltGen();

                    if(!strings.get(3).equals(strings.get(4))){
                        mainFrame.showError("As senhas digitadas não são iguais");
                        return;
                    }

                    String password = auth.CalculateHexHashPassword(strings.get(4), salt);

                    GroupDAO gp;

                    if(strings.get(2).equals("Administrador")){
                        gp = GroupDAO.get(1);
                    } else{
                        gp = GroupDAO.get(2);
                    }

                    if(password == null){
                        mainFrame.showError("A senha não está de acordo com a política de senhas.");
                        return;
                    } else{
                        UserDAO newUser = new UserDAO(email,password,salt,false,null, 0,0,sCertificate,0,gp);
                    }

                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void loadUpdateView(UserDAO userDAO) {
        mainFrame.setUpdate(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalAccess(), strings -> {
            loadAdminView(userDAO);
        });
    }

    private static void loadExitView(UserDAO userDAO) {
        mainFrame.setExit(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalAccess(), strings -> {
            loadLoginView();
        });
    }

    private static Date stringToDate(String date) {
        String dateFormat = "yyyy-mm-dd HH:MM:SS";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date newDate = sdf.parse(date);
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String dateToString(Date newDate) {
        String dateFormat = "yyyy-mm-dd HH:MM:SS";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String date = sdf.format(newDate);
        return date;
    }

    private static Date addMinutesToJavaUtilDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private static HashMap<String, String> getCertificateInfo(String sCertificate) {
        String certificatelocal = sCertificate;
        X509Certificate certificate = null;
        CertificateFactory cf = null;
        certificatelocal = certificatelocal.replace("-----BEGIN CERTIFICATE-----", "").replace("-----END CERTIFICATE-----", ""); // NEED FOR PEM FORMAT CERT STRING
        byte[] certificateData = Base64.getDecoder().decode(certificatelocal);
        try {
            cf = CertificateFactory.getInstance("X509");
            certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateData));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("email", certificate.getSubjectDN().toString().split(",")[0].split("=")[1]);
            map.put("name", certificate.getSubjectDN().getName().split("CN=")[1].split(",")[0]);
            return map;

        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PublicKey getCertificatePublicKey(String sCertificate) {
        String certificatelocal = sCertificate;
        X509Certificate certificate = null;
        CertificateFactory cf = null;
        certificatelocal = certificatelocal.replace("-----BEGIN CERTIFICATE-----", "").replace("-----END CERTIFICATE-----", ""); // NEED FOR PEM FORMAT CERT STRING
        byte[] certificateData = Base64.getDecoder().decode(certificatelocal);
        try {
            cf = CertificateFactory.getInstance("X509");
            certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateData));
            return certificate.getPublicKey();

        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String certificateToString(X509Certificate newCertificate) {
        StringWriter sWriter = new StringWriter();
        try {
            sWriter.write("-----BEGIN CERTIFICATE-----");
            sWriter.write(DatatypeConverter.printBase64Binary(newCertificate.getEncoded()).replaceAll("(.{64})", "$1"));
            sWriter.write("-----END CERTIFICATE-----");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sWriter.toString();
    }
}
