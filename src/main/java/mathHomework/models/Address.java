package mathHomework.models;

import mathHomework.utils.Base58;
import mathHomework.utils.BitwiseFunction;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class Address {
    private EncryptionKey encryptionKey;
    String address;
    String pkHash;

    Address(EncryptionKey encryptionKey){
        this.encryptionKey=encryptionKey;
        this.pkHash = Address.pkHash(this.encryptionKey.getPublicKey());
        this.address=Address.hashAddressFromPKHash(BitwiseFunction.utf8ToUnicode(this.pkHash));
    }
    //TODO

    static String pkHash(String publicKey) {
        return RIPEMD160.ripemd160(SHA256.sha256(publicKey));
    }

    static String hashAddressFromPKHash(String pkHash) {
        String pkHashShaTwice = pkHashShaTwice(AddressConstants.versionByteUnicode + pkHash);
        char[] checkSumBytes = getCheckSumBytes( pkHashShaTwice);
        String checkSumString =new String(checkSumBytes);/////not correct;
        return '1' + base58(AddressConstants.versionByteUnicode +pkHash+checkSumString);
    }

    static String pkHashShaTwice(String pkHash) {
        return SHA256.sha256(BitwiseFunction.utf8ToUnicode(SHA256.sha256(pkHash)));
    }

    static char[] getCheckSumBytes(String pkHashShaTwice) {
        char[] checkSumBytes = new char[4];
        int i =0;
        for(char c : BitwiseFunction.utf8ToUnicode(pkHashShaTwice).substring(0,4).toCharArray()){
            checkSumBytes[i++]=c;
        }
        return checkSumBytes;
    }

    static String base58(String s) {
        BigInteger base256decoded = new BigInteger("0");
        for( char c : s.toCharArray()) {
            base256decoded = base256decoded.multiply(new BigInteger("256")).add(BigInteger.valueOf((int) c));
        }
        BigInteger bigInt58 = new BigInteger("58");
        StringBuilder result = new StringBuilder();
        while (base256decoded.compareTo(BitwiseFunction.ZERO_BIG_INT) > 0){
            result.insert(0, AddressConstants.base58Charset.charAt(base256decoded.mod(bigInt58).intValue()));
            base256decoded = base256decoded.divide(bigInt58);
        }
        return result.toString();
    }

}