package cn.seu.edu.LANComm.communication.util;


/**
 * 使用大端原则，实现java的基本类型与byte数组之间的转换
 * Created by Administrator on 2018/2/2.
 * @author WYCPhoenix
 * @date 2018-2-2-14:39
 */
public class ByteArrayConvetor {

    /**
     * char转为byte数组
     * @param c
     * @return
     */
    public static byte[] charToByteArray(char c) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte)((c & 0XFF00) >> 8);
        bytes[1] = (byte)((c & 0X00FF));
        return bytes;
    }

    /**
     * 从byte数组的index位置开始，连续两个字节转为char
     * @param bytes
     * @param index
     * @return
     */
    public static char byteArrayToChar(byte[] bytes, int index) {
        return (char) ((0XFF00 & (bytes[index] << 8)) | (0X00FF & bytes[index +1]));
    }

    /**
     * 将short转为byte数组
     * @param s
     * @return
     */
    public static byte[] shortToByteArray(short s) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte)((s & 0XFF00) >> 8);
        bytes[1] = (byte)(s & 0X00FF);
        return bytes;
    }

    /**
     * 从 index 位置开始，连续2个字节转为short
     * @param bytes
     * @param index
     * @return
     */
    public static short byteArrayToShort(byte[] bytes, int index) {
        return (short) ((0XFF00 & bytes[index] << 8) | (0X00FF & bytes[index + 1]));
    }

    /**
     * 将int转为字节数组
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte)((i & 0XFF000000) >> 24);
        bytes[1] = (byte)((i & 0X00FF0000) >> 16);
        bytes[2] = (byte)((i & 0X0000FF00) >> 8);
        bytes[3] = (byte)(i & 0X000000FF);
        return bytes;
    }

    /**
     * 从index位置开始，连续4个字节转为int
     * @param bytes
     * @param index
     * @return
     */
    public static int byteArrayToInt(byte[] bytes, int index) {
        return (0XFF000000 & (bytes[index] << 24)) |
                (0X00FF0000 & (bytes[index + 1] << 16)) |
                (0X0000FF00 & (bytes[index + 2] << 8)) |
                (0X000000FF & (bytes[index + 3]));
    }

    /**
     * 将float数据转为字节数组
     * @param f
     * @return
     */
    public static byte[] floatToByte(float f) {
        int intBits = Float.floatToIntBits(f);
        return intToByteArray(intBits);
    }

    /**
     * 从index位置开始，连续4个字节转为float
     * @param bytes
     * @param index
     * @return
     */
    public static float byteArrayToFloat(byte[] bytes, int index) {
        return Float.intBitsToFloat(byteArrayToInt(bytes, index));
    }

    /**
     * 将long转为字节数组
     * @param l
     * @return
     */
    public static byte[] longToByteArray(long l) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte)(0XFF & (l >> 56));
        bytes[1] = (byte)(0XFF & (l >> 48));
        bytes[2] = (byte)(0XFF & (l >> 40));
        bytes[3] = (byte)(0XFF & (l >> 32));
        bytes[4] = (byte)(0XFF & (l >> 24));
        bytes[5] = (byte)(0XFF & (l >> 16));
        bytes[6] = (byte)(0XFF & (l >> 8));
        bytes[7] = (byte)(0XFF & l);
        return bytes;
    }

    /**
     * 从index位置开始，连续8个字节转为long
     * @param bytes
     * @param index
     * @return
     */
    public static long byteArrayToLong(byte[] bytes, int index) {
        return  (0XFF00000000000000L & ((long) bytes[index] << 56)) |
                (0X00FF000000000000L & ((long) bytes[index +1] << 48)) |
                (0X0000FF0000000000L & ((long) bytes[index + 2] << 40)) |
                (0X000000FF00000000L & ((long) bytes[index + 3] << 32)) |
                (0X00000000FF000000L & ((long) bytes[index + 4] << 24)) |
                (0X0000000000FF0000L & ((long) bytes[index + 5] << 16)) |
                (0X000000000000FF00L & ((long) bytes[index + 6] << 8)) |
                (0X00000000000000FFL & ((long) bytes[index + 7]));
    }

    /**
     * 将double转为byte数组
     * @param d
     * @return
     */
    public static byte[] doubleToByteArray(double d) {
        long longBits = Double.doubleToLongBits(d);
        return longToByteArray(longBits);
    }

    /**
     * 从index位置开始，连续8个字节转为double
     * @param bytes
     * @param index
     * @return
     */
    public static double byteArrayToDouble(byte[] bytes, int index) {
        return Double.longBitsToDouble(byteArrayToLong(bytes, index));
    }

}
