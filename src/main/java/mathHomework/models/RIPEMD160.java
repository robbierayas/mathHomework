package mathHomework.models;


import com.google.common.annotations.VisibleForTesting;
import mathHomework.utils.BitwiseFunction;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RIPEMD160 {
    public String[] messageBlocks;
    public Map<String, BigInteger> initialValues = Stream.of(new Object[][] {
            { "Aleft", new BigInteger("67452301",16) },
            { "Bleft", new BigInteger("efcdab89",16) },
            { "Cleft", new BigInteger("98badcfe",16) },
            { "Dleft", new BigInteger("10325476",16) },
            { "Eleft", new BigInteger("c3d2e1f0",16) },
            { "Aright", new BigInteger("67452301",16) },
            { "Bright", new BigInteger("efcdab89",16) },
            { "Cright", new BigInteger("98badcfe",16) },
            { "Dright", new BigInteger("10325476",16) },
            { "Eright", new BigInteger("c3d2e1f0",16) }
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));

    public RIPEMD160() {
    }

    public String ripemd160(String message){
        String paddedMessage=padMessage(message);
        //process message into chunks
        this.messageBlocks = chunkMessage(paddedMessage);
        //use 10 rounds of 16 bit ops on message block and buffer - in 2 parallel lines of 5
        Map<String, BigInteger> registerValues = initialValues;
        for(int round=0;round<5;round++){
            for(int j=0;j<16;j++){
                registerValues=processRound(registerValues, round,j);
            }
        }

        //add output to message to form new buffer value
        //convert h0, h1, h2, h3 and h4 in hex, then add, little endian
        String output=addOutput(registerValues);

        //output hash value is the final buffer value
        return output;
    }

    @VisibleForTesting
    String padMessage(String message) {
        //convert hex to bin
        String binaryMessage = BitwiseFunction.hexToBin(message);
        //get message length
        String messageLength=String.format("%64s",  Integer.toBinaryString(binaryMessage.length())).replace(' ', '0');
        //append one
        binaryMessage += "1";
        //append zeroes
        binaryMessage = String.format("%512s",String.format("%-448s", binaryMessage).replace(' ', '0')).replace(' ', '0');
        //get little endian of message
        String litteEndian="";
        for(int x=0;x<(messageLength.length()/32);x++) {
            litteEndian=BitwiseFunction.littleEndian(messageLength.substring(x*32,(x+1)*32), 2)+litteEndian;
        }
        //add little endian to message
        binaryMessage+=litteEndian;
        return BitwiseFunction.binToHex(binaryMessage);
    }

    @VisibleForTesting
    String[] chunkMessage(String message) {
        //split message to 16 32-bit words
        String[] chunks = new String[16];
        for(int x=0;x<(message.length()/8);x++) {
            chunks[x]=BitwiseFunction.littleEndian(message.substring(x*8,(x+1)*8),16);
        }
        return chunks;
    }
    @VisibleForTesting
    Map<String, BigInteger> processRound(Map<String, BigInteger> registerValues, int round, int bitOp) {
        //get constants for this round
        int rho=RIPEMD160Constants.RHO.get(bitOp);
        int pi=(9*bitOp+5)%16;

        int leftWord=RIPEMD160Constants.WORDSELECTLEFT.get(round*16+bitOp);
        int rightWord=RIPEMD160Constants.WORDSELECTRIGHT.get(round*16+bitOp);

        BigInteger xLeft=new BigInteger(messageBlocks[leftWord],16);
        BigInteger xRight=new BigInteger(messageBlocks[rightWord],16);

        BigInteger KLeft = RIPEMD160Constants.CONSTANTMAPLEFT.get(round);
        BigInteger KRight= RIPEMD160Constants.CONSTANTMAPRIGHT.get(round);

        int sLeft = RIPEMD160Constants.SHIFTMAPLEFT.get(round*16+bitOp);
        int sRight = RIPEMD160Constants.SHIFTMAPRIGHT.get(round*16+bitOp);

        int fLeft=RIPEMD160Constants.FUNCTIONMAPLEFT.get(round);
        int fRight=RIPEMD160Constants.FUNCTIONMAPRIGHT.get(round);

        //do compression
        BigInteger[] outputLeft  = compress(registerValues,"left",fLeft,xLeft,KLeft,sLeft);
        BigInteger[] outputRight = compress(registerValues,"right",fRight,xRight,KRight,sRight);

        registerValues =Stream.of(new Object[][] {
                { "Aleft", outputLeft[0] },
                { "Bleft", outputLeft[1] },
                { "Cleft", outputLeft[2] },
                { "Dleft", outputLeft[3] },
                { "Eleft", outputLeft[4] },
                { "Aright", outputRight[0] },
                { "Bright", outputRight[1] },
                { "Cright", outputRight[2] },
                { "Dright", outputRight[3] },
                { "Eright", outputRight[4] }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));

        //assign for next round
        return registerValues;
    }

    @VisibleForTesting
    BigInteger[] compress(Map<String, BigInteger> currentValues,String side, int function,BigInteger X, BigInteger K, int s) {
        //calculate A
        BigInteger a= currentValues.get("A"+side);
        BigInteger b= currentValues.get("B"+side);
        BigInteger c= currentValues.get("C"+side);
        BigInteger d= currentValues.get("D"+side);
        BigInteger e= currentValues.get("E"+side);
        BigInteger aOut=BitwiseFunction.ZERO_BIG_INT;
        if(function==1){
            aOut=a.add(b.xor(c).xor(d)).and(RIPEMD160Constants.MASK);
        }else if(function == 2){
            aOut=a.add(b.and(c).or(b.not().and(d))).and(RIPEMD160Constants.MASK);
        }else if(function == 3){
            aOut=a.add(b.or(c.not()).xor(d)).and(RIPEMD160Constants.MASK);
        }else if(function == 4){
            aOut=a.add(b.and(d).or(c.and(d.not()))).and(RIPEMD160Constants.MASK);
        }else if(function == 5){
            aOut=a.add(b.xor(c.or(d.not()))).and(RIPEMD160Constants.MASK);
        }
//        BigInteger ax=aOut.add(X);
//        BigInteger ax1=aOut.add(X).and(RIPEMD160Constants.MASK);
//        BigInteger ak=aOut.add(X).and(RIPEMD160Constants.MASK).add(K).and(RIPEMD160Constants.MASK);

//        aOut=cyclicLeftShift(aOut.add(X).and(RIPEMD160Constants.MASK).add(K).and(RIPEMD160Constants.MASK),32,s);
        aOut=BitwiseFunction.cyclicLeftShift(aOut.add(X).and(RIPEMD160Constants.MASK).add(K).and(RIPEMD160Constants.MASK),32,s);
        aOut=aOut.add(e).and(RIPEMD160Constants.MASK);

        //calculate C
        BigInteger cOut=BitwiseFunction.cyclicLeftShift(c,32,10).mod(RIPEMD160Constants.MASK);

        //set values
        BigInteger[] output=new BigInteger[5];
        output[0]=e.mod(RIPEMD160Constants.MASK);
        output[1]=aOut.and(RIPEMD160Constants.MASK);
        output[2]=b.mod(RIPEMD160Constants.MASK);
        output[3]=cOut.mod(RIPEMD160Constants.MASK);
        output[4]=d.mod(RIPEMD160Constants.MASK);
        return output;
    }

    @VisibleForTesting
    String addOutput(Map<String, BigInteger> currentValues) {
        //add output to message to form new buffer value
        //convert h0, h1, h2, h3 and h4 in hex, then add, little endian
        //output hash value is the final buffer value
        BigInteger a= initialValues.get("Aleft");
        BigInteger b= initialValues.get("Bleft");
        BigInteger c= initialValues.get("Cleft");
        BigInteger d= initialValues.get("Dleft");
        BigInteger e= initialValues.get("Eleft");
        BigInteger aLeft= currentValues.get("Aleft");
        BigInteger bLeft= currentValues.get("Bleft");
        BigInteger cLeft= currentValues.get("Cleft");
        BigInteger dLeft= currentValues.get("Dleft");
        BigInteger eLeft= currentValues.get("Eleft");
        BigInteger aRight= currentValues.get("Aright");
        BigInteger bRight= currentValues.get("Bright");
        BigInteger cRight= currentValues.get("Cright");
        BigInteger dRight= currentValues.get("Dright");
        BigInteger eRight= currentValues.get("Eright");
        BigInteger[] output=new BigInteger[5];
        output[0]=b.add(cLeft).add(dRight).and(RIPEMD160Constants.MASK);
        output[1]=c.add(dLeft).add(eRight).and(RIPEMD160Constants.MASK);
        output[2]=d.add(eLeft).add(aRight).and(RIPEMD160Constants.MASK);
        output[3]=e.add(aLeft).add(bRight).and(RIPEMD160Constants.MASK);
        output[4]=a.add(bLeft).add(cRight).and(RIPEMD160Constants.MASK);
        String outputString="";

        for(BigInteger bigInteger: output){
            outputString+=BitwiseFunction.littleEndian(bigInteger).toString(16);
        }

        return outputString;
    }



}
