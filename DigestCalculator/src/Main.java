import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        // TODO Auto-generated method stub
        System.out.println("\n" + "Pontifícia Universidade Católica do Rio de Janeiro");
        System.out.println("INF1416 - Segurança da Informação");
        System.out.println("Trabalho 2 - DigestCalculator");
        System.out.println("Grupo 4:");
        System.out.println("Fernando Homem da Costa");
        System.out.println("Juliana Dana Zilberberg\n");

        int nEntradas = args.length;

        if (nEntradas < 2) {
            System.out.println("Argumentos da linha de comando insuficientes para a execução do programa!\n");
            System.out.println("\nEstrutura do comando: DigestCalculator Tipo_Digest Caminho_ArqListaDigest Caminho_Arq1... Caminho_ArqN\n");
            System.exit(1);
        } else {
            String typeDigest = args[0];
            String listaDigest = args[1];
            List<String> listaArqN = new ArrayList<String>();

            for(int i = 2; i < (nEntradas); i++ ){
                listaArqN.add(args[i]);
            }

            DigestCalculator calculator = new DigestCalculator(listaDigest, listaArqN, typeDigest);
            calculator.initDigest();
            calculator.CalcuateDigest();
            calculator.CompareAllDigests();

        }

    }

}
