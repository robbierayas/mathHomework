package mathHomework.models;

import mathHomework.utils.BitwiseFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    //TODO
    @Test
    void testAddress(){
        Address result = new Address(new EncryptionKey("\u0004'ÖK-éõ\u001AÁ¿k(p\u0088Þ:üö~\u008DØ \u0084\u0081(Ì'÷\u001C\u0018Åøºïç\u001CÁ@R´\u0098\u009E3¡\u007FG\u0095\u0002/p15aË>óÐµ\u0099Ä\u00993Ú¦ý"));
        assertEquals("f3cd5ddd30ad4d28f13cf195786f2e95e8914b22",result.pkHash);
        assertEquals("1PE7Djw8d1RthCXNwyYYNBv89mmgVezsvy",result.address);
    }

    @Test
    void testPkHash(){
        String publicKey="0427d64b2de9f51ac1bf6b287088de3afcf67e8dd820848128cc27f71c18c5f8baefe71cc14052b4989e33a17f4795022f70313561cb3ef3d0b599c49933daa6fd";
        String result = Address.pkHash(BitwiseFunction.utf8ToUnicode(publicKey));
        assertEquals("f3cd5ddd30ad4d28f13cf195786f2e95e8914b22",result);
    }


    @Test
    void testHashAddressFromPKHash(){
        String publicKey="f3cd5ddd30ad4d28f13cf195786f2e95e8914b22";
        String result = Address.hashAddressFromPKHash(BitwiseFunction.utf8ToUnicode(publicKey));
        assertEquals("1PE7Djw8d1RthCXNwyYYNBv89mmgVezsvy",result);
    }

    @Test
    void testPkHashShaTwice(){
        String pkHash="f3cd5ddd30ad4d28f13cf195786f2e95e8914b22";
        String result = Address.pkHashShaTwice(AddressConstants.versionByteUnicode +BitwiseFunction.utf8ToUnicode(pkHash));
        assertEquals("5c8492ba9c962bd90185764df1d68e106828d9608ebd42a81280e3d7ba7f41f7",result);
    }

    @Test
    void testGetCheckSumBytes(){
        String pkHashShaTwice = "5c8492ba9c962bd90185764df1d68e106828d9608ebd42a81280e3d7ba7f41f7";
        char[] result = Address.getCheckSumBytes(pkHashShaTwice);
        char[] expected = new char[]{ (char)92, (char) 132, (char) 146, (char) 186};
        assertArrayEquals(expected,result);
    }

    @Test
    void testBase58(){
        String byteAddress = "\0óÍ]Ý0\u00ADM(ñ<ñ\u0095xo.\u0095è\u0091K\"\\\u0084\u0092º";
        String result = Address.base58(byteAddress);
        assertEquals("PE7Djw8d1RthCXNwyYYNBv89mmgVezsvy",result);
    }
}
