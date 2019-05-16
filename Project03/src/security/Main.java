package security;

import com.sun.tools.javac.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.cert.CertificateEncodingException;
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

        file = new File("Tests/passwords.txt");
        ArrayList<String> password = new ArrayList<String>();
        br = new BufferedReader(new FileReader(file));

        st = null;

        while ((st = br.readLine()) != null) {
            password.add(st);
        }

        FileInputStream inStream = new FileInputStream("Keys/user01-x509.crt");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);

        System.out.println((cert.getSubjectDN().toString().split(",")[0].split("=")[1]).trim());

        Authencation auth  = new Authencation();

        if(!auth.FirstValidation((cert.getSubjectDN().toString().split(",")[0].split("=")[1]).trim(), emails.get(0))){
            System.out.println("O e-mail fornecido está incorreto.");
        } else {

            if(false){//Se o usuário estiver bloqueado.
                System.out.println("O usuário está bloqueado.");
            } else {//E-mail é válido e o usuário não está bloqueado.
               //ir para segunda etapa.
            }
        }

        if(!auth.SecondValidation(password,"c68350d110fd9e12d89d6e843ef78a5fea668643")){
            System.out.println(auth.SecondValidation(password,"c68350d110fd9e12d89d6e843ef78a5fea668643"));
            if(false){//se o contador de senha for menor que 3
                System.out.println("O Senha fornecida está incorreta.");
                System.out.println("Faltam contador-1 tentativas");
                //inserir contador de erro de senhas
            } else {
                System.out.println("Usuário bloqueado.");
                //voltar para primeira tela de login - email.
            }

        } else {//A senha está correta, devemos zerar o contador de erros e seguir para 3 etapa
            //contador_de_erros = 0;

        }





    }

}
