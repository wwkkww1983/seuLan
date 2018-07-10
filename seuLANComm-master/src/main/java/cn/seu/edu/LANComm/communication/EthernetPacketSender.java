package cn.seu.edu.LANComm.communication;

import cn.seu.edu.LANComm.communication.util.DataLinkParameterEnum;
import cn.seu.edu.LANComm.communication.util.FramingEncoder;
import cn.seu.edu.LANComm.communication.util.MACStringConvertor;
import cn.seu.edu.LANComm.communication.util.NetworkInterfaceUtil;
import cn.seu.edu.LANComm.communication.util.ParameterUnitEnum;
import cn.seu.edu.LANComm.ui.TimedDialog;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * Created by Administrator on 2018/1/29.
 * @author WYCPhoenix
 * @date 2018-2-5-19:54
 */
public class EthernetPacketSender {
    /**
     * 发送数据最小的个数
     */
    private static final int MINIUM_BYTES = 46;
    private static final int MAXIUM_BYTES = 1400;
    /**
     * 发送MAC帧
     * @param destMAC 目的MAC地址 6 byte
     * @param srcMAC 源MAC地址 6 byte
     * @param frameType 协议类型或长度 4 byte；
     * @param data 数据段， 大于 46 byte 且小于1400 byte
     *             嗯，虽然试过更小的字节数也是可以的，但还是按照CSMA/CD协议来吧
     * @param sender jpcap发送实例
     */
    @SuppressWarnings("all")
    private static void sendData(short frameType, byte[]destMAC, byte[] srcMAC, byte[] data, JpcapSender sender) {
        if (!checkData(frameType, destMAC, srcMAC, data, sender)) {
            TimedDialog.getDialog("警告","发送数据不合理", JOptionPane.INFORMATION_MESSAGE ,false,0);
        }
        // 组装MAC帧
        EthernetPacket ethernetPacket = new EthernetPacket();
        // 同步头、间隔标识、CRC由系统添加，其余用于指定
        ethernetPacket.frametype = frameType;
        ethernetPacket.src_mac = srcMAC;
        ethernetPacket.dst_mac = destMAC;
        Packet packet = new Packet();
        packet.datalink = ethernetPacket;
        packet.data = data;
        sender.sendPacket(packet);
    }

    /**
     * 校验发送帧的数据长度
     * @param destMAC
     * @param srcMAC
     * @param frameType
     * @param data
     * @param sender
     * @return
     */
    @SuppressWarnings("all")
    private static boolean checkData(short frameType, byte[]destMAC, byte[] srcMAC, byte[] data, JpcapSender sender) {
        if (destMAC.length != 6) {
            return false;
        }
        if (srcMAC.length != 6) {
            return false;
        }
        if (data.length <MINIUM_BYTES || data.length > MAXIUM_BYTES) {
            return false;
        }
        if (sender == null) {
            return false;
        }
        return true;
    }

    /**
     * 向所有指定的MAC地址发送数据包
     * @param receiverMACs 目的MAC
     * @param localMAC 本地MAC
     * @param dataLinkParameterEnum 消息类型
     * @param data 待发送的消息
     */
    public static void sendEthernetPacket(String[] receiverMACs, String localMAC, DataLinkParameterEnum dataLinkParameterEnum, float[] data) {
        NetworkInterface deviceUsed = NetworkInterfaceUtil.getDesignateDeviceByMACString(localMAC);
        if (deviceUsed != null) {
            JpcapSender sender = null;
            try {
                sender = JpcapSender.openDevice(deviceUsed);
            } catch (IOException e) {
                TimedDialog.getDialog("错误","网卡打开失败，请确认上位机MAC地址或者重启程序", JOptionPane.ERROR_MESSAGE, false,0);
            }
            // float数组转为字节数组
            byte[] dataBytes = FramingEncoder.getByteToSend(dataLinkParameterEnum, data);
            // 检查dataBytes长度，长度不足 MINIUM_BYTES 的填充0
            byte[] bytesToSend = null;
            if (dataBytes.length < MINIUM_BYTES) {
                bytesToSend = new byte[MINIUM_BYTES];
                for (int i = 0; i < MINIUM_BYTES; i++) {
                    if (i < dataBytes.length) {
                        bytesToSend[i] = dataBytes[i];
                    } else {
                        bytesToSend[i] = 0;
                    }
                }
            } else {
                bytesToSend = dataBytes;
            }
            byte[] srcMAC = MACStringConvertor.stringToMAC(localMAC);
            // 向所有指定MAC发送数据帧
            for (String MAC : receiverMACs) {
                byte[] desMAC = MACStringConvertor.stringToMAC(MAC);
                sendData(Short.parseShort(DataLinkParameterEnum.FRAME_TYPE.getDataType()),
                        desMAC, srcMAC, bytesToSend, sender);
            }
            if (sender != null) {
                sender.close();
            }
        } else {
            TimedDialog.getDialog("错误","上位机MAC地址选择错误", JOptionPane.ERROR_MESSAGE, false,0);
        }

    }

    public static void main(String[] args) {
        String[] desMAC = {"00-0C-29-62-C7-C8",
                            "00-0C-29-1E-AA-3F"};
        String srcMAC = "00-23-24-B0-96-A7";
        float[] data = {1F,3.14F,3F,3.14F,4F,6F};
        System.out.println("开始发送");
        DataLinkParameterEnum dataLinkParameterEnum = DataLinkParameterEnum.COMMUNICATION_START;
        sendEthernetPacket(desMAC, srcMAC, dataLinkParameterEnum, data);
        System.out.println("发送结束");
    }

    /**
     * 根据单位，获取倍数关系
     * @param unit 单位
     * @return 倍数
     */
    private float getValueByUnit(String unit) {
        EnumSet<ParameterUnitEnum> set = EnumSet.allOf(ParameterUnitEnum.class);
        Iterator<ParameterUnitEnum> iterator = set.iterator();
        float value = 0F;
        while (iterator.hasNext()) {
            ParameterUnitEnum parameterUnitEnum = iterator.next();
            if (parameterUnitEnum.getUnit().equals(unit)) {
                value = parameterUnitEnum.getValue();
                break;
            }
        }
        return value;
    }
}
