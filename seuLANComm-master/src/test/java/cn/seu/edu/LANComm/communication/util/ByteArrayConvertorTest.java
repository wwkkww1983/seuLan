package cn.seu.edu.LANComm.communication.util;

import org.junit.Assert;
import static cn.seu.edu.LANComm.communication.util.ByteArrayConvetor.*;
import org.junit.Test;

/**
 * Created by Administrator on 2018/2/2.
 */
public class ByteArrayConvertorTest {

    @Test
    public void testChar() {
        char start = Character.MIN_VALUE;
        char endChar = Character.MAX_VALUE;
        while (start < endChar) {
            Assert.assertArrayEquals(new char[]{start}, new char[]{byteArrayToChar(charToByteArray(start),0)});
            start = (char) (start + 1);
        }
    }

    @Test
    public void testShort() {
        short start = Short.MIN_VALUE;
        while (start < Short.MAX_VALUE) {
            Assert.assertArrayEquals(new short[]{start}, new short[]{byteArrayToShort(shortToByteArray(start), 0)});
            start = (short) (start + 1);
        }
    }

    @Test
    public void testInt() {
        int start = -100;
        while (start < 1E9) {
            Assert.assertArrayEquals(new int[]{start}, new int[]{byteArrayToInt(intToByteArray(start),0)});
            start = start + 100;
        }
    }

    @Test
    public void testFloat() {
        float start = -100F;
        while (start < 1E9F) {
            Assert.assertArrayEquals(new float[]{start}, new float[]{byteArrayToFloat(floatToByte(start),0)}, 1E-10F);
            start = start + 100.11F;
        }
    }

    @Test
    public void testLong() {
        long start = -100L;
        while (start < (long) 1E9) {
            Assert.assertArrayEquals(new long[]{start}, new long[]{byteArrayToLong(longToByteArray(start), 0)});
            start = start + 100;
        }
    }

    @Test
    public void testDouble() {
        double start = -100D;
        while (start < 1E9) {
            Assert.assertArrayEquals(new double[]{start}, new double[]{byteArrayToDouble(doubleToByteArray(start), 0)}, 1E-10);
            start = start + 1000.11;
        }
    }
}
