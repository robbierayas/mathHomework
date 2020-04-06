package mathHomework.models;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ECDSAConstants {

    public static final BigInteger TWO = BigInteger.TWO;
    public static final BigInteger P_CURVE = (TWO.pow(256)).subtract(TWO.pow(32)).subtract(TWO.pow(9)).subtract(TWO.pow(8)).subtract(TWO.pow(7)).subtract(TWO.pow(6)).subtract(TWO.pow(4)).subtract(BigInteger.ONE);
    public static final BigInteger N = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141",16);

    public static final BigInteger A_CURVE = BigInteger.ZERO;
    public static final BigInteger B_CURVE = new BigInteger("7",10);

    public static final BigInteger Gx = new BigInteger("55066263022277343669578718895168534326250603453777594175500187360389116729240",10);
    public static final BigInteger Gy = new BigInteger("32670510020758816978083085130507043184471273380659243275938904335757337482424",10);

    // PRIVATE //

    /**
     The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private ECDSAConstants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}