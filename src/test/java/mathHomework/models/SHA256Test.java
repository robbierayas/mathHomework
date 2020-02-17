package mathHomework.models;

import mathHomework.utils.BitwiseFunction;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SHA256Test {
    @Test
    public void testSHA256UsingPublicKey(){
        String publicKey="0427d64b2de9f51ac1bf6b287088de3afcf67e8dd820848128cc27f71c18c5f8baefe71cc14052b4989e33a17f4795022f70313561cb3ef3d0b599c49933daa6fd";
        String result = SHA256.sha256UsingDigest(BitwiseFunction.hexToAscii(publicKey));
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result);
    }
    @Test

    public void testSHA256UsingDigest(){
        String message = "\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý";
        String result = SHA256.sha256UsingDigest(message);
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result);
    }

    @Test
    public void testSHA256(){
        String result = SHA256.sha256("\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý");
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result);
        String result2 = SHA256.sha256(BitwiseFunction.hexToAscii("0427d64b2de9f51ac1bf6b287088de3afcf67e8dd820848128cc27f71c18c5f8baefe71cc14052b4989e33a17f4795022f70313561cb3ef3d0b599c49933daa6fd"));
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result2);
        String result3 = SHA256.sha256("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368");
        assertEquals("9c618e84ba7bb0b6d208f7f57d9b92bdfb929efba53a3668469115a96105db90",result3);
        String result4 = SHA256.sha256(BitwiseFunction.hexToAscii("9c618e84ba7bb0b6d208f7f57d9b92bdfb929efba53a3668469115a96105db90"));
        assertEquals("5c8492ba9c962bd90185764df1d68e106828d9608ebd42a81280e3d7ba7f41f7",result4);
    }

    @Test
    public void testPreprocessing(){
        BigInteger[][] expected = new BigInteger[][]{new BigInteger[]{new BigInteger("1633845248"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("16")}};
        BigInteger[][] result = SHA256.preprocessing("ab",1);
        assertArrayEquals(expected,result);
    }

    @Test
    public void testHashComputation(){
        BigInteger[][] preprocessedMessage = new BigInteger[][]{new BigInteger[]{new BigInteger("1633845248"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("16")}};
        Map<String, BigInteger> result = SHA256.hashComputation(preprocessedMessage,1);
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
    public void testPrepareMessageSchedule(){
        BigInteger[][] preprocessedMessage = new BigInteger[][]{new BigInteger[]{new BigInteger("1633845248"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("16")}};
        BigInteger[] result = SHA256.prepareMessageSchedule(0, preprocessedMessage);
        BigInteger[] expected = new BigInteger[]{new BigInteger("1633845248"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("16"),new BigInteger("1633845248"),
                new BigInteger("655360"),new BigInteger("270033981"),new BigInteger("1073742468"),new BigInteger("1050487830"),
                new BigInteger("16951296"),new BigInteger("864452582"),new BigInteger("1650638859"),new BigInteger("4027399724"),
                new BigInteger("1211410954"),new BigInteger("2028988364"),new BigInteger("4277374028"),new BigInteger("268710035"),
                new BigInteger("3944143914"),new BigInteger("549890843"),new BigInteger("1386658034"),new BigInteger("2860467165"),
                new BigInteger("1704602302"),new BigInteger("1491927166"),new BigInteger("12639368"),new BigInteger("4093457531"),
                new BigInteger("3985429324"),new BigInteger("1833301693"),new BigInteger("155258341"),new BigInteger("3528335737"),
                new BigInteger("3062717792"),new BigInteger("3012079207"),new BigInteger("539425273"),new BigInteger("1076043381"),
                new BigInteger("2496143508"),new BigInteger("1471727768"),new BigInteger("3093250499"),new BigInteger("3643758778"),
                new BigInteger("432964173"),new BigInteger("3888910579"),new BigInteger("1993602963"),new BigInteger("3286075862"),
                new BigInteger("3578358685"),new BigInteger("600467822"),new BigInteger("3702232564"),new BigInteger("3868419969"),
                new BigInteger("1230845418"),new BigInteger("1369500673"),new BigInteger("1165505681"),new BigInteger("3029804626"),
                new BigInteger("3722238860"),new BigInteger("3963502664"),new BigInteger("1311518612"),
                };
        assertArrayEquals(result,expected);
    }

    @Test
    public void testProcessRound(){
        Map<String, BigInteger> previousRegisterValues = SHA256Constants.H_initial;
        BigInteger[] preprocessedMessage = new BigInteger[]{new BigInteger("1633845248"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),
                new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("16"),new BigInteger("1633845248"),
                new BigInteger("655360"),new BigInteger("270033981"),new BigInteger("1073742468"),new BigInteger("1050487830"),
                new BigInteger("16951296"),new BigInteger("864452582"),new BigInteger("1650638859"),new BigInteger("4027399724"),
                new BigInteger("1211410954"),new BigInteger("2028988364"),new BigInteger("4277374028"),new BigInteger("268710035"),
                new BigInteger("3944143914"),new BigInteger("549890843"),new BigInteger("1386658034"),new BigInteger("2860467165"),
                new BigInteger("1704602302"),new BigInteger("1491927166"),new BigInteger("12639368"),new BigInteger("4093457531"),
                new BigInteger("3985429324"),new BigInteger("1833301693"),new BigInteger("155258341"),new BigInteger("3528335737"),
                new BigInteger("3062717792"),new BigInteger("3012079207"),new BigInteger("539425273"),new BigInteger("1076043381"),
                new BigInteger("2496143508"),new BigInteger("1471727768"),new BigInteger("3093250499"),new BigInteger("3643758778"),
                new BigInteger("432964173"),new BigInteger("3888910579"),new BigInteger("1993602963"),new BigInteger("3286075862"),
                new BigInteger("3578358685"),new BigInteger("600467822"),new BigInteger("3702232564"),new BigInteger("3868419969"),
                new BigInteger("1230845418"),new BigInteger("1369500673"),new BigInteger("1165505681"),new BigInteger("3029804626"),
                new BigInteger("3722238860"),new BigInteger("3963502664"),new BigInteger("1311518612"),
        };
        Map<String, BigInteger> result = SHA256.processRound(previousRegisterValues, preprocessedMessage,0);
        Map<String, BigInteger> expected = Stream.of(new Object[][] {
                { "a",  new BigInteger("1567295565") },
                { "b",  new BigInteger("1779033703") },
                { "c",  new BigInteger("3144134277") },
                { "d",  new BigInteger("1013904242") },
                { "e",  new BigInteger("4197081762") },
                { "f",  new BigInteger("1359893119") },
                { "g",  new BigInteger("2600822924") },
                { "h",  new BigInteger("528734635") }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        assertEquals(result,expected);
    }

    @Test
    public void testComputeIntermediateHash(){
        Map<String, BigInteger> registerValues = SHA256Constants.H_initial;
        Map<String, BigInteger> hashValues = Stream.of(new Object[][] {
                { "a",  new BigInteger("2441362069") },
                { "b",  new BigInteger("1927581855") },
                { "c",  new BigInteger("1341247529") },
                { "d",  new BigInteger("822279815") },
                { "e",  new BigInteger("3814999612") },
                { "f",  new BigInteger("4235600577") },
                { "g",  new BigInteger("964681381") },
                { "h",  new BigInteger("4185995498") }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        Map<String, BigInteger> result = SHA256.computeIntermediateHash(registerValues, hashValues);
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


        Map<String, BigInteger> registerValues2 = Stream.of(new Object[][] {
                { "a",  new BigInteger("1779033703") },
                { "b",  new BigInteger("3144134277") },
                { "c",  new BigInteger("1013904242") },
                { "d",  new BigInteger("2773480762") },
                { "e",  new BigInteger("1359893119") },
                { "f",  new BigInteger("2600822924") },
                { "g",  new BigInteger("528734635") },
                { "h",  new BigInteger("1541459225") }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        Map<String, BigInteger> hashValues2 = Stream.of(new Object[][] {
                { "a",  new BigInteger("1567295565") },
                { "b",  new BigInteger("1779033703") },
                { "c",  new BigInteger("3144134277") },
                { "d",  new BigInteger("1013904242") },
                { "e",  new BigInteger("4197081762") },
                { "f",  new BigInteger("1359893119") },
                { "g",  new BigInteger("2600822924") },
                { "h",  new BigInteger("528734635") }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        Map<String, BigInteger> result2 = SHA256.computeIntermediateHash(registerValues2, hashValues2);
        Map<String, BigInteger> expected2 = Stream.of(new Object[][] {
                { "a", new BigInteger("c774eeb4",16) },
                { "b", new BigInteger("257194ec",16) },
                { "c", new BigInteger("f7d6a1f7",16) },
                { "d", new BigInteger("e1bee8ac",16) },
                { "e", new BigInteger("4b38b521",16) },
                { "f", new BigInteger("ec13bb0b",16) },
                { "g", new BigInteger("ba894237",16) },
                { "h", new BigInteger("7b64a6c4",16) }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        assertEquals(result2,expected2);
    }

    @Test
    public void testAddOutput(){
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
        String result = SHA256.addOutput(encodedhash);
        assertEquals("fb8e20fc2e4c3f248c60c39bd652f3c1347298bb977b8b4d5903b85055620603",result);
    }

    @Test
    public void testUpperSigma0(){
        BigInteger result = SHA256.upperSigma0(new BigInteger("1"));
        assertEquals(new BigInteger("1074267136"),result);
        result = SHA256.upperSigma0(new BigInteger("1779033703"));
        assertEquals(BitwiseFunction.mask32(new BigInteger("-836717442")),result);
    }

    @Test
    public void testUpperSigma1(){
        BigInteger result = SHA256.upperSigma1(new BigInteger("1359893119"));
        assertEquals(BitwiseFunction.mask32(new BigInteger("898049835")),result);
    }

    @Test
    public void testLowerSigma0(){
        BigInteger result = SHA256.lowerSigma0(new BigInteger("0"));
        assertEquals(new BigInteger("0"),result);
        result = SHA256.lowerSigma0(new BigInteger("16"));
        assertEquals(new BigInteger("537133058"),result);
    }

    @Test
    public void testLowerSigma1(){
        BigInteger result = SHA256.lowerSigma1(new BigInteger("0"));
        assertEquals(new BigInteger("0"),result);
        result = SHA256.lowerSigma1(new BigInteger("16"));
        assertEquals(new BigInteger("655360"),result);
    }

    @Test
    public void testChoice(){
        BigInteger result = SHA256.choice(new BigInteger("1359893119"),new BigInteger("2600822924"),new BigInteger("528734635"));
        assertEquals(new BigInteger("528861580"),result);
    }

    @Test
    public void testMajority(){
        BigInteger result = SHA256.majority(new BigInteger("1779033703"),new BigInteger("3144134277"),new BigInteger("1013904242"));
        assertEquals(new BigInteger("980412007"),result);
    }


}
