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
        new RegistryDAO(1001, null, null, null).save();
        mainFrame = MainFrame.getInstance();
        loadLoginView();
//        loadAdminView(UserDAO.get("admin@inf1416.puc-rio.br"));
    }

    private static void loadLoginView() {
        new RegistryDAO(2001, null, null, null).save();
        mainFrame.setLogin(strings -> {
            UserDAO user = UserDAO.get(strings.get(0));
            if (user == null) {
                new RegistryDAO(2005, null, null, null).save();
                mainFrame.showError("Usuario não existe");
            } else {
                if (user.isBlocked()) {
                    Date current = addMinutesToJavaUtilDate(stringToDate(user.getTimeBlocked()), 2);
                    if (current.getTime() > new Date().getTime()) {
                        new RegistryDAO(2004, user, null, null).save();
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
                    new RegistryDAO(2003, user, null, null).save();
                    new RegistryDAO(2002, user, null, null).save();
                    loadPasswordView(user);
                }
            }
        });
    }

    private static void loadPasswordView(UserDAO user) {
        new RegistryDAO(3001, user, null, null).save();
        mainFrame.setPassword(strings -> {
            if (!auth.SecondValidation(strings, user.getSalt(), user.getPassword())) {
                mainFrame.showError("O Senha fornecida está incorreta.");
                user.setAttempt(user.getAttempt() + 1);
                user.update();
                new RegistryDAO(3003 + user.getAttempt(), user, null, null).save();
                if (3 - user.getAttempt() > 0) {//se o contador de senha for menor que 3
                    System.out.println("Faltam " + (3 - user.getAttempt()) + " tentativas");
                    loadPasswordView(user);
                } else {
                    new RegistryDAO(3007, user, null, null).save();
                    mainFrame.showError("Usuário bloqueado.");
                    user.setBlocked(true);
                    user.setTimeBlocked(dateToString(new Date()));
                    user.update();
                    loadLoginView();//voltar para primeira tela de login - email.
                }
            } else {
                new RegistryDAO(3003, user, null, null).save();
                new RegistryDAO(3002, user, null, null).save();
                System.out.println("A senha está correta");
                user.setAttempt(0);
                user.update();
                loadSecretKeyView(user);
            }
        });
    }

    private static void loadSecretKeyView(UserDAO user) {
        new RegistryDAO(4001, user, null, null).save();
        mainFrame.setSecretKey(strings -> {
            if (user.getCertificate() != null && !user.getCertificate().trim().isEmpty()) {

                if (!auth.ThirdValidation(strings.get(0), "Keys/user01-pkcs8-des.pem", getCertificatePublicKey(user.getCertificate()), user)) {
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
                        new RegistryDAO(4007, user, null, null).save();
                        mainFrame.showError("Usuário bloqueado.");
                        user.setBlocked(true);
                        user.setTimeBlocked(dateToString(new Date()));
                        user.update();
                        loadLoginView();//voltar para primeira tela de login - email.
                    }

                } else {// Se a verificação for positiva, o processo deve permitir acesso ao sistema.
                    //Entrar no sistema;
                    new RegistryDAO(4003, user, null, null).save();
                    new RegistryDAO(4002, user, null, null).save();
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
        new RegistryDAO(5001, userDAO, null, null).save();
        mainFrame.setMainScreenAdmin(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalAccess(),
                strings -> {
                    if (strings.get(0).equals("register")) {
                        new RegistryDAO(5002, userDAO, null, null).save();
                        loadRegisterView(userDAO);
                    }
                    else if (strings.get(0).equals("update")) {
                        new RegistryDAO(5003, userDAO, null, null).save();
                        loadUpdateView(userDAO);
                    }
                    else if (strings.get(0).equals("exit")) {
                        new RegistryDAO(5004, userDAO, null, null).save();
                        loadExitView(userDAO);
                    }
                    else if (strings.get(0).equals("files")) {
                        new RegistryDAO(5005, userDAO, null, null).save();
                        loadFileView(userDAO);
                    }
                });
    }

    private static void loadUserView(UserDAO userDAO) {

    }

    private static void loadFileView(UserDAO userDAO) {
        new RegistryDAO(8001, userDAO, null, null).save();
        String[][] strings = new String[new String(auth.VerifyFile("Files/index")).split("\n").length][];
        String[] localsplit = new String(auth.VerifyFile("Files/index")).split("\n");

        int i = 0;

        for(String j: localsplit){
            strings[i] = j.split(" ");
            i++;
        }

        mainFrame.setFiles(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalQuery(), strings, strings1 -> {

            if (strings1.get(0).equals("back")) {
                new RegistryDAO(8002, userDAO, null, null).save();
                loadAdminView(userDAO);
            } else if(strings1.get(0).equals("list")){
/*
                String[][] strings = new String[new String(auth.VerifyFile(strings1.get(1)+"index")).split("\n").length][];
                String[] localsplit = new String(auth.VerifyFile(strings1.get(1))+"index").split("\n");

                int i = 0;

                for(String j: localsplit){
                    strings[i] = j.split(" ");
                    i++;
                }*/

            } else {
                int index = Integer.valueOf(strings1.get(1));
                try {
                    createFile(auth.VerifyFile(strings1.get(2) + "/" + strings[index][0]), strings1.get(2) + "/" + strings[index][1]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                new RegistryDAO(8003, userDAO, null, null).save();
            }
        });
    }

    private static void loadRegisterView(UserDAO userDAO) {
        new RegistryDAO(6001, userDAO, null, null).save();
        mainFrame.setRegister(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), 10, strings -> {
            if(strings.get(0).equals("back")){
                new RegistryDAO(6007, userDAO, null, null).save();
                loadAdminView(userDAO);
            } else {
                new RegistryDAO(6002, userDAO, null, null).save();
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

                    start = subjectDN.indexOf("=", end);
                    end = subjectDN.indexOf(",", start);
                    String name = subjectDN.substring(start + 1, end);

                    String sCertificate = certificateToString(cert);

                    String salt = auth.SaltGen();

                    if(!strings.get(3).equals(strings.get(4))){
                        new RegistryDAO(6003, userDAO, null, null).save();
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
                        new RegistryDAO(6003, userDAO, null, null).save();
                        mainFrame.showError("A senha não está de acordo com a política de senhas.");
                        return;
                    } else{
                        UserDAO newUser = new UserDAO(email,password,salt,false,null, 0,0,sCertificate,0,gp);
                        String issuer = cert.getIssuerX500Principal().toString().split("=")[1].split(",")[0];
                        String serial = cert.getSerialNumber().toString();
                        String version = String.valueOf(cert.getVersion());
                        String after = cert.getNotAfter().toString();
                        String before = cert.getNotBefore().toString();
                        String alg = cert.getSigAlgName();
                        mainFrame.setConfirmDialog(String.format("versao: %s\nserie: %s\nnot after: %s\nnot before: %s\nalgoritmo: %s\nemissor: %s\nnome: %s\nemail: %s", version, serial, after, before, alg, issuer, name, email), strings1 -> {
                            if(strings1.get(0).equals("yes")){
                                new RegistryDAO(6005, userDAO, null, null).save();
                                if(UserDAO.get(email) == null) {
                                    newUser.save();
                                    loadRegisterView(userDAO);
                                } else {
                                    new RegistryDAO(6004, userDAO, null, null).save();
                                    mainFrame.showError("Usuario com email ja cadastrado");
                                }
                            } else
                                new RegistryDAO(6006, userDAO, null, null).save();
                        });
                    }

                } catch (CertificateException e) {
                    new RegistryDAO(6004, userDAO, null, null).save();
                    e.printStackTrace();
                } catch (IOException e) {
                    new RegistryDAO(6004, userDAO, null, null).save();
                    e.printStackTrace();
                }
            }
        });
    }

    private static void loadUpdateView(UserDAO userDAO) {
        new RegistryDAO(7001, userDAO, null, null).save();
        mainFrame.setUpdate(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalAccess(), strings -> {
            new RegistryDAO(7006, userDAO, null, null).save();
            String sCertificate = userDAO.getCertificate();
            if(strings.get(0) != null && !strings.get(0).isEmpty()){
                Path cfPath = Paths.get(strings.get(0));
                byte[] fileBytes = auth.readFile(cfPath);

                InputStream stream = new ByteArrayInputStream(fileBytes);
                CertificateFactory factory = null;
                try {
                    factory = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate) factory.generateCertificate(stream);
                    stream.close();

                    sCertificate = certificateToString(cert);
                } catch (CertificateException e) {
                    new RegistryDAO(7003, userDAO, null, null).save();
                    e.printStackTrace();
                } catch (IOException e) {
                    new RegistryDAO(7003, userDAO, null, null).save();
                    e.printStackTrace();
                }
            }
            if(strings.get(1) != null && !strings.get(1).isEmpty()){
                String salt = auth.SaltGen();

                if(!strings.get(1).equals(strings.get(2))){
                    new RegistryDAO(7002, userDAO, null, null).save();
                    mainFrame.showError("As senhas digitadas não são iguais");
                    return;
                }

                String password = auth.CalculateHexHashPassword(strings.get(1), salt);

                if(password == null) {
                    new RegistryDAO(7002, userDAO, null, null).save();
                    mainFrame.showError("A senha não está de acordo com a política de senhas.");
                    return;
                }
                userDAO.setPassword(password);
                userDAO.setSalt(salt);
            }
            userDAO.setCertificate(sCertificate);
            userDAO.update();
            if(userDAO.getGroup().getName().equals("Administrador"))
                loadAdminView(userDAO);
            else
                loadUserView(userDAO);

        });
    }

    private static void loadExitView(UserDAO userDAO) {
        new RegistryDAO(9001, userDAO, null, null).save();
        mainFrame.setExit(getCertificateInfo(userDAO.getCertificate()).get("name"), userDAO.getGroup().getName(), userDAO.getLoginName(), userDAO.getTotalAccess(), strings -> {
            if(strings.get(0).equals("back")){
                new RegistryDAO(9004, userDAO, null, null).save();
                if(userDAO.getGroup().getName().equals("Administrador"))
                    loadAdminView(userDAO);
                else loadUserView(userDAO);
            } else {
                new RegistryDAO(9003, userDAO, null, null).save();
                new RegistryDAO(1002, null, null, null).save();
                System.exit(1);
            }
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

    private static boolean createFile(byte[] fileContent, String filename) throws FileNotFoundException {

        try (FileOutputStream stream = new FileOutputStream(filename)) {
            stream.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }

}
