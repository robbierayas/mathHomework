package mathHomework.models;

import mathHomework.utils.BitwiseFunction;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SHA256Test {
    @Test
    public void testSHA256UsingPublicKey(){
        SHA256 sha256= new SHA256();
        String publicKey="0427d64b2de9f51ac1bf6b287088de3afcf67e8dd820848128cc27f71c18c5f8baefe71cc14052b4989e33a17f4795022f70313561cb3ef3d0b599c49933daa6fd";
        String result = sha256.sha256UsingDigest(BitwiseFunction.hexToAscii(publicKey));
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result);
    }
    @Test

    public void testSHA256UsingDigest(){
        SHA256 sha256= new SHA256();
        String message = "\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý";
        String result = sha256.sha256UsingDigest(message);
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result);
    }

    //TODO
    @Test
    public void testSHA256(){
        SHA256 sha256= new SHA256();
        String result = sha256.sha256("\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý");
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result);
    }

    @Test
    public void testPreprocessing(){
        SHA256 sha256= new SHA256();
        BigInteger[][] expected = new BigInteger[][]{new BigInteger[]{new BigInteger("1633845248"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("16")}};
        BigInteger[][] result = sha256.preprocessing("ab");
        assertArrayEquals((Object[][])expected,(Object[][])result);
    }

    @Test
    public void testHashComputation(){
        SHA256 sha256= new SHA256();
        BigInteger[] result = sha256.hashComputation(new BigInteger[][]{});
        assertEquals(new BigInteger[]{},result);
    }

    @Test
    public void testAddOutput(){
        SHA256 sha256= new SHA256();
        String result = sha256.addOutput(new BigInteger[]{});
        assertEquals("",result);
    }


}
