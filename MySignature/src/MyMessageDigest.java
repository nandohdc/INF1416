import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyMessageDigest {
    private MessageDigest messageDigest;
    private byte[] message;
    private String algorithm;
    private int length;
    private byte[] digest;
    private String hexDigest;

    protected MyMessageDigest(byte[] NewMessage, String NewAlgorithm){
        this.setMessage(NewMessage);
        this.setAlgorithm(NewAlgorithm);
    }

    private void selectMessageDigest() throws NoSuchAlgorithmException {
        String[] options = {"MD5", "SHA1", "SHA-256"};

        for(int i = 0; i < options.length; i++) {
            if(this.getAlgorithm().equals(options[i])) {
                this.setMessageDigest(MessageDigest.getInstance(options[i]));
                System.out.println(this.getMessageDigest().getProvider().getInfo() + "\n");
                return;
            }
        }

        System.out.println("Algoritmo selecionado não está disponível.\n");
    }

    protected byte[] generateMessageDigest() throws NoSuchAlgorithmException {
        this.selectMessageDigest();
        this.getMessageDigest().update(message);
        digest = this.getMessageDigest().digest();
        this.setLength(digest.length);
        this.convertToHex();
        return digest;
    }

    private void convertToHex() {
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < this.getLength(); i++) {
            String hex = Integer.toHexString(0x0100 + (this.getDigest()[i] & 0x00FF)).substring(1);
            buf.append((hex.length() < 2 ? "0" : "") + hex);
        }

        this.setHexDigest(buf.toString());

        System.out.println( "Digest length: " + this.length * 8 + "bits" + "\n" );

        // imprime o digest em hexadecimal
        System.out.println( "Digest(hex): " + this.getHexDigest() + "\n");

    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public MessageDigest getMessageDigest() {
        return messageDigest;
    }

    public void setMessageDigest(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    public int getLength() {
        //System.out.println( "\nDigest length: " + this.length * 8 + "bits" );
        return length;
    }

    public void setLength(int length) {
        this.length = length; //Multiplicar por 8 para converter pra bits
    }

    public void setDigest(byte [] NewDigest){
        this.digest = NewDigest;
    }

    public byte [] getDigest(){
        return this.digest;
    }

    public void setHexDigest(String NewHexDigest){
        this.hexDigest = NewHexDigest;
    }

    public String getHexDigest(){
        return this.hexDigest;
    }
}
