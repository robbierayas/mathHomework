package mathHomework.utils;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.Arrays;

public class BitwiseFunction {

    public static final BigInteger ZERO_BIG_INT = new BigInteger("0",16);
    public static final BigInteger ALL_ONES_BIG_INT = new BigInteger("FFFFFFFF",16);

    public static String binToHex(String s){
        return String.format("%128s",  (new BigInteger(s,2)).toString(16)).replace(' ', '0');
    }
    public static String hexToBin(String s){
        return String.format("%256s",  (new BigInteger(s,16)).toString(2)).replace(' ', '0');
    }

    public static String hexstringToString(String s){
        String result="";
        try {
            result= new String(Hex.decodeHex(s), java.nio.charset.StandardCharsets.ISO_8859_1);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String byteArrayToHexString(byte[] byteArray){
        return Hex.encodeHexString(byteArray,true);
    }



    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    public static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            String hexString = Integer.toHexString(ch);
            if(hexString.length()<2){
                hexString = "0"+hexString;
            }
            hex.append(hexString);
        }
        return hex.toString();
    }

    public static String littleEndian(String message, int base) {
        StringBuilder output=new StringBuilder();
        //set string length by base
        int s=2;
        if(base==2){
            s=8;
        }
        for(int x=0;x<(message.length()/s);x++){
            output.insert(0,message.substring(s*x,s*(x+1)));
        }
        return output.toString();
    }


    @VisibleForTesting
    public static BigInteger littleEndian(BigInteger bigInteger) {
        String hexS = bigInteger.toString(16);
        byte[] extractedBytes = bigInteger.toString(16).getBytes();
        BigInteger test1 = new BigInteger(hexS,16);
        BigInteger test2 = new BigInteger(new String(extractedBytes),16);
        int skipped = 0;
        boolean skip = true;
        for (byte b : extractedBytes) {
            boolean signByte = b == (byte) 0x00;
            if (skip && signByte) {
                skipped++;
            } else if (skip) {
                skip = false;
            }
        }
        extractedBytes = Arrays.copyOfRange(extractedBytes, skipped,
                extractedBytes.length);
        byte[] reversed = reverseArray(extractedBytes);
        return new BigInteger(new String(reversed),16);
    }

    @VisibleForTesting
    static byte[] reverseArray(byte [] bytes ) {
        if(bytes.length % 4 == 0 ){
            for(int i = 0; i < bytes.length / 4; i++)
            {
                byte temp = bytes[i*2];
                byte temp2 = bytes[i*2+1];
                bytes[i*2] = bytes[bytes.length - i*2 - 2];
                bytes[i*2+1] = bytes[bytes.length - i*2 - 1];
                bytes[bytes.length - i*2 - 2] = temp;
                bytes[bytes.length - i*2 - 1] = temp2;
            }
        }else{
            for(int i = 0; i < bytes.length / 2; i++)
            {
                byte temp = bytes[i];
                bytes[i] = bytes[bytes.length - i - 1];
                bytes[bytes.length - i - 1] = temp;
            }
        }
        return bytes;
    }

    public static String cyclicShift(String input,int shift){
        return Integer.toHexString(Integer.rotateLeft(new BigInteger(input,16).intValue(),shift));
    }

    public static BigInteger cyclicLeftShift(BigInteger n, int L, int k) {
        return n.shiftLeft(k)
                .or(n.shiftRight(L - k))
                .and(allOnes(L));
    }

    public static BigInteger cyclicRightShift(BigInteger n, int L, int k) {
        return n.shiftRight(k)
                .or(n.shiftLeft(L - k))
                .and(allOnes(L));
    }

    @VisibleForTesting
    static BigInteger allOnes(int L) {
        return BigInteger.ZERO
                .setBit(L)
                .subtract(BigInteger.ONE);
    }
    public static BigInteger mask32(BigInteger a){
        return a.and(ALL_ONES_BIG_INT);
    }
}
