package security;


import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws IOException, CertificateException {

        File file = new File("Tests/fake_email_list.txt");
        ArrayList<String> emails = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        while ((st = br.readLine()) != null) {
            emails.add(st);
        }

        FileInputStream inStream = new FileInputStream("Keys/user01-x509.crt");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);

        System.out.println((cert.getSubjectDN().toString().split(",")[0].split("=")[1]).trim());

        file = new File("Tests/passwords.txt");
        ArrayList<String> password = new ArrayList<String>();
        br = new BufferedReader(new FileReader(file));


        while ((st = br.readLine()) != null) {
            password.add(st);
        }


        Authencation auth  = new Authencation();

        if(!auth.FirstValidation((cert.getSubjectDN().toString().split(",")[0].split("=")[1]).trim(), emails.get(0))){
            System.out.println("O e-mail fornecido está incorreto.");
        } else {

            if(false){//Se o usuário estiver bloqueado.
                System.out.println("O usuário está bloqueado.");
            } else {//E-mail é válido e o usuário não está bloqueado.
                if(!auth.SecondValidation(password,"nmg6Tg1kr3", "d55b0b6d862323c1f72d65552b3514716393a403")){
                    if(auth.getPasswordsAttempts() < 3){//se o contador de senha for menor que 3
                        System.out.println("O Senha fornecida está incorreta.");

                        auth.setPasswordsAttempts(auth.getPasswordsAttempts()+1);

                        System.out.println("Faltam " +  (3 - auth.getPasswordsAttempts())  +" tentativas");
                    } else {
                        System.out.println("Usuário bloqueado.");
                        //voltar para primeira tela de login - email.
                    }

                } else {//A senha está correta, devemos zerar o contador de erros e seguir para 3 etapa
                    System.out.println("A senha está correta!");

                    auth.setPasswordsAttempts(0);

                    if(!auth.ThirdValidation("user01", "Keys/user01-pkcs8-des.pem", cert.getPublicKey())){
                        //Se a verificação for negativa, o usuário deve ser apropriadamente avisado e
                        // o processo deve contabilizar um erro de verificação da chave privada,
                        // retornando para o início da terceira etapa

                        if (auth.getPrivateKeyAttempts() < 3){// Se o contador de erros de chave privada for menor que 3
                            System.out.println("A Chave Privada está incorreta!");
                            auth.setPrivateKeyAttempts(auth.getPrivateKeyAttempts()+1);
                            System.out.println("Faltam " +  (3 - auth.getPrivateKeyAttempts())  +" tentativas");

                        } else {
                            //deve-se seguir para a primeira etapa e o acesso do usuário deve ser bloqueado por 2 minutos
                            System.out.println("Usuário bloqueado.");

                        }

                    } else{// Se a verificação for positiva, o processo deve permitir acesso ao sistema.
                        //Entrar no sistema;
                        System.out.println("A Chave Privada está correta!");

                        auth.setPrivateKeyAttempts(0);

                    }
                }
            }
        }

    }

}
