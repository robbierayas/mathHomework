package mathHomework.models;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RIPEMD160Constants {

    public static final BigInteger MASK = new BigInteger("FFFFFFFF",16);
    public static final Map<Integer,Integer> ORDERMAP = Stream.of(new Object[][] {
            {0,7},
            {1,4},
            {2,13},
            {3,1},
            {4,10},
            {5,6},
            {6,15},
            {7,3},
            {8,12},
            {9,0},
            {10,9},
            {11,5},
            {12,2},
            {13,14},
            {14,11},
            {15,8}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer,Integer> FUNCTIONMAPLEFT = Stream.of(new Object[][] {
            {0,1},
            {1,2},
            {2,3},
            {3,4},
            {4,5}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer,Integer> FUNCTIONMAPRIGHT = Stream.of(new Object[][] {
            {0,5},
            {1,4},
            {2,3},
            {3,2},
            {4,1}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer,BigInteger> CONSTANTMAPLEFT = Stream.of(new Object[][] {
            {0, new BigInteger("00000000",16)},
            {1, new BigInteger("5A827999",16)},
            {2, new BigInteger("6ED9EBA1",16)},
            {3, new BigInteger("8F1BBCDC",16)},
            {4, new BigInteger("A953FD4E",16)}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (BigInteger) data[1]));

    public static final Map<Integer,BigInteger> CONSTANTMAPRIGHT = Stream.of(new Object[][] {
            {0, new BigInteger("50A28BE6",16)},
            {1, new BigInteger("5C4DD124",16)},
            {2, new BigInteger("6D703EF3",16)},
            {3, new BigInteger("7A6D76E9",16)},
            {4, new BigInteger("00000000",16)}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (BigInteger) data[1]));

    public static final Map<Integer,Integer> SHIFTMAPLEFT = Stream.of(new Object[][] {
            {0,11},
            {1,14},
            {2,15},
            {3,12},
            {4,5},
            {5,8},
            {6,7},
            {7,9},
            {8,11},
            {9,13},
            {10,14},
            {11,15},
            {12,6},
            {13,7},
            {14,9},
            {15,8},
            {16,7},
            {17,6},
            {18,8},
            {19,13},
            {20,11},
            {21,9},
            {22,7},
            {23,15},
            {24,7},
            {25,12},
            {26,15},
            {27,9},
            {28,11},
            {29,7},
            {30,13},
            {31,12},
            {32,11},
            {33,13},
            {34,6},
            {35,7},
            {36,14},
            {37,9},
            {38,13},
            {39,15},
            {40,14},
            {41,8},
            {42,13},
            {43,6},
            {44,5},
            {45,12},
            {46,7},
            {47,5},
            {48,11},
            {49,12},
            {50,14},
            {51,15},
            {52,14},
            {53,15},
            {54,9},
            {55,8},
            {56,9},
            {57,14},
            {58,5},
            {59,6},
            {60,8},
            {61,6},
            {62,5},
            {63,12},
            {64,9},
            {65,15},
            {66,5},
            {67,11},
            {68,6},
            {69,8},
            {70,13},
            {71,12},
            {72,5},
            {73,12},
            {74,13},
            {75,14},
            {76,11},
            {77,8},
            {78,5},
            {79,6}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer,Integer> SHIFTMAPRIGHT = Stream.of(new Object[][] {
            {0,8},
            {1,9},
            {2,9},
            {3,11},
            {4,13},
            {5,15},
            {6,15},
            {7,5},
            {8,7},
            {9,7},
            {10,8},
            {11,11},
            {12,14},
            {13,14},
            {14,12},
            {15,6},
            {16,9},
            {17,13},
            {18,15},
            {19,7},
            {20,12},
            {21,8},
            {22,9},
            {23,11},
            {24,7},
            {25,7},
            {26,12},
            {27,7},
            {28,6},
            {29,15},
            {30,13},
            {31,11},
            {32,9},
            {33,7},
            {34,15},
            {35,11},
            {36,8},
            {37,6},
            {38,6},
            {39,14},
            {40,12},
            {41,13},
            {42,5},
            {43,14},
            {44,13},
            {45,13},
            {46,7},
            {47,5},
            {48,15},
            {49,5},
            {50,8},
            {51,11},
            {52,14},
            {53,14},
            {54,6},
            {55,14},
            {56,6},
            {57,9},
            {58,12},
            {59,9},
            {60,12},
            {61,5},
            {62,15},
            {63,8},
            {64,8},
            {65,5},
            {66,12},
            {67,9},
            {68,12},
            {69,5},
            {70,14},
            {71,6},
            {72,8},
            {73,13},
            {74,6},
            {75,5},
            {76,15},
            {77,13},
            {78,11},
            {79,11}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer,Integer> RHO = Stream.of(new Object[][] {
            {0,7},
            {1,4},
            {2,13},
            {3,1},
            {4,10},
            {5,6},
            {6,15},
            {7,3},
            {8,12},
            {9,0},
            {10,9},
            {11,5},
            {12,2},
            {13,14},
            {14,11},
            {15,8}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer,Integer> WORDSELECTLEFT = Stream.of(new Object[][] {
            {0,0},
            {1,1},
            {2,2},
            {3,3},
            {4,4},
            {5,5},
            {6,6},
            {7,7},
            {8,8},
            {9,9},
            {10,10},
            {11,11},
            {12,12},
            {13,13},
            {14,14},
            {15,15},
            {16,7},
            {17,4},
            {18,13},
            {19,1},
            {20,10},
            {21,6},
            {22,15},
            {23,3},
            {24,12},
            {25,0},
            {26,9},
            {27,5},
            {28,2},
            {29,14},
            {30,11},
            {31,8},
            {32,3},
            {33,10},
            {34,14},
            {35,4},
            {36,9},
            {37,15},
            {38,8},
            {39,1},
            {40,2},
            {41,7},
            {42,0},
            {43,6},
            {44,13},
            {45,11},
            {46,5},
            {47,12},
            {48,1},
            {49,9},
            {50,11},
            {51,10},
            {52,0},
            {53,8},
            {54,12},
            {55,4},
            {56,13},
            {57,3},
            {58,7},
            {59,15},
            {60,14},
            {61,5},
            {62,6},
            {63,2},
            {64,4},
            {65,0},
            {66,5},
            {67,9},
            {68,7},
            {69,12},
            {70,2},
            {71,10},
            {72,14},
            {73,1},
            {74,3},
            {75,8},
            {76,11},
            {77,6},
            {78,15},
            {79,13}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));

    public static final Map<Integer,Integer> WORDSELECTRIGHT = Stream.of(new Object[][] {
            {0,5},
            {1,14},
            {2,7},
            {3,0},
            {4,9},
            {5,2},
            {6,11},
            {7,4},
            {8,13},
            {9,6},
            {10,15},
            {11,8},
            {12,1},
            {13,10},
            {14,3},
            {15,12},
            {16,6},
            {17,11},
            {18,3},
            {19,7},
            {20,0},
            {21,13},
            {22,5},
            {23,10},
            {24,14},
            {25,15},
            {26,8},
            {27,12},
            {28,4},
            {29,9},
            {30,1},
            {31,2},
            {32,15},
            {33,5},
            {34,1},
            {35,3},
            {36,7},
            {37,14},
            {38,6},
            {39,9},
            {40,11},
            {41,8},
            {42,12},
            {43,2},
            {44,10},
            {45,0},
            {46,4},
            {47,13},
            {48,8},
            {49,6},
            {50,4},
            {51,1},
            {52,3},
            {53,11},
            {54,15},
            {55,0},
            {56,5},
            {57,12},
            {58,2},
            {59,13},
            {60,9},
            {61,7},
            {62,10},
            {63,14},
            {64,12},
            {65,15},
            {66,10},
            {67,4},
            {68,1},
            {69,5},
            {70,8},
            {71,7},
            {72,6},
            {73,2},
            {74,13},
            {75,14},
            {76,0},
            {77,3},
            {78,9},
            {79,11}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));
    // PRIVATE //

    /**
     The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private RIPEMD160Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}