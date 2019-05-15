package security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class SecretKeyGen {

    private String encode = null;
    private String secret = null;

    public SecretKeyGen(String newSecret){
        this.setSecret(newSecret);
        this.setEncode("us-ascii");
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }


    public String getEncode() {
        return this.encode;
    }

    public String getSecret() {
        return this.secret;
    }

    private SecureRandom SeedGen (String newSecureRandomAlgorithm, String newProvider){

        SecureRandom randomGen = null;

        try {
            randomGen = SecureRandom.getInstance(newSecureRandomAlgorithm, newProvider);
            randomGen.setSeed(this.getSecret().getBytes(this.getEncode()));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[ERROR][Class: SecretKeyGen] Secure Random Algorithm not found: " + newSecureRandomAlgorithm);
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.err.println("[ERROR][Class: SecretKeyGen] Char-encoding not found: " + this.getEncode());
            System.exit(1);
        } catch (NoSuchProviderException e) {
            System.err.println("[ERROR][Class: SecretKeyGen] Provider not found: " + newProvider);
            System.exit(1);
        }
        return randomGen;
    }

    public SecretKey KeyGen(String newSecureRandomAlgorithm, String newProvider, String keyGeneratorAlgorithm, int KeySize){
        SecureRandom randomGen = SeedGen(newSecureRandomAlgorithm, newProvider);
        KeyGenerator keyGenerator = null;

        try {
            keyGenerator = KeyGenerator.getInstance(keyGeneratorAlgorithm);
        } catch ( NoSuchAlgorithmException e ) {
            System.err.println("[ERROR][Class: SecretKeyGen] Key Generator Algorithm not found: " + keyGeneratorAlgorithm);
            System.exit(1);
        }

        keyGenerator.init(KeySize, randomGen);
        SecretKey newSecretKey = keyGenerator.generateKey();

        return newSecretKey;
    }

}
