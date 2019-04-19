/*
 *
 * SHA1DSA
 * SHA1RSA
 * SHA256RSA
 * */

import java.security.*;
import javax.crypto.*;
import java.util.Arrays;

public class MySignature {
    private MyMessageDigest myMessageDigest;
    private KeyPairGenerator keyGen;
    private KeyPair key;
    private Cipher cipher;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private byte[] cipherText;
    private byte[] decryptedMessageDigest;


    public MySignature(String NewAlgorithm){
        this.myMessageDigest =  new MyMessageDigest(NewAlgorithm);
    }

    private void generateKeyPair() throws NoSuchAlgorithmException {
        System.out.println( "Start generating RSA key...\n" );
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(1024);
        this.setKeyPair(this.keyGen.generateKeyPair());
        this.setPrivateKey(this.key.getPrivate());
        System.out.println("Private Key:" + this.getPrivateKey() + " \n");
        this.setPublicKey(this.key.getPublic());
        System.out.println("Public Key:" + this.getPublicKey() + " \n");
        System.out.println( "Finish generating RSA key...\n" );
    }


    public void initSign() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        System.out.println("Generating Key Pair\n");
        this.generateKeyPair();
        this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        this.cipher.init(Cipher.ENCRYPT_MODE,this.getPrivateKey());
        System.out.println("Finish generating Key Pair\n");
    }

    public void update() throws NoSuchAlgorithmException {
        System.out.println("Calculating Message Digest\n");
        this.myMessageDigest.generateMessageDigest(this.myMessageDigest.getMessage());
    }

    public void sign() throws BadPaddingException, IllegalBlockSizeException {
        System.out.println("Signing Message Digest with Private Key\n");
        this.setCipherText(cipher.doFinal(this.myMessageDigest.getDigest()));
        System.out.println("Finish signing Message Digest with Private Key\n");
    }

    public void initVerify() throws InvalidKeyException {
        System.out.println("Initializing Cipher Text Verification\n");
        this.cipher.init(Cipher.DECRYPT_MODE, this.getPublicKey());
    }

    public boolean verify() throws IllegalBlockSizeException, BadPaddingException {
        this.setDecryptedMessageDigest(cipher.doFinal(this.getCipherText()));

        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < this.getDecryptedMessageDigest().length; i++) {
            String hex = Integer.toHexString(0x0100 + (this.getDecryptedMessageDigest()[i] & 0x00FF)).substring(1);
            buf.append((hex.length() < 2 ? "0" : "") + hex);
        }

        String decryptedMessageDigest = buf.toString();

        System.out.println("Comparing...");
        System.out.println("Message Digest: " + this.myMessageDigest.getHexDigest());
        System.out.println("Decrypted Message Digest: " + decryptedMessageDigest);

        System.out.println("\nFinishing Cipher Text Verification\n");
        return Arrays.equals(this.myMessageDigest.getDigest(), this.getDecryptedMessageDigest());
    }


    public void setKeyPair(KeyPair newKeyPair){
        this.key = newKeyPair;
    }

    public KeyPair getKeyPair(){
        return this.key;
    }

    public void setPublicKey(PublicKey newPublicKey){
        this.publicKey = newPublicKey;
    }

    public PublicKey getPublicKey(){
        return this.publicKey;
    }

    public void setPrivateKey(PrivateKey newPrivateKey){
        this.privateKey = newPrivateKey;
    }

    public PrivateKey getPrivateKey(){
        return this.privateKey;
    }

    public void setMessage(byte[] message) {
        this.myMessageDigest.setMessage(message);
    }

    public byte[] getMessage() {
        return this.myMessageDigest.getMessage();
    }

    public void setAlgorithm(String algorithm) {
        this.myMessageDigest.setAlgorithm(algorithm);
    }

    public String getAlgorithm() {
        return this.myMessageDigest.getAlgorithm();
    }

    public void setCipherText(byte[] newCipherText){
        this.cipherText = newCipherText;
    }

    public byte[] getCipherText(){
        return this.cipherText;
    }

    public void setDecryptedMessageDigest(byte[] newDecryptedMessageDigest){
        this.decryptedMessageDigest = newDecryptedMessageDigest;
    }

    public byte[] getDecryptedMessageDigest(){
        return this.decryptedMessageDigest;
    }

}
