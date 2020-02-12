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
        Map<String, BigInteger> encodedhash = hashComputation(preprocessedMessage);
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
    Map<String, BigInteger> hashComputation(BigInteger[][] preprocessedMessage) {
        Map<String, BigInteger> previousRegisterValues = SHA256Constants.H_initial;
        for (int i=0; i<numberOfBlocks; i++) {
            BigInteger[] W = new BigInteger[64];

            // 1 - prepare message schedule 'W'
            for (int t=0;  t<16; t++){
                W[t] = preprocessedMessage[i][t];
            }
            for (int t=16; t<64; t++) {
                W[t] = (lowerSigma1(W[t-2]).add(W[t-7]).add(lowerSigma0(W[t-15]).add(W[t-16])));//.shiftRight(0);
            }

            // 2 - initialise working variables a, b, c, d, e, f, g, h with previous hash value
                    //done above

            // 3 - main loop (note '>>> 0' for 'addition modulo 2^32')
            Map<String, BigInteger> hashValues = null;
            for(int t=0;t<64;t++) {
                hashValues = processRound(previousRegisterValues, W,t);
            }
            previousRegisterValues = computeIntermediateHash(previousRegisterValues, hashValues);
        }
//
//        /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
//
//    }
        return previousRegisterValues;
    }

    @VisibleForTesting
    Map<String, BigInteger> processRound(Map<String, BigInteger> registerValues, BigInteger[] messageSchedule, int round) {
        BigInteger[] register = new BigInteger[8];
        BigInteger T1 = registerValues.get("h").add(upperSigma1(registerValues.get("e")))
                        .add(choice(registerValues.get("e"),registerValues.get("f"),registerValues.get("g")))
                        .add(new BigInteger(SHA256Constants.K[round],16)).add(messageSchedule[round]);
        BigInteger T2 = upperSigma0(registerValues.get("a")).add(majority(registerValues.get("a"),registerValues.get("b"),registerValues.get("c")));
        register[7] = registerValues.get("g");
        register[6] = registerValues.get("f");
        register[5] = registerValues.get("e");
        register[4] = registerValues.get("d").add(T1);
        register[3] = registerValues.get("c");
        register[2] = registerValues.get("b");
        register[1] = registerValues.get("a");
        register[0] = T1.add(T2);
        registerValues =Stream.of(new Object[][] {
                { "a", register[0] },
                { "b", register[1] },
                { "c", register[2] },
                { "d", register[3] },
                { "e", register[4] },
                { "f", register[5] },
                { "g", register[6] },
                { "h", register[7] }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        return registerValues;
    }

    Map<String, BigInteger> computeIntermediateHash(Map<String, BigInteger> registerValues, Map<String, BigInteger> hashValues){
        registerValues =Stream.of(new Object[][] {
                { "a", registerValues.get("a").add(hashValues.get("a"))},
                { "b", registerValues.get("b").add(hashValues.get("b"))},
                { "c", registerValues.get("c").add(hashValues.get("c"))},
                { "d", registerValues.get("d").add(hashValues.get("d"))},
                { "e", registerValues.get("e").add(hashValues.get("e"))},
                { "f", registerValues.get("f").add(hashValues.get("f"))},
                { "g", registerValues.get("g").add(hashValues.get("g"))},
                { "h", registerValues.get("h").add(hashValues.get("h"))}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        //
//            // 4 - compute the new intermediate hash value (note '>>> 0' for 'addition modulo 2^32')
//            H[0] = (H[0]+a) >>> 0;
//            H[1] = (H[1]+b) >>> 0;
//            H[2] = (H[2]+c) >>> 0;
//            H[3] = (H[3]+d) >>> 0;
//            H[4] = (H[4]+e) >>> 0;
//            H[5] = (H[5]+f) >>> 0;
//            H[6] = (H[6]+g) >>> 0;
//            H[7] = (H[7]+h) >>> 0;
        return registerValues;
    }


    @VisibleForTesting
    String addOutput(Map<String, BigInteger> encodedhash) {

////        ///////addOutput
//        // convert H0..H7 to hex strings (with leading zeros)
//        for (let h=0; h<H.length; h++) H[h] = ('00000000'+H[h].toString(16)).slice(-8);
//
//        // concatenate H0..H7, with separator if required
//        const separator = opt.outFormat=='hex-w' ? ' ' : '';
//
//        return H.join(separator);
        return "";
    }

    //TODO Logical Function4.1.2
    @VisibleForTesting
    BigInteger upperSigma0(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,2)
                .or(BitwiseFunction.cyclicRightShift(x,32,13))
                .or(BitwiseFunction.cyclicRightShift(x,32,22));
    }

    @VisibleForTesting
    BigInteger upperSigma1(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,2)
                .or(BitwiseFunction.cyclicRightShift(x,32,13))
                .or(BitwiseFunction.cyclicRightShift(x,32,22));
    }

    @VisibleForTesting
    BigInteger lowerSigma0(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,2)
                .or(BitwiseFunction.cyclicRightShift(x,32,13))
                .or(BitwiseFunction.cyclicRightShift(x,32,22));
    }

    @VisibleForTesting
    BigInteger lowerSigma1(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,2)
                .or(BitwiseFunction.cyclicRightShift(x,32,13))
                .or(BitwiseFunction.cyclicRightShift(x,32,22));
    }

    @VisibleForTesting
    BigInteger choice(BigInteger a, BigInteger b, BigInteger c){
        return BitwiseFunction.cyclicRightShift(a,32,2)
                .or(BitwiseFunction.cyclicRightShift(a,32,13))
                .or(BitwiseFunction.cyclicRightShift(a,32,22));
    }

    @VisibleForTesting
    BigInteger majority(BigInteger a, BigInteger b, BigInteger c){
        return BitwiseFunction.cyclicRightShift(a,32,2)
                .or(BitwiseFunction.cyclicRightShift(b,32,13))
                .or(BitwiseFunction.cyclicRightShift(c,32,22));
    }

}
