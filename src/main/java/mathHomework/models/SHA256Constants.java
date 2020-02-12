package mathHomework.models;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SHA256Constants {

    public static final BigInteger MASK = new BigInteger("FFFFFFFF",16);
    // constants [§4.2.2]
    public static final String[] K = new String[]{
            "428a2f98", "71374491", "b5c0fbcf", "e9b5dba5", "3956c25b", "59f111f1", "923f82a4", "ab1c5ed5",
            "d807aa98", "12835b01", "243185be", "550c7dc3", "72be5d74", "80deb1fe", "9bdc06a7", "c19bf174",
            "e49b69c1", "efbe4786", "0fc19dc6", "240ca1cc", "2de92c6f", "4a7484aa", "5cb0a9dc", "76f988da",
            "983e5152", "a831c66d", "b00327c8", "bf597fc7", "c6e00bf3", "d5a79147", "06ca6351", "14292967",
            "27b70a85", "2e1b2138", "4d2c6dfc", "53380d13", "650a7354", "766a0abb", "81c2c92e", "92722c85",
            "a2bfe8a1", "a81a664b", "c24b8b70", "c76c51a3", "d192e819", "d6990624", "f40e3585", "106aa070",
            "19a4c116", "1e376c08", "2748774c", "34b0bcb5", "391c0cb3", "4ed8aa4a", "5b9cca4f", "682e6ff3",
            "748f82ee", "78a5636f", "84c87814", "8cc70208", "90befffa", "a4506ceb", "bef9a3f7", "c67178f2" };

    // initial hash value [§5.3.3]
//    public static final String[] H_initial = new String[]{
//            "6a09e667", "bb67ae85", "3c6ef372", "a54ff53a", "510e527f", "9b05688c", "1f83d9ab", "5be0cd19" };
    public static final Map<String, BigInteger> H_initial = Stream.of(new Object[][] {
            { "a", new BigInteger("6a09e667",16) },
            { "b", new BigInteger("bb67ae85",16) },
            { "c", new BigInteger("3c6ef372",16) },
            { "d", new BigInteger("a54ff53a",16) },
            { "e", new BigInteger("510e527f",16) },
            { "f", new BigInteger("9b05688c",16) },
            { "g", new BigInteger("1f83d9ab",16) },
            { "h", new BigInteger("5be0cd19",16) }
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigInteger) data[1]));
    // PRIVATE //

    /**
     The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private SHA256Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}