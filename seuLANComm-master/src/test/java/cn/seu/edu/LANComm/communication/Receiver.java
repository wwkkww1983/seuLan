package cn.seu.edu.LANComm.communication;

import cn.seu.edu.LANComm.communication.util.FramingDecoder;
import cn.seu.edu.LANComm.communication.util.MACStringConvertor;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

import java.io.IOException;

/**
 * Created by Administrator on 2018/1/29.
 */
public class Receiver implements PacketReceiver{

    public static void main(String[] args) {
        String filterDst = "ether dst 00:0C:29:1E:AA:3F";
        String filterSrcTx = "ether src 00:0C:29:1E:AA:3F";
        String filterSrcRx = "ether src 00:0C:29:62:C7:C8";
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        try {
            JpcapCaptor jpcapCaptor = JpcapCaptor.openDevice(devices[0], 2000, false, 20);
            jpcapCaptor.setFilter(filterSrcRx, true);
            jpcapCaptor.loopPacket(-1, new Receiver());
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void receivePacket(Packet packet) {
        // TODO Auto-generated method stub
        EthernetPacket epacket = (EthernetPacket)packet.datalink;
        if(epacket.frametype == 8511) {
            System.out.println("目的MAC: " + MACStringConvertor.macToString(epacket.dst_mac));
            System.out.println("源MAC: " + MACStringConvertor.macToString(epacket.src_mac));
            System.out.println("FrameType: " + epacket.frametype);
            printByteArray(packet.data);
        }
    }

    private static void printByteArray(byte[] byteArray) {
        FramingDecoder decoder = new FramingDecoder(byteArray);
        System.out.println("数据帧类型： " + decoder.getParameterIDentifier().getDataType() +
                " " + decoder.getParameterIDentifier().getDescription());
        System.out.println("数据长度 ： " + decoder.getDataLen());
        float[] data = decoder.getTransmittedData();
        System.out.println("====数据打印开始====");
        for(int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " , ");
        }
        System.out.println();
        System.out.println("====数据打印结束====");
    }

}

