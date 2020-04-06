package mathHomework.models;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ECDSATest {
    @Test
    public void testRIPEMD160(){
        String result = RIPEMD160.ripemd160("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368");
        assertEquals("f3cd5ddd30ad4d28f13cf195786f2e95e8914b22",result);
    }

    @Test
    public void testPadMessage(){
        String result = RIPEMD160.padMessage("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368");
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed3688000000000000000000000000000000000000000000000000001000000000000",result);
    }


    @Test
    public void testChunkMessage(){
        String[] result = RIPEMD160.chunkMessage("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed3688000000000000000000000000000000000000000000000000001000000000000");
        assertArrayEquals(new String[]{"73d6c4f6", "fe2f916f", "14fb65be", "a1c2aa97", "8837d011", "d7b5393f", "9fd3a02e", "68d36e21", "00000080", "00000000", "00000000", "00000000", "00000000", "00000000", "00000100", "00000000"}
                        ,result);
    }

    @Test
    public void testProcessRound(){
        Map<String, BigInteger> currentValues = Stream.of(new Object[][] {
                { "Aleft", new BigInteger("67452301",16)},
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
        String[] messageBlocks = new String[]{"73d6c4f6", "fe2f916f", "14fb65be", "a1c2aa97", "8837d011", "d7b5393f", "9fd3a02e", "68d36e21", "00000080", "00000000", "00000000", "00000000", "00000000", "00000000", "00000100", "00000000"};
        Map<String, BigInteger>  result = RIPEMD160.processRound(currentValues,0,0, messageBlocks);

        Map<String, BigInteger> expected = Stream.of(new Object[][] {
                { "Aleft", new BigInteger("c3d2e1f0",16) },
                { "Bleft", new BigInteger("cc2aa403",16) },
                { "Cleft", new BigInteger("efcdab89",16) },
                { "Dleft", new BigInteger("eb73fa62",16) },
                { "Eleft", new BigInteger("10325476",16) },
                { "Aright", new BigInteger("c3d2e1f0",16) },
                { "Bright", new BigInteger("930f7e8f",16) },
                { "Cright", new BigInteger("efcdab89",16) },
                { "Dright", new BigInteger("eb73fa62",16) },
                { "Eright", new BigInteger("10325476",16) }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        assertEquals(expected,result);
    }

    @Test
    public void testCompress(){
        Map<String, BigInteger> currentValues = Stream.of(new Object[][] {
                { "Aleft", new BigInteger("67452301",16)},
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
        BigInteger[] result = RIPEMD160.compress(currentValues,"left",1, new BigInteger("1943454966",10),new BigInteger("0",10),11);
        assertArrayEquals(new BigInteger[]{ new BigInteger("c3d2e1f0",16),  new BigInteger("cc2aa403",16),  new BigInteger("efcdab89",16),  new BigInteger("eb73fa62",16),  new BigInteger("10325476",16)},result);
    }

    @Test
    public void testAddOutput(){
        Map<String, BigInteger> currentValues= Stream.of(new Object[][] {
                { "Aleft", new BigInteger("82a24ea5",16) },
                { "Bleft", new BigInteger("11e8ef41",16) },
                { "Cleft", new BigInteger("e109aea8",16) },
                { "Dleft", new BigInteger("e3474402",16) },
                { "Eleft", new BigInteger("302cc6dc",16) },
                { "Aright", new BigInteger("5592219f",16) },
                { "Bright", new BigInteger("4eb93ee3",16) },
                { "Cright", new BigInteger("a91d7fa6",16) },
                { "Dright", new BigInteger( "c8673c2",16) },
                { "Eright", new BigInteger("ac4b8c30",16) }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
        String result = RIPEMD160.addOutput(currentValues);
        assertEquals("f3cd5ddd30ad4d28f13cf195786f2e95e8914b22",result);
    }


}
