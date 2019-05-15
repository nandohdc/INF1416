package security;

import javax.crypto.SecretKey;

public class Main {

    public static void main(String[] args) {
        SecretKeyGen keyGen = null;
        SecretKey secretKey = null;
        keyGen = new SecretKeyGen("Fernando");
        secretKey = keyGen.KeyGen("SHA1PRNG","SUN" ,"DES", 56);
        System.out.println(secretKey);
    }

}
