package mathHomework.models;


import com.google.common.annotations.VisibleForTesting;
import mathHomework.utils.BitwiseFunction;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SHA256 {
//https://www.movable-type.co.uk/scripts/sha256.html
    public int numberOfBlocks = 0;
    public SHA256() {
    }

    public String sha256UsingDigest(String message){

        String output ="";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    message.getBytes(StandardCharsets.ISO_8859_1));
            output = Hex.encodeHexString(encodedhash,true);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return output;
    }

    //TODO
    public String sha256(String message){

        String output ="";
        BigInteger[][] preprocessedMessage = preprocessing(message);
        BigInteger[] encodedhash = hashComputation(preprocessedMessage);
        output = addOutput(encodedhash);
        return output;
    }

    @VisibleForTesting
    BigInteger[][] preprocessing(String message) {
        message += new String(new int[]{128},0,1);  // add trailing '1' bit (+ 0's padding) to string [§5.1.1]

        // convert string msg into 512-bit blocks (array of 16 32-bit integers) [§5.2.1]
        double l = ((double)message.length())/4 + 2; // length (in 32-bit integers) of msg + ‘1’ + appended length
        numberOfBlocks = (int) Math.ceil(l/16);  // number of 16-integer (512-bit) blocks required to hold 'l' ints
        BigInteger[][] M = new BigInteger[numberOfBlocks][16];     // message M is N×16 array of 32-bit integers

        for (int i=0; i<numberOfBlocks; i++) {
            for (int j=0; j<16; j++) { // encode 4 chars per integer (64 per block), big-endian encoding
                M[i][j] = charToBigInteger(message,i*64+j*4+0).shiftLeft(24)
                            .or(charToBigInteger(message,i*64+j*4+1).shiftLeft(16)
                            .or(charToBigInteger(message,i*64+j*4+2).shiftLeft(8)
                            .or(charToBigInteger(message,i*64+j*4+3))));
            } // note running off the end of msg is ok 'cos bitwise ops on NaN return 0
        }
        // add length (in bits) into final pair of 32-bit integers (big-endian) [§5.1.1]
        // note: most significant word would be (len-1)*8 >>> 32, but since JS converts
        // bitwise-op args to 32 bits, we need to simulate this by arithmetic operators
        BigInteger lenHi = BigDecimal.valueOf(Math.floor(((message.length()-1)*8) / Math.pow(2, 32))).toBigInteger();
        BigInteger lenLo = BigDecimal.valueOf(((message.length()-1)*8) >>> 0).toBigInteger();
        M[numberOfBlocks-1][14] = lenHi;
        M[numberOfBlocks-1][15] = lenLo;
        return M;
    }

    BigInteger charToBigInteger(String message, int index){
        BigInteger convertedInteger = new BigInteger("0");//new String(new int[]{0},0,1));
        Character charInput = null;
        if(message.length()>index){
            charInput = message.charAt(index);
        }
        if(charInput != null){
            convertedInteger = new BigInteger(String.valueOf((int)charInput));
        }

        return convertedInteger;
    }

    @VisibleForTesting
    BigInteger[] hashComputation(BigInteger[][] preprocessedMessage) {
        for (int i=0; i<numberOfBlocks; i++) {
            BigInteger[] W = new BigInteger[64];

            // 1 - prepare message schedule 'W'
            for (int t=0;  t<16; t++){
                W[t] = preprocessedMessage[i][t];
            }
            for (int t=16; t<64; t++) {
                W[t] = (Sha256.σ1(W[t-2]) + W[t-7] + Sha256.σ0(W[t-15]) + W[t-16]) >>> 0;
            }

            // 2 - initialise working variables a, b, c, d, e, f, g, h with previous hash value
            let a = H[0], b = H[1], c = H[2], d = H[3], e = H[4], f = H[5], g = H[6], h = H[7];

            // 3 - main loop (note '>>> 0' for 'addition modulo 2^32')
            for (let t=0; t<64; t++) {
                const T1 = h + Sha256.Σ1(e) + Sha256.Ch(e, f, g) + K[t] + W[t];
                const T2 =     Sha256.Σ0(a) + Sha256.Maj(a, b, c);
                h = g;
                g = f;
                f = e;
                e = (d + T1) >>> 0;
                d = c;
                c = b;
                b = a;
                a = (T1 + T2) >>> 0;
            }

            // 4 - compute the new intermediate hash value (note '>>> 0' for 'addition modulo 2^32')
            H[0] = (H[0]+a) >>> 0;
            H[1] = (H[1]+b) >>> 0;
            H[2] = (H[2]+c) >>> 0;
            H[3] = (H[3]+d) >>> 0;
            H[4] = (H[4]+e) >>> 0;
            H[5] = (H[5]+f) >>> 0;
            H[6] = (H[6]+g) >>> 0;
            H[7] = (H[7]+h) >>> 0;
        }
//
//        // convert H0..H7 to hex strings (with leading zeros)
//        for (let h=0; h<H.length; h++) H[h] = ('00000000'+H[h].toString(16)).slice(-8);
//
//        // concatenate H0..H7, with separator if required
//        const separator = opt.outFormat=='hex-w' ? ' ' : '';
//
//        return H.join(separator);
//
//        /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
//
//        function utf8Encode(str) {
//        try {
//            return new TextEncoder().encode(str, 'utf-8').reduce((prev, curr) => prev + String.fromCharCode(curr), '');
//        } catch (e) { // no TextEncoder available?
//            return unescape(encodeURIComponent(str)); // monsur.hossa.in/2012/07/20/utf-8-in-javascript.html
//        }
//        }
//
//        function hexBytesToString(hexStr) { // convert string of hex numbers to a string of chars (eg '616263' -> 'abc').
//            const str = hexStr.replace(' ', ''); // allow space-separated groups
//        return str=='' ? '' : str.match(/.{2}/g).map(byte => String.fromCharCode(parseInt(byte, 16))).join('');
//        }
//    }
        return new BigInteger[]{};
    }

    @VisibleForTesting
    String addOutput(BigInteger[] encodedhash) {
        return "";
    }

    //TODO Logical Function4.1.2

}
