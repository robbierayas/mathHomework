package mathHomework.utils;

import mathHomework.models.RIPEMD160;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitwiseFunctionTest {
    @Test
    void testBinToHex(){
        String s = "1110";
        String result = BitwiseFunction.binToHex(s);
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e",result);
    }
    @Test
    void testBinToHex2(){
        String s = "11110110110001001101011001110011011011111001000100101111111111101011111001100101111110110001010010010111101010101100001010100001000100011101000000110111100010000011111100111001101101011101011100101110101000001101001110011111001000010110111011010011011010001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000";
        String result = BitwiseFunction.binToHex(s);
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed3688000000000000000000000000000000000000000000000000001000000000000",result);
    }

    @Test
    void testHexToBin(){
        String s = "a";
        String result = BitwiseFunction.hexToBin(s);
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001010",result);
    }

    @Test
    void testHexToBin2(){
        String s = "f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368";
        String result = BitwiseFunction.hexToBin(s);
        assertEquals("1111011011000100110101100111001101101111100100010010111111111110101111100110010111111011000101001001011110101010110000101010000100010001110100000011011110001000001111110011100110110101110101110010111010100000110100111001111100100001011011101101001101101000",result);
    }


    @Test
    void testHexstringToString(){
        String s = "0427d64b2de9f51ac1bf6b287088de3afcf67e8dd820848128cc27f71c18c5f8baefe71cc14052b4989e33a17f4795022f70313561cb3ef3d0b599c49933daa6fd";
        String result = BitwiseFunction.hexstringToString(s);
        assertEquals("\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý",result);
    }

    @Test
    void testByteArrayToHexString(){
        byte[] b = new byte[]{-10,-60,-42,115,111,-111,47,-2,-66,101,-5,20,-105,-86,-62,-95,17,-48,55,-120,63,57,-75,-41,46,-96,-45,-97,33,110,-45,104};
        String result = BitwiseFunction.byteArrayToHexString(b);
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result);
    }


    @Test
    public void testLittleEndian(){
        String result = BitwiseFunction.littleEndian("45679812",16);
        assertEquals("12986745",result);
    }
    @Test
    public void testLittleEndian2(){
        String result = BitwiseFunction.littleEndian("0000001000000000",2);
        assertEquals("0000000000000010",result);
    }
    @Test
    public void testLittleEndian3(){
        String result = BitwiseFunction.littleEndian("0000000011111111",2);
        assertEquals("1111111100000000",result);
    }
    @Test
    public void testLittleEndian4(){
        String result = BitwiseFunction.littleEndian("f6c4d673",16);
        assertEquals("73d6c4f6",result);
    }
    @Test
    public void testCyclicShift(){
        String result = BitwiseFunction.cyclicShift("efcdab89",16);
        assertEquals("ab89efcd",result);
    }
    @Test
    public void testCyclicRightShift(){
        BigInteger result = BitwiseFunction.cyclicRightShift(new BigInteger("9fcf3c9c",16),32,8);
        assertEquals(new BigInteger("9c9fcf3c",16),result);
    }
    @Test
    public void testCyclicLeftShift(){
        BigInteger result = BitwiseFunction.cyclicLeftShift(new BigInteger("9fcf3c9c",16),32,8);
        assertEquals(new BigInteger("cf3c9c9f",16),result);
    }

    @Test
    public void testAllOnes(){
        BigInteger result = BitwiseFunction.allOnes(32);
        assertEquals(new BigInteger("ffffffff",16),result);
    }

    @Test
    public void testLittleEndianBigInteger(){
        BigInteger bi = new BigInteger("dd5dcdf3", 16);
        BigInteger result = BitwiseFunction.littleEndian(bi);
        assertEquals(new BigInteger("f3cd5ddd",16),result);
    }

    @Test
    public void testReverseArray(){
        byte[] array = "1234".getBytes();
        byte[] result = BitwiseFunction.reverseArray(array);
        assertArrayEquals("3412".getBytes(),result);
    }
}
