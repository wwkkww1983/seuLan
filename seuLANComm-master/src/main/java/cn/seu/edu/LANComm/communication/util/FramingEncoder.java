package cn.seu.edu.LANComm.communication.util;

/**
 * 以太帧数据段编码，字节存储顺序为Big Endian，帧结构描述
 * 1、data字段：
 *  1、 第一个字节，参数识别符：
 *      1、0 连接测试
 *      2、1 参数配置
 *      3、2 星座数据
 *      4、3 中频时域信号
 *      5、4 跳频图案
 *  2、第二字节，数据段后序数据个数，数据类型为float
 * Created by Administrator on 2018/2/4.
 * @author WYCPhoenix
 * @date 2018-2-4-17:40
 */
public class FramingEncoder {

    public static byte[] getByteToSend(DataLinkParameterEnum parameterIDdentifier, float[] data) {
        // 申请空间
        int dataLen = data.length;
        int len = 1 + 1 + dataLen * 4;
        byte[] bytes = new byte[len];
        // 参数识别符
        bytes[0] = Byte.parseByte(parameterIDdentifier.getDataType());
        // 待发送数据总长度
        bytes[1] = (byte) dataLen;
        for (int index = 2; index <= len - 1;) {
            byte[] floatBytes = ByteArrayConvetor.floatToByte(data[index / 4]);
            bytes[index++] = floatBytes[0];
            bytes[index++] = floatBytes[1];
            bytes[index++] = floatBytes[2];
            bytes[index++] = floatBytes[3];
        }
        return bytes;
    }

}
