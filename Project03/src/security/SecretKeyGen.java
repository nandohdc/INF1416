package security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SecretKeyGen {

    private String encode = null;
    private String secret = null;
    private X509Certificate x509certificate;

    public SecretKeyGen(String newSecret){
        this.setSecret(newSecret);
        this.setEncode("us-ascii");
        this.setX509certificate(null);
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

    public X509Certificate getcertificate() {
        return x509certificate;
    }

    public void setX509certificate(X509Certificate x509certificate) {
        this.x509certificate = x509certificate;
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



    private X509Certificate getX509Certificate (byte[] byteCertificate){
        CertificateFactory certificateFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream certificateInputStream = new ByteArrayInputStream(byteCertificate);
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);
            return x509Certificate;

        } catch (CertificateException e) {
            System.err.println("[ERROR][Class: Authencation] x509Certificate not found: " + byteCertificate.toString());
            System.exit(1);
        }

        return null;
    }

    public boolean passwordAuthencation(String userPassword, String dbPassword){
        if(!userPassword.equals(dbPassword)){
            return false;
        }
        return true;
    }

    private PublicKey getCertificatePublicKey (byte[] certificate){

        CertificateFactory certificateFactory = null;
        try {

            certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream certificateInputStream = new ByteArrayInputStream(certificate);
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);
            return x509Certificate.getPublicKey();

        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }

}
