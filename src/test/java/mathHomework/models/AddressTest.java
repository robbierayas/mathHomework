package mathHomework.models;

import mathHomework.utils.BitwiseFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {
    //TODO
    @Test
    public void testAddress(){
        Address result = new Address(new EncryptionKey("\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý"));
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result.address);
    }

    @Test
    public void testHashAddressFromPublicKey(){
        Address result = new Address(new EncryptionKey("\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý"));
        assertEquals("f6c4d6736f912ffebe65fb1497aac2a111d037883f39b5d72ea0d39f216ed368",result.address);
    }

    @Test
    public void testPkHash(){
        String publicKey="0427d64b2de9f51ac1bf6b287088de3afcf67e8dd820848128cc27f71c18c5f8baefe71cc14052b4989e33a17f4795022f70313561cb3ef3d0b599c49933daa6fd";
        String result = Address.pkHash(BitwiseFunction.hexToAscii(publicKey));
        assertEquals("f3cd5ddd30ad4d28f13cf195786f2e95e8914b22",result);
    }

    @Test
    public void testPkHashShaTwice(){
        String pkHash="f3cd5ddd30ad4d28f13cf195786f2e95e8914b22";
        String result = Address.pkHashShaTwice(BitwiseFunction.hexToAscii(pkHash));
        assertEquals("5c8492ba9c962bd90185764df1d68e106828d9608ebd42a81280e3d7ba7f41f7",result);
    }

    @Test
    public void testGetCheckSumBytes(){
        String pkHashShaTwice = "2bb273f7266ac8e76f07ee55a2ade19704d6177f6b01aee7bc9a346c664798df";
        byte[] result = Address.getCheckSumBytes(pkHashShaTwice);
        byte[] expected = new byte[]{ 50, 98, 98, 50};
        assertArrayEquals(expected,result);
    }

    @Test
    public void testBase58(){
        String byteAddress = "��]�0�M(�<��xo.���K\"\\���";
        String result = Address.base58(byteAddress);
        assertEquals("PE7Djw8d1RthCXNwyYYNBv89mmgVezsvy",result);
    }
}
