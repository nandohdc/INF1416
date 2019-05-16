package security;

import javax.crypto.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Authencation {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private int passwordsAttempts;
    private int privateKeyAttempts;

    public Authencation(){
        this.setPrivateKey(null);
        this.setPublicKey(null);

        this.setPasswordsAttempts(0);
        this.setPrivateKeyAttempts(0);

    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }



    public int getPasswordsAttempts() {
        return passwordsAttempts;
    }

    public void setPasswordsAttempts(int passwordsAttempts) {
        this.passwordsAttempts = passwordsAttempts;
    }

    public int getPrivateKeyAttempts() {
        return privateKeyAttempts;
    }

    public void setPrivateKeyAttempts(int privateKeyAttempts) {
        this.privateKeyAttempts = privateKeyAttempts;
    }


    private boolean emailAuthencation(String userEmail, String newDBEmail){

        if(!userEmail.equals(newDBEmail)){
            return false;
        }
        return true;
    }


    public boolean FirstValidation(String emailCertificate, String newUserEmail){
        if(!this.emailAuthencation(newUserEmail, emailCertificate)){
            System.out.println("[First Validation] NOT OK -  emailCertificate: " + emailCertificate + " " + "newUserEmail: " +  newUserEmail);
            return false;

        } else {
            System.out.println("[First Validation] OK -  emailCertificate: " + emailCertificate + " " + "newUserEmail: " + newUserEmail);
            return true;
        }
    }

    private String CalculateHexHashPassword(String newPassword, String newSalt){
        MessageDigest SHA1 = null;
        byte[] digestPassword = null;

        try {
            SHA1 = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[ERROR][Class: Authencation] Message Digest Algorithm not found: " + "SHA1");
            System.exit(1);
        }

        SHA1.update((newPassword + newSalt).getBytes());
        digestPassword = SHA1.digest();

        if(digestPassword != null){
            StringBuffer buf = new StringBuffer();

            for (int i = 0; i < digestPassword.length; i++) {
                String hex = Integer.toHexString(0x0100 + (digestPassword[i] & 0x00FF)).substring(1);
                buf.append((hex.length() < 2 ? "0" : "") + hex);
            }
            return buf.toString();
        }
        return null;
    }

    public boolean SecondValidation(List<String> passwordsCombinations, String SaltDBPassword,String HashDBPassword){
        Map<String,String> HashMapPassowrds = new HashMap<String,String>();

        if(SaltDBPassword == null){
            System.err.println("[ERROR][Class: Authencation] O Salt Fornecido possui valor nulo!");
            return false;
        }

        if(SaltDBPassword.length() < 10){
            System.err.println("[ERROR][Class: Authencation] O Salt Fornecido não tem o tamanho correto! " + "Tamanho: " + SaltDBPassword.length());
            return false;
        }

        if(passwordsCombinations.size() == 0){
            System.err.println("[ERROR][Class: Authencation] A lista de password está vazia! " + "Tamanho: " + passwordsCombinations.size());
            return false;
        }

        for (String password : passwordsCombinations) {
            HashMapPassowrds.put(this.CalculateHexHashPassword(password, SaltDBPassword), password);
        }

        if(!HashMapPassowrds.containsKey(HashDBPassword)){
            System.out.println("[Second Validation] NOT OK -> Hash not found -  HashCalculated: " + HashMapPassowrds.containsKey(HashDBPassword) + " " + "HashDBPassword: " + HashDBPassword);
            return false;
        }
        System.out.println("[Second Validation] OK -  HashCalculated: " + HashMapPassowrds.containsKey(HashDBPassword)+ " " + "HashDBPassword: " + HashDBPassword);
        return true;
    }

    private byte[] readPEMFromFile(String newPath, Cipher cipher){
        byte[] cipherPemBytes = new byte[0];

        try {
            cipherPemBytes = Files.readAllBytes(Paths.get(newPath));
        } catch (IOException e) {
            System.err.println("[ERROR][Class: Authencation] File not found: " + newPath);
            System.exit(1);
        }

        try {
            byte[] pemB = cipher.doFinal(cipherPemBytes);
            return pemB;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("[ERROR][Class: Authencation] Bad Padding!" );
            return null;
        }

        return null;
    }

    private KeyFactory getkeyFactory(String algorithm){
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[ERROR][Class: Authencation] Algorithm not found: " + algorithm);
            System.exit(1);
        }
        return null;
    }

    private PrivateKey keyFactoryGeneratePrivate (KeyFactory newKeyFactory, PKCS8EncodedKeySpec newPkcs8EncodedKeySpec){
        if(newKeyFactory != null){
            try {
                PrivateKey pvk = newKeyFactory.generatePrivate(newPkcs8EncodedKeySpec);
                return pvk;
            } catch (InvalidKeySpecException e) {
                System.err.println("[ERROR][Class: Authencation] Invalid Key: " + newPkcs8EncodedKeySpec.toString());
                System.exit(1);
            }
        }
        return null;
    }

    private KeyGenerator getKeyGen (String algorithm){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            return keyGen;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[ERROR][Class: Authencation] Algorithm not found: " + algorithm);
            System.exit(1);
        }

        return null;
    }

    private SecureRandom getSecureRandom (String algorithm) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(algorithm);
            return secureRandom;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[ERROR][Class: Authencation] Algorithm not found: " + algorithm);
            System.exit(1);
        }
        return null;
    }

    private PrivateKey getprivateKey(String newSecret, String PathToCertificate){

        KeyGenerator keyGen = this.getKeyGen("DES");

        if(keyGen != null){

            SecureRandom secureRandom = this.getSecureRandom("SHA1PRNG");
            secureRandom.setSeed(newSecret.getBytes());
            keyGen.init(56, secureRandom);

            Key key = keyGen.generateKey();

            if(key !=  null) {
                Cipher cipher = null;
                try {
                    cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("[ERROR][Class: Authencation] Algorithm not found: " + "DES");
                    System.exit(1);
                } catch (NoSuchPaddingException e) {
                    System.err.println("[ERROR][Class: Authencation] Padding not found: " + "PKCS5Padding");
                    System.exit(1);
                }
                try {
                    cipher.init(Cipher.DECRYPT_MODE, key);
                } catch (InvalidKeyException e) {
                    System.err.println("[ERROR][Class: Authencation] Invalid Key: " + key.toString());
                    System.exit(1);
                }

                byte[] pemFile = this.readPEMFromFile(PathToCertificate, cipher);

                if (pemFile != null) {
                    String pem64 = new String(pemFile);
                    pem64 = pem64.replace("-----BEGIN PRIVATE KEY-----\n", "");
                    pem64 = pem64.replace("-----END PRIVATE KEY-----\n", "");

                    byte[] pem = Base64.getMimeDecoder().decode(pem64);

                    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(pem);

                    KeyFactory keyFactory = this.getkeyFactory("RSA");

                    PrivateKey privateKey = this.keyFactoryGeneratePrivate(keyFactory, pkcs8EncodedKeySpec);

                    return privateKey;

                } else {

                    return null;
                }
            }

            else{

                return null;
            }
        }

        return null;
    }


    private boolean SignRandomMessage(String algorithm){
        SecureRandom random = new SecureRandom();
        byte[] msg = new byte[2048];

        random.nextBytes(msg);

        Signature sign = null;

        try {

            sign = Signature.getInstance(algorithm);
            sign.initSign(this.getPrivateKey());
            sign.update(msg);

            byte[] cipherMessage = sign.sign();

            sign.initVerify(this.getPublicKey());
            sign.update(msg);

            if(sign.verify(cipherMessage)) {
                return true;
            } else{
                return false;
            }

        } catch (NoSuchAlgorithmException e) {
            System.err.println("[ERROR][Class: Authencation] Algorithm not found: " + algorithm);
            System.exit(1);
        } catch (InvalidKeyException e) {
            System.err.println("[ERROR][Class: Authencation] Invalid Key: " );
            System.exit(1);
        } catch (SignatureException e) {
            System.err.println("[ERROR][Class: Authencation] Invalid Key: " );
            System.exit(1);
        }

        return false;
    }

    public boolean ThirdValidation(String newSecret, String PathToPEM, PublicKey pbk){

        if(newSecret == null){
            System.err.println("[ERROR][Class: Authencation] Invalid secret!" );
            return false;
        }

        if(PathToPEM == null){
            System.err.println("[ERROR][Class: Authencation] Invalid PathToPEM!" );
            return false;
        }

        this.setPrivateKey(this.getprivateKey(newSecret, PathToPEM));

        if(this.getPrivateKey()!=null){
            this.setPublicKey(pbk);//this.getCertificatePublicKey(certificate)
            if(this.getPublicKey() != null){
                if(this.SignRandomMessage("MD5withRSA")){
                    System.out.println("[Third Validation] OK - Signature verified!\n" + "Private Key: " + this.getPrivateKey() + "\n" + "Public Key: " + this.getPublicKey());
                    return true;
                } else{
                    System.out.println("[Third Validation] NOT OK - Signature failed!");
                    return false;
                }
            }
            else{
                System.out.println("[Third Validation] NOT OK - Invalid Public Key!");
                return false;
            }
        }
        System.out.println("[Third Validation] NOT OK - Invalid Private Key!");
        return false;
    }
}
