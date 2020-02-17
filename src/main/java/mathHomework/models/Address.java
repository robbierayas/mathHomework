package mathHomework.models;

import mathHomework.utils.Base58;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Address {
    EncryptionKey encryptionKey;
    String address;
    String pkHash;

    public Address(EncryptionKey encryptionKey){
        this.encryptionKey=encryptionKey;
        this.pkHash = Address.pkHash(this.encryptionKey.getPublicKey());
        this.address=Address.hashAddressFromPKHash(this.pkHash);
    }
    //TODO

    static String pkHash(String publicKey) {
        return RIPEMD160.ripemd160(SHA256.sha256(publicKey));
    }

    static private String hashAddressFromPKHash(String pkHash) {
        String pkHashShaTwice = pkHashShaTwice(pkHash);
        byte[] checkSumBytes = getCheckSumBytes(AddressConstants.versionByte + pkHashShaTwice);
        String address = base58(AddressConstants.versionByte +pkHash+new String(checkSumBytes));
        return address;
    }

    static String pkHashShaTwice(String pkHash) {
        return SHA256.sha256(SHA256.sha256(pkHash));
    }

    static byte[] getCheckSumBytes(String pkHashShaTwice) {
        byte[] pkHashBytes = pkHashShaTwice.getBytes();
        return Arrays.copyOfRange(pkHashBytes, 0, 4);
    }

    static String base58(String s) {
        return Base58.encode(s.getBytes(StandardCharsets.ISO_8859_1));
    }

}
