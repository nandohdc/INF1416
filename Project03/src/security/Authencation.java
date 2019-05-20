package security;

import model.RegistryDAO;
import model.UserDAO;

import javax.crypto.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;


public class Authencation {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private byte[] skey;
    private int userAttempts;

    private String sd;

    public Authencation(){
        this.setPrivateKey(null);
        this.setPublicKey(null);
        this.setSKey(null);

        this.setUserAttempts(0);

        this.setSd(null);

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

    public byte[] getSKey() {
        return skey;
    }

    public void setSKey(byte[] secreytkey) {
        this.skey = secreytkey;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }


    public int getUserAttempts(){
        return this.userAttempts;
    }

    public void setUserAttempts(int newUserAttempts){
        this.userAttempts = newUserAttempts;
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

    private static boolean PassowrdValidation(String newPassword){
        if (!newPassword.matches("[0-9]+")|| newPassword.length() < 6 || newPassword.length() > 8){
            return false;
        } else {
            boolean crescente = true;
            boolean decrescente = true;

            for (int i = 0; i < newPassword.length() - 1; i++) {
                char c = newPassword.charAt(i);
                char cProx = newPassword.charAt(i+1);

                if (Character.getNumericValue(cProx) != Character.getNumericValue(c) + 1) {
                    crescente = false;
                }
                if (Character.getNumericValue(cProx) != Character.getNumericValue(c) - 1) {
                    decrescente = false;
                }
                if (cProx == c ) {
                    return false;
                }
            }
            return (!crescente) && (!decrescente);
        }
    }

    public static String TesteCalculateHexHashPassword(String newPassword, String newSalt){

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

    public static String CalculateHexHashPassword(String newPassword, String newSalt){

        if(!PassowrdValidation(newPassword)){
            return null;
        }

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
            HashMapPassowrds.put(TesteCalculateHexHashPassword(password, SaltDBPassword), password);
        }

        if(!HashMapPassowrds.containsKey(HashDBPassword)){
            System.out.println("[Second Validation] NOT OK -> Hash not found -  HashCalculated: " + HashMapPassowrds.containsKey(HashDBPassword) + " " + "HashDBPassword: " + HashDBPassword);
            return false;
        }
        System.out.println("[Second Validation] OK -  HashCalculated: " + HashMapPassowrds.containsKey(HashDBPassword)+ " " + "HashDBPassword: " + HashDBPassword);
        return true;
    }

    private byte[] readPEMFromFile(String newPath, Cipher cipher) throws IOException{
        byte[] cipherPemBytes = new byte[0];

        cipherPemBytes = Files.readAllBytes(Paths.get(newPath));

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

    private PrivateKey getprivateKey(String newSecret, String PathToCertificate) throws IOException {

        KeyGenerator keyGen = this.getKeyGen("DES");

        if (keyGen != null) {

            SecureRandom secureRandom = this.getSecureRandom("SHA1PRNG");
            secureRandom.setSeed(newSecret.getBytes());
            keyGen.init(56, secureRandom);

            Key key = keyGen.generateKey();

            if (key != null) {
                Cipher cipher = null;
                try {
                    cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                    cipher.init(Cipher.DECRYPT_MODE, key);
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
                    }

                } catch (NoSuchPaddingException e) {
                    System.err.println("[ERROR][Class: Authencation] Padding not found: " + "PKCS5Padding");
                    System.exit(1);
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("[ERROR][Class: Authencation] Algorithm not found: " + "DES");
                    System.exit(1);
                } catch (InvalidKeyException e) {
                    System.err.println("[ERROR][Class: Authencation] Invalid Key: " + key.toString());
                    System.exit(1);
                }

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

    public boolean ThirdValidation(String newSecret, String PathToPEM, PublicKey pbk, UserDAO userDAO){

        if(newSecret == null){
            System.err.println("[ERROR][Class: Authencation] Invalid secret!" );
            return false;
        }

        if(PathToPEM == null){
            new RegistryDAO(4004, userDAO, null, null).save();
            System.err.println("[ERROR][Class: Authencation] Invalid PathToPEM!" );
            return false;
        }
        try {
            this.setPrivateKey(this.getprivateKey(newSecret, PathToPEM));
        } catch (IOException e){
            new RegistryDAO(4004, userDAO, null, null).save();
        }

        if(this.getPrivateKey()!=null){
            this.setPublicKey(pbk);//this.getCertificatePublicKey(certificate)
            if(this.getPublicKey() != null){
                if(this.SignRandomMessage("MD5withRSA")){
                    System.out.println("[Third Validation] OK - Signature verified!\n" + "Private Key: " + this.getPrivateKey() + "\n" + "Public Key: " + this.getPublicKey());
                    return true;
                } else{
                    new RegistryDAO(4006, userDAO, null, null).save();
                    System.out.println("[Third Validation] NOT OK - Signature failed!");
                    return false;
                }
            }
            else{
                System.out.println("[Third Validation] NOT OK - Invalid Public Key!");
                return false;
            }
        }
        new RegistryDAO(4005, userDAO, null, null).save();
        System.out.println("[Third Validation] NOT OK - Invalid Private Key!");
        return false;
    }

    public byte[] readFile(Path path){
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


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

    private byte[] getSecretKey(String FilePath){
        Path path = Paths.get(FilePath + ".env");
        byte[] fileBytes = this.readFile(path);

        if(fileBytes != null && this.getPrivateKey()!= null){
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey());
                cipher.update(fileBytes);
                byte [] seed = cipher.doFinal();
                this.setSd(new String(seed));

                return seed;

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private byte[] getFileContent(String filePath) {
        Path path = Paths.get(filePath + ".enc");

        byte[] fileBytes = this.readFile(path);

        if (fileBytes != null) {
            KeyGenerator keyGen = this.getKeyGen("DES");

            if (keyGen != null) {

                SecureRandom secureRandom = this.getSecureRandom("SHA1PRNG");
                secureRandom.setSeed(this.getSKey());
                keyGen.init(56, secureRandom);

                Key key = keyGen.generateKey();

                if (key != null) {
                    Cipher cipher = null;
                    try {
                        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                        cipher.init(Cipher.DECRYPT_MODE, key);

                        byte[] encryptedFile = Files.readAllBytes(path);

                        byte[] fileContent = cipher.doFinal(encryptedFile);

                        return fileContent;

                    } catch (NoSuchPaddingException e) {
                        System.err.println("[ERROR][Class: Authencation] Padding not found: " + "PKCS5Padding");
                        System.exit(1);
                    } catch (NoSuchAlgorithmException e) {
                        System.err.println("[ERROR][Class: Authencation] Algorithm not found: " + "DES");
                        System.exit(1);
                    } catch (InvalidKeyException e) {
                        System.err.println("[ERROR][Class: Authencation] Invalid Key: " + key.toString());
                        System.exit(1);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        return null;
    }

    private boolean SignatureVerification (String filePath, byte[] fileContent){
        Path path = Paths.get(filePath + ".asd");

        byte[] fileSignature = this.readFile(path);

        if (fileSignature != null) {
            Signature signature = null;
            try {
                signature = Signature.getInstance("MD5withRSA");
                signature.initVerify(this.getPublicKey());
                signature.update(fileContent);

                return signature.verify(fileSignature);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public byte[] VerifyFile(String filePath) {

        this.setSKey(this.getSecretKey(filePath));

        if (this.getSKey() != null) {
            byte[] fileContent = getFileContent(filePath);

            if (fileContent != null && this.getPublicKey() != null) {

                if (!SignatureVerification(filePath, fileContent)) {
                    return null;
                }
                return fileContent;
            }
            System.out.println("[Error][Authentication] File is null");
            return null;
        }
        return null;
    }

    public static String SaltGen(){
        String candidateChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        SecureRandom rand = new SecureRandom();
        StringBuffer salt = new StringBuffer(10);

        for (int i = 0; i < 10; i++) {
            salt.append(candidateChars.charAt(rand.nextInt(candidateChars.length())));
        }

        return salt.toString();
    }
}