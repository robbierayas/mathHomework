package mathHomework.models;

import mathHomework.utils.BitwiseFunction;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;
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
        sha256.numberOfBlocks=1;
        BigInteger[][] preprocessedMessage = new BigInteger[][]{new BigInteger[]{new BigInteger("1633845248"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("16")}};
        Map<String, BigInteger> result = sha256.hashComputation(preprocessedMessage);
        Map<String, BigInteger> expected = Stream.of(new Object[][] {
                { "a", new BigInteger("fb8e20fc",16) },
                { "b", new BigInteger("2e4c3f24",16) },
                { "c", new BigInteger("8c60c39b",16) },
                { "d", new BigInteger("d652f3c1",16) },
                { "e", new BigInteger("347298bb",16) },
                { "f", new BigInteger("977b8b4d",16) },
                { "g", new BigInteger("5903b850",16) },
                { "h", new BigInteger("55620603",16) }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        assertEquals(result,expected);
    }

    @Test
    public void testAddOutput(){
        SHA256 sha256= new SHA256();
        Map<String, BigInteger> encodedhash = Stream.of(new Object[][] {
                { "a", new BigInteger("fb8e20fc",16) },
                { "b", new BigInteger("2e4c3f24",16) },
                { "c", new BigInteger("8c60c39b",16) },
                { "d", new BigInteger("d652f3c1",16) },
                { "e", new BigInteger("347298bb",16) },
                { "f", new BigInteger("977b8b4d",16) },
                { "g", new BigInteger("5903b850",16) },
                { "h", new BigInteger("55620603",16) }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        String result = sha256.addOutput(encodedhash);
        assertEquals("fb8e20fc2e4c3f248c60c39bd652f3c1347298bb977b8b4d5903b85055620603",result);
    }

    @Test
    public void testUpperSigma0(){
        SHA256 sha256= new SHA256();
        BigInteger result = sha256.upperSigma0(new BigInteger("1"));
        assertEquals(new BigInteger("1074267136"),result);
        result = sha256.upperSigma0(new BigInteger("1779033703"));
        assertEquals(new BigInteger("-836717442"),result);
    }

    @Test
    public void testUpperSigma1(){
        SHA256 sha256= new SHA256();
        BigInteger result = sha256.upperSigma1(new BigInteger("1359893119"));
        assertEquals(new BigInteger("898049835"),result);
    }

    @Test
    public void testLowerSigma0(){
        SHA256 sha256= new SHA256();
        BigInteger result = sha256.lowerSigma0(new BigInteger("0"));
        assertEquals(new BigInteger("0"),result);
        result = sha256.lowerSigma0(new BigInteger("16"));
        assertEquals(new BigInteger("537133058"),result);
    }

    @Test
    public void testLowerSigma1(){
        SHA256 sha256= new SHA256();
        BigInteger result = sha256.lowerSigma1(new BigInteger("0"));
        assertEquals(new BigInteger("0"),result);
        result = sha256.lowerSigma1(new BigInteger("16"));
        assertEquals(new BigInteger("655360"),result);
    }

    @Test
    public void testChoice(){
        SHA256 sha256= new SHA256();
        BigInteger result = sha256.choice(new BigInteger("1359893119"),new BigInteger("2600822924"),new BigInteger("528734635"));
        assertEquals(new BigInteger("528861580"),result);
    }

    @Test
    public void testMajority(){
        SHA256 sha256= new SHA256();
        BigInteger result = sha256.majority(new BigInteger("1779033703"),new BigInteger("3144134277"),new BigInteger("1013904242"));
        assertEquals(new BigInteger("980412007"),result);
    }


}
