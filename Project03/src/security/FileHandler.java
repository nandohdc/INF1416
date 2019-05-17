package security;

import javax.crypto.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

public class FileHandler {

    private SecureRandom SeedGen (String newSecureRandomAlgorithm, byte[] seed){

        SecureRandom randomGen = null;

        try {
            randomGen = SecureRandom.getInstance(newSecureRandomAlgorithm);
            randomGen.setSeed(seed);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("[ERROR][Class: SecretKeyGen] Secure Random Algorithm not found: " + newSecureRandomAlgorithm);
            System.exit(1);
        }

        return randomGen;
    }

    private byte[] readFile(Path path){
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Key KeyGen(Path path, PrivateKey pvk, String newSecureRandomAlgorithm, String keyGeneratorAlgorithm, int KeySize){

        byte[] fileBytes = this.readFile(path);

        if(fileBytes != null){
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, pvk);
                byte[] seed = cipher.doFinal(fileBytes);

                SecureRandom randomGen = SeedGen(newSecureRandomAlgorithm, seed);
                KeyGenerator keyGenerator = null;

                try {
                    keyGenerator = KeyGenerator.getInstance(keyGeneratorAlgorithm);
                } catch ( NoSuchAlgorithmException e ) {
                    System.err.println("[ERROR][Class: SecretKeyGen] Key Generator Algorithm not found: " + keyGeneratorAlgorithm);
                    System.exit(1);
                }

                keyGenerator.init(KeySize, randomGen);
                Key newSecretKey = keyGenerator.generateKey();

                return newSecretKey;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    public byte[] getFileContent(String filePath, Key secretKey) {
        Path path = Paths.get(filePath + ".enc");

        byte[] fileBytes = this.readFile(path);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return cipher.doFinal(fileBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
