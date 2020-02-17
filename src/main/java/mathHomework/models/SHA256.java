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
    public SHA256() {
    }

    public static String sha256UsingDigest(String message){

        String output ="";
        try {

            System.out.println("Default Charset=" + Charset.defaultCharset());
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String messagedecoded = new String(message.getBytes(StandardCharsets.ISO_8859_1));
            byte[] encodedhash = digest.digest(
                    message.getBytes(StandardCharsets.ISO_8859_1));
            output = Hex.encodeHexString(encodedhash,true);
            String mHessagedecoded = new String(Hex.decodeHex(output));
            String yo="0";
        } catch (NoSuchAlgorithmException | DecoderException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String sha256(String message){

        String output;
        double l = ((double)message.length())/4 + 2; // length (in 32-bit integers) of msg + ‘1’ + appended length
        int numberOfBlocks = (int) Math.ceil(l/16);  // number of 16-integer (512-bit) blocks required to hold 'l' ints
        BigInteger[][] preprocessedMessage = preprocessing(message, numberOfBlocks);
        Map<String, BigInteger> encodedhash = hashComputation(preprocessedMessage, numberOfBlocks);
        output = addOutput(encodedhash);
        return output;
    }

    @VisibleForTesting
    static BigInteger[][] preprocessing(String message, int numberOfBlocks) {
        message += new String(new int[]{128},0,1);  // add trailing '1' bit (+ 0's padding) to string [§5.1.1]

        // convert string msg into 512-bit blocks (array of 16 32-bit integers) [§5.2.1]
        BigInteger[][] M = new BigInteger[numberOfBlocks][16];     // message M is N×16 array of 32-bit integers

        for (int i=0; i<numberOfBlocks; i++) {
            for (int j=0; j<16; j++) { // encode 4 chars per integer (64 per block), big-endian encoding
                M[i][j] = charToBigInteger(message,i*64+j*4).shiftLeft(24)
                            .or(charToBigInteger(message,i*64+j*4+1).shiftLeft(16)
                            .or(charToBigInteger(message,i*64+j*4+2).shiftLeft(8)
                            .or(charToBigInteger(message,i*64+j*4+3))));
            } // note running off the end of msg is ok 'cos bitwise ops on NaN return 0
        }
        // add length (in bits) into final pair of 32-bit integers (big-endian) [§5.1.1]
        // note: most significant word would be (len-1)*8 >>> 32, but since JS converts
        // bitwise-op args to 32 bits, we need to simulate this by arithmetic operators
        BigInteger lenHi = BigDecimal.valueOf(Math.floor(((message.length()-1)*8) / Math.pow(2, 32))).toBigInteger();
        BigInteger lenLo = BigDecimal.valueOf(((message.length()-1)*8)).toBigInteger();
        M[numberOfBlocks-1][14] = lenHi;
        M[numberOfBlocks-1][15] = lenLo;
        return M;
    }

    static BigInteger charToBigInteger(String message, int index){
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
    static Map<String, BigInteger> hashComputation(BigInteger[][] preprocessedMessage, int numberOfBlocks) {
        Map<String, BigInteger> previousRegisterValues = SHA256Constants.H_initial;
        for (int i=0; i<numberOfBlocks; i++) {
            BigInteger[] W = prepareMessageSchedule(i, preprocessedMessage);
            // 2 - initialise working variables a, b, c, d, e, f, g, h with previous hash value
                    //done above

            // 3 - main loop (note '>>> 0' for 'addition modulo 2^32')
            Map<String, BigInteger> hashValues = previousRegisterValues;
            for (int t=0;t<64;t++) {
                hashValues = processRound(hashValues, W,t);
            }
            previousRegisterValues = computeIntermediateHash(previousRegisterValues, hashValues);
        }
        return previousRegisterValues;
    }

    static BigInteger[] prepareMessageSchedule(int i, BigInteger[][] preprocessedMessage) {
        BigInteger[] W = new BigInteger[64];

        // 1 - prepare message schedule 'W'
        System.arraycopy(preprocessedMessage[i], 0, W, 0, 16);
        for (int t=16; t<64; t++) {
            W[t] = BitwiseFunction.mask32(lowerSigma1(W[t-2]).add(W[t-7]).add(lowerSigma0(W[t-15]).add(W[t-16])));//.shiftRight(0);
        }
        return W;
    }

    @VisibleForTesting
    static Map<String, BigInteger> processRound(Map<String, BigInteger> registerValues, BigInteger[] messageSchedule, int round) {
        BigInteger[] register = new BigInteger[8];
        BigInteger T1 = BitwiseFunction.mask32(registerValues.get("h").add(upperSigma1(registerValues.get("e")))
                        .add(choice(registerValues.get("e"),registerValues.get("f"),registerValues.get("g")))
                        .add(new BigInteger(SHA256Constants.K[round],16)).add(messageSchedule[round]));
        BigInteger T2 = BitwiseFunction.mask32(upperSigma0(registerValues.get("a")).add(majority(registerValues.get("a"),registerValues.get("b"),registerValues.get("c"))));
        register[7] = registerValues.get("g");
        register[6] = registerValues.get("f");
        register[5] = registerValues.get("e");
        register[4] = BitwiseFunction.mask32(registerValues.get("d").add(T1));
        register[3] = registerValues.get("c");
        register[2] = registerValues.get("b");
        register[1] = registerValues.get("a");
        register[0] = BitwiseFunction.mask32(T1.add(T2));
        registerValues = Stream.of(new Object[][] {
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

    static Map<String, BigInteger> computeIntermediateHash(Map<String, BigInteger> registerValues, Map<String, BigInteger> hashValues){
        registerValues = Stream.of(new Object[][] {
                { "a", BitwiseFunction.mask32(registerValues.get("a").add(hashValues.get("a")))},
                { "b", BitwiseFunction.mask32(registerValues.get("b").add(hashValues.get("b")))},
                { "c", BitwiseFunction.mask32(registerValues.get("c").add(hashValues.get("c")))},
                { "d", BitwiseFunction.mask32(registerValues.get("d").add(hashValues.get("d")))},
                { "e", BitwiseFunction.mask32(registerValues.get("e").add(hashValues.get("e")))},
                { "f", BitwiseFunction.mask32(registerValues.get("f").add(hashValues.get("f")))},
                { "g", BitwiseFunction.mask32(registerValues.get("g").add(hashValues.get("g")))},
                { "h", BitwiseFunction.mask32(registerValues.get("h").add(hashValues.get("h")))}
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
    static String addOutput(Map<String, BigInteger> encodedhash) {
        StringBuilder outputString = new StringBuilder();
        for (BigInteger h : encodedhash.values()){
            // convert H0..H7 to hex strings (with leading zeros)
            StringBuilder hString = new StringBuilder(h.toString(16));
            while(hString.length() < 8)
                hString.insert(0, "0");
            outputString.append(hString);
        }
////        ///////addOutput
//        // convert H0..H7 to hex strings (with leading zeros)
//        for (let h=0; h<H.length; h++) H[h] = ('00000000'+H[h].toString(16)).slice(-8);
//
//        // concatenate H0..H7, with separator if required
//        const separator = opt.outFormat=='hex-w' ? ' ' : '';
//
//        return H.join(separator);
        return outputString.toString();
    }

    @VisibleForTesting
    static BigInteger upperSigma0(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,2)
                .xor(BitwiseFunction.cyclicRightShift(x,32,13))
                .xor(BitwiseFunction.cyclicRightShift(x,32,22));
    }

    @VisibleForTesting
    static BigInteger upperSigma1(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,6)
                .xor(BitwiseFunction.cyclicRightShift(x,32,11))
                .xor(BitwiseFunction.cyclicRightShift(x,32,25));
    }

    @VisibleForTesting
    static BigInteger lowerSigma0(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,7)
                .xor(BitwiseFunction.cyclicRightShift(x,32,18))
                .xor(x.shiftRight(3));
    }

    @VisibleForTesting
    static BigInteger lowerSigma1(BigInteger x){
        return BitwiseFunction.cyclicRightShift(x,32,17)
                .xor(BitwiseFunction.cyclicRightShift(x,32,19))
                .xor(x.shiftRight(10));
    }

    @VisibleForTesting
    static BigInteger choice(BigInteger a, BigInteger b, BigInteger c){
        return (a.and(b)).xor(a.not().and(c));
    }

    @VisibleForTesting
    static BigInteger majority(BigInteger a, BigInteger b, BigInteger c){
        return (a.and(b)).xor(a.and(c)).xor(b.and(c));
    }

}
