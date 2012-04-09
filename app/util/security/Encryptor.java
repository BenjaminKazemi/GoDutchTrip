package util.security;

import play.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    public final static Encryptor SHA_256 = new Encryptor("SHA-256");
    private String algorithm;

    public Encryptor(String algorithm) {
        this.algorithm = algorithm;
    }

    public synchronized String encrypt(String str) {
        String encryptedString = null;
        if (str != null && !str.isEmpty()) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                byte[] encrypted = md.digest(str.getBytes());
                // Convert the byte to hex format
                StringBuilder hexString = new StringBuilder();
                for (byte b : encrypted) {
                    hexString.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
                }
                encryptedString = hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                Logger.error(e, "No such algorithm exists!!");
            }
        }
        return encryptedString;
    }

    public static void main(String[] args) {
        System.out.println(Encryptor.SHA_256.encrypt("321"));
    }
}
