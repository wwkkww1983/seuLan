package cn.seu.edu.LANComm.communication.util;

import java.util.EnumSet;
import java.util.Iterator;

/**
 * 以太帧数据帧解码，字节存储顺序 Big Endian
 * 1、frameType = 8511
 * 2、data字段：
 *  1、 第一个字节高8bit，参数识别符：
 *      1、0000 连接测试
 *      2、0001 参数配置
 *      3、0010 星座数据
 *      4、0011 中频时域信号
 *      5、0100 跳频图案
 *  2、第一字节的低8bit，数据段后序数据个数，数据类型为float
 * Created by Administrator on 2018/2/4.
 * @author WYCPhoenix
 * @date 2018-2-4-17:41
 */
public class FramingDecoder {
    private byte[] dataReceived;

    public FramingDecoder(byte[] dataReceived) {
        this.dataReceived = dataReceived;
    }

    public DataLinkParameterEnum getParameterIDentifier() {
        DataLinkParameterEnum res = null;
        // 取出第一个字节
        int parameterIDentifier = dataReceived[0];
        StringBuilder stringBuilder = new StringBuilder();
        String dataType = stringBuilder.append(parameterIDentifier).toString();
        // 遍历DataLinkParameterEnum
        EnumSet<DataLinkParameterEnum> enums = EnumSet.allOf(DataLinkParameterEnum.class);
        Iterator<DataLinkParameterEnum> iterator = enums.iterator();
        while (iterator.hasNext()) {
            DataLinkParameterEnum dataLinkParameterEnum = iterator.next();
            if (dataLinkParameterEnum.getDataType().equals(dataType)) {
                res = dataLinkParameterEnum;
                break;
            }
        }
        return res;
    }

    public int getDataLen() {
        // 注意java的byte是有符号的，需要转为无符号数
        int dataLen = dataReceived[1] & 0XFF;
        return dataLen;
    }

    public float[] getTransmittedData() {
        float[] data = new float[getDataLen()];
        int index = 0;
        byte[] dataToDecode = new byte[getDataLen() * 4];
        // 注意这里偏移2个字节，接收字节数组的前两个字节不算在内
        for (int i = 2; i < dataToDecode.length + 2; i++) {
            dataToDecode[i-2] = dataReceived[i];
        }
        while (index < dataToDecode.length) {
            data[index / 4] = ByteArrayConvetor.byteArrayToFloat(dataToDecode,index);
            index = index + 4;

        }
        return data;
    }

    public static void main(String[] args) {
    }
}
