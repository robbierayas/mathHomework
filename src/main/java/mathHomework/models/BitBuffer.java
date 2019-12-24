package mathHomework.models;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import static java.lang.Long.SIZE;

//https://gist.github.com/Exerosis/3c024915bfd8d0c9a9f94640ffa32b30
public class BitBuffer {
    private static final byte[] MASKS = new byte[SIZE];
    private static final Unsafe UNSAFE;

    static {
        for (int i = 0; i < MASKS.length; i++)
            MASKS[i] = (byte) (Math.pow(2, i) - 1);

        try {
            final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (Exception reason) {
            throw new RuntimeException(reason);
        }
    }


    private final ByteBuffer bytes;
    private int position = 0;
    private byte bit = 0;
    private long buffer;

    public BitBuffer(ByteBuffer bytes) {
        this.bytes = bytes;
        buffer = bytes.getLong(position);
    }


    public void writeBits(long value, int count) {
        buffer |= value >>> bit;

        //Save the full buffer to memory and pull out the next one.
        if ((bit += count) > 64)
            buffer = bytes.putLong(position++).getLong(position) | value << (bit += -64);
    }

    public long readBits(int count) {
        long value = buffer << bit;
        if ((bit += count) > 64)
            value |= (buffer = bytes.getLong(position++)) >>> (bit += -64);
        return value & MASKS[count];
    }

    public BitBuffer position(int position) {
        this.position = position;
        return this;
    }

    public BitBuffer clear() {
        return position(0);
    }
}