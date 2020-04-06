package mathHomework.models;

import mathHomework.utils.BitwiseFunction;

import java.math.BigInteger;
import java.util.Random;

public class EncryptionKey {
    String publicKey;
    String privateKey;
    boolean privateKeyFound;

    public EncryptionKey(String publicKey) {
        this.publicKey = publicKey;
        this.privateKeyFound = false;
    }

    public EncryptionKey(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = BitwiseFunction.utf8ToUnicode(privateKey);
        this.privateKeyFound = true;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public boolean isPrivateKeyFound() {
        return privateKeyFound;
    }

    public void setPrivateKeyFound(boolean privateKeyFound) {
        this.privateKeyFound = privateKeyFound;
    }
    //TODO
    static public EncryptionKey createEncryptionKey(String privateKey){
        String publicKey = "ecdsa";
        return new EncryptionKey(publicKey, privateKey);
    }

    static public EncryptionKey createEncryptionKey(){
        String privateKey = new BigInteger(256, new Random()).toString();
        String publicKey = ECDSA.ecdsa(privateKey);
        return new EncryptionKey(publicKey, privateKey);
    }
}
