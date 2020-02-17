package mathHomework.models;

import mathHomework.utils.Base58;
import mathHomework.utils.BitwiseFunction;

import java.math.BigInteger;
import java.nio.charset.Charset;
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

    static String hashAddressFromPKHash(String pkHash) {
        String pkHashShaTwice = pkHashShaTwice(AddressConstants.versionByteUnicode + pkHash);
        byte[] checkSumBytes = getCheckSumBytes( pkHashShaTwice);
        String checkSumString =new String(checkSumBytes);/////not correct;
        String address = '1' + base58(AddressConstants.versionByteUnicode +pkHash+checkSumString);
        return address;
    }

    static String pkHashShaTwice(String pkHash) {
        return SHA256.sha256(BitwiseFunction.utf8ToUnicode(SHA256.sha256(pkHash)));
    }

    static byte[] getCheckSumBytes(String pkHashShaTwice) {
        byte[] checkSumBytes = new byte[4];
        int i =0;
        for(char c : BitwiseFunction.utf8ToUnicode(pkHashShaTwice).substring(0,4).toCharArray()){
            checkSumBytes[i++]=(byte)c;
        }
        return checkSumBytes;
    }

    static String base58(String s) {
        BigInteger base256decoded = new BigInteger("0");
        for( char c : s.toCharArray()) {
            base256decoded = base256decoded.multiply(new BigInteger("256")).add(BigInteger.valueOf((int) c));
        }
        BigInteger bigInt58 = new BigInteger("58");
        String result = "";
        while (base256decoded.compareTo(BitwiseFunction.ZERO_BIG_INT) == 1){
            result=AddressConstants.base58Charset.charAt(base256decoded.mod(bigInt58).intValue())+result;
            base256decoded = base256decoded.divide(bigInt58);
        }
        return result;
    }

}