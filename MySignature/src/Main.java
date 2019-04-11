import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // TODO Auto-generated method stub
        System.out.println("\n" + "Pontifícia Universidade Católica do Rio de Janeiro");
        System.out.println("INF1416 - Segurança da Informação");
        System.out.println("Grupo 4:");
        System.out.println("Fernando Homem da Costa");
        System.out.println("Juliana Dana Zilberberg\n");

        if (args.length > 2) {
            System.err.println("Usage: java MySignature text");
            System.exit(1);
        }

        try {
            byte[] plainText = args[0].getBytes("UTF8");
            String [] splitted = args[1].split("with");
            String algorithm = splitted[0];
            String method = splitted[1];

            if(!method.equals("RSA")){
                System.out.println("O único método suportado é o RSA!");
                System.exit(1);
            } else{

                MySignature signature = new MySignature(plainText, algorithm);

                signature.initSign();
                signature.update();
                signature.sign();
                signature.initVerify();

                if (signature.verify()) {
                    System.out.println("Signature verified");
                }
                else {
                    System.out.println("Signature failed");
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
