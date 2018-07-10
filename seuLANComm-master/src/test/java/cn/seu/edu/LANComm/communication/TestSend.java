package cn.seu.edu.LANComm.communication;

import cn.seu.edu.LANComm.communication.util.MACStringConvertor;
import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

import java.io.IOException;

/**
 * Created by Administrator on 2018/1/30.
 */
public class TestSend {
    public static void main(String[] args) throws Exception{
        String destMAC = "00-0C-29-1E-AA-3F";
        String srcMAC = "00-23-24-B0-96-A7";
        byte[] dest = MACStringConvertor.stringToMAC(destMAC);
        byte[] src = MACStringConvertor.stringToMAC(srcMAC);
        short frameType = (short) 8511;
        byte[] data = new byte[60];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) 0;
        }
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        JpcapSender sender = null;
        try {
            sender = JpcapSender.openDevice(devices[0]);
            sendData(frameType, dest, src, data, sender);
            Thread.sleep(1000);
            System.out.println("发送数据");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送MAC帧
     * @param destMAC 目的MAC地址 6 byte
     * @param srcMAC 源MAC地址 6 byte
     * @param frameType 协议类型或长度 4 byte；
     * @param data 数据段， 大于 46 byte 且小于1400 byte
     * @param sender jpcap发送实例
     */
    @SuppressWarnings("all")
    private static void sendData(short frameType, byte[]destMAC, byte[] srcMAC, byte[] data, JpcapSender sender) {
        if (!checkData(frameType, destMAC, srcMAC, data, sender)) {
            System.out.println("参数不合理");
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
        if (data.length <46 || data.length > 1400) {
            return false;
        }
        if (sender == null) {
            return false;
        }
        return true;
    }
}
